package com.example.ai.synthetic_data_generator_ai.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.example.ai.synthetic_data_generator_ai.dto.LearnDatabaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DynamicDataService {

    private final Map<String, DataSource> dataSources;

    /**
     * Injects all beans of type DataSource into a Map.
     * The key is the bean name (e.g., "companyDataSource") and the value is the
     * DataSource instance.
     */
    @Autowired
    public DynamicDataService(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
        log.info("List of datasources: {}", dataSources.toString());
    }

    /**
     * Selects a DataSource based on a schema name and performs an operation.
     * 
     * @param schemaName The name of the schema (e.g., "company", "library").
     * @return A string indicating which database was connected to.
     */

    public LearnDatabaseResponse getDatabaseSchema(String schemaName) {
        // Construct the bean name from the request parameter (e.g., "company" ->
        // "companyDataSource")
        String dataSourceName = schemaName + "DataSource";
        DataSource selectedDataSource = dataSources.get(dataSourceName);
        if (selectedDataSource == null) {
            throw new IllegalArgumentException("Invalid schema name provided: " + schemaName);
        }

        JdbcTemplate jdbcTemplate = new JdbcTemplate(selectedDataSource);

        return jdbcTemplate.execute((ConnectionCallback<LearnDatabaseResponse>) connection -> {
            return extractSchema(connection, schemaName);
        });
    }

    private LearnDatabaseResponse extractSchema(Connection connection, String schemaName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog();

            LearnDatabaseResponse response = new LearnDatabaseResponse();
            response.setDatabase(catalog);
            response.setServer(metaData.getDatabaseProductName());
            response.setTables(extractTables(metaData, catalog));

            return response;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching database metadata for schema: " + schemaName, e);
        }
    }

    private List<LearnDatabaseResponse.Table> extractTables(DatabaseMetaData metaData, String catalog)
            throws SQLException {
        List<LearnDatabaseResponse.Table> tables = new ArrayList<>();
        try (ResultSet tablesResultSet = metaData.getTables(catalog, null, "%", new String[] { "TABLE" })) {
            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");
                LearnDatabaseResponse.Table table = new LearnDatabaseResponse.Table();
                table.setName(tableName);
                table.setComment(tablesResultSet.getString("REMARKS"));
                table.setColumns(extractColumns(metaData, catalog, tableName));
                table.setPrimaryKey(extractPrimaryKey(metaData, catalog, tableName));
                table.setForeignKeys(extractForeignKeys(metaData, catalog, tableName));
                table.setIndexes(extractIndexes(metaData, catalog, tableName));
                tables.add(table);
            }
        }
        return tables;
    }

    private List<LearnDatabaseResponse.Column> extractColumns(DatabaseMetaData metaData, String catalog,
            String tableName) throws SQLException {
        List<LearnDatabaseResponse.Column> columns = new ArrayList<>();
        try (ResultSet rs = metaData.getColumns(catalog, null, tableName, "%")) {
            while (rs.next()) {
                LearnDatabaseResponse.Column column = new LearnDatabaseResponse.Column();
                column.setName(rs.getString("COLUMN_NAME"));
                column.setType(rs.getString("TYPE_NAME"));
                column.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
                column.setDefaultValue(rs.getString("COLUMN_DEF"));
                column.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));
                column.setComment(rs.getString("REMARKS"));
                columns.add(column);
            }
        }
        return columns;
    }

    private String extractPrimaryKey(DatabaseMetaData metaData, String catalog, String tableName) throws SQLException {
        try (ResultSet rs = metaData.getPrimaryKeys(catalog, null, tableName)) {
            if (rs.next()) {
                return rs.getString("COLUMN_NAME");
            }
        }
        return null;
    }

    private List<LearnDatabaseResponse.ForeignKey> extractForeignKeys(DatabaseMetaData metaData, String catalog,
            String tableName) throws SQLException {
        List<LearnDatabaseResponse.ForeignKey> foreignKeys = new ArrayList<>();
        try (ResultSet rs = metaData.getImportedKeys(catalog, null, tableName)) {
            while (rs.next()) {
                LearnDatabaseResponse.ForeignKey fk = new LearnDatabaseResponse.ForeignKey();
                fk.setName(rs.getString("FK_NAME"));
                fk.setColumns(List.of(rs.getString("FKCOLUMN_NAME")));
                fk.setReferencedTable(rs.getString("PKTABLE_NAME"));
                fk.setReferencedColumns(List.of(rs.getString("PKCOLUMN_NAME")));
                fk.setUpdateRule(getRuleName(rs.getShort("UPDATE_RULE")));
                fk.setDeleteRule(getRuleName(rs.getShort("DELETE_RULE")));
                foreignKeys.add(fk);
            }
        }
        return foreignKeys;
    }

    private List<LearnDatabaseResponse.Index> extractIndexes(DatabaseMetaData metaData, String catalog,
            String tableName) throws SQLException {
        List<LearnDatabaseResponse.Index> indexes = new ArrayList<>();
        try (ResultSet rs = metaData.getIndexInfo(catalog, null, tableName, false, false)) {
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");
                if (indexName == null)
                    continue;

                LearnDatabaseResponse.Index index = new LearnDatabaseResponse.Index();
                index.setName(indexName);
                index.setColumns(List.of(rs.getString("COLUMN_NAME")));
                index.setUnique(!rs.getBoolean("NON_UNIQUE"));
                index.setType(rs.getString("TYPE")); // This is a short, might need mapping
                indexes.add(index);
            }
        }
        return indexes;
    }

    /**
     * Executes a SQL query against the specified schema.
     *
     * @param schemaName The name of the schema (e.g., "company", "library").
     * @param query      The SQL query to execute.
     * @return A list of maps representing the query result.
     */
    public List<Map<String, Object>> executeQuery(String schemaName, String query) {
        String dataSourceName = schemaName + "DataSource";
        DataSource selectedDataSource = dataSources.get(dataSourceName);

        if (selectedDataSource == null) {
            throw new IllegalArgumentException("Invalid schema name provided: " + schemaName);
        }

        JdbcClient jdbcClient = JdbcClient.create(selectedDataSource);
        return jdbcClient.sql(query).query().listOfRows();
    }

    private String getRuleName(short rule) {
        switch (rule) {
            case DatabaseMetaData.importedKeyCascade:
                return "CASCADE";
            case DatabaseMetaData.importedKeyRestrict:
                return "RESTRICT";
            case DatabaseMetaData.importedKeySetNull:
                return "SET NULL";
            case DatabaseMetaData.importedKeyNoAction:
                return "NO ACTION";
            case DatabaseMetaData.importedKeySetDefault:
                return "SET DEFAULT";
            default:
                return null;
        }
    }
}

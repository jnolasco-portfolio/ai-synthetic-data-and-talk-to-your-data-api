package com.example.ai.synthetic_data_generator_ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.JdbcClient;

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
    public String getDatabaseName(String schemaName) {
        // Construct the bean name from the request parameter (e.g., "company" ->
        // "companyDataSource")
        String dataSourceName = schemaName + "DataSource";
        DataSource selectedDataSource = dataSources.get(dataSourceName);

        if (selectedDataSource == null) {
            throw new IllegalArgumentException("Invalid schema name provided: " + schemaName);
        }

        // Now you can use the selected DataSource to interact with the database
        try (Connection connection = selectedDataSource.getConnection()) {
            String databaseName = connection.getCatalog();
            return "Successfully connected to database: " + databaseName;
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database for schema: " + schemaName, e);
        }
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
}

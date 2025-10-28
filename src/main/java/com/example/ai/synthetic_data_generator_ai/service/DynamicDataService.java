package com.example.ai.synthetic_data_generator_ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Service
public class DynamicDataService {

    private final Map<String, DataSource> dataSources;

    /**
     * Injects all beans of type DataSource into a Map.
     * The key is the bean name (e.g., "companyDataSource") and the value is the DataSource instance.
     */
    @Autowired
    public DynamicDataService(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    /**
     * Selects a DataSource based on a schema name and performs an operation.
     * @param schemaName The name of the schema (e.g., "company", "library").
     * @return A string indicating which database was connected to.
     */
    public String getDatabaseName(String schemaName) {
        // Construct the bean name from the request parameter (e.g., "company" -> "companyDataSource")
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
}

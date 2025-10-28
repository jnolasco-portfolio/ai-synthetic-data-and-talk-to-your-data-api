package com.example.ai.synthetic_data_generator_ai.controller;

import com.example.ai.synthetic_data_generator_ai.service.DynamicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private final DynamicDataService dynamicDataService;

    @Autowired
    public DataController(DynamicDataService dynamicDataService) {
        this.dynamicDataService = dynamicDataService;
    }

    /**
     * An example endpoint to test dynamic datasource selection.
     * Call this with /data/company, /data/library, or /data/restaurant.
     * @param schemaName The name of the schema to connect to.
     * @return A message indicating the result of the connection attempt.
     */
    @GetMapping("/data/{schemaName}")
    public String getDatabaseName(@PathVariable String schemaName) {
        return dynamicDataService.getDatabaseName(schemaName);
    }
}

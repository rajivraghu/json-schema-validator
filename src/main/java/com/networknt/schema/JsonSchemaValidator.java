package com.networknt.schema;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.SpecVersion.VersionFlag;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class JsonSchemaValidator {

    public static void main(String[] args) throws IOException {
        // Load the JSON Schema file
        File schemaFile = new File("/workspaces/json-schema-validator/src/main/resources/schema.json");
        JsonNode schemaNode = JsonLoader.fromFile(schemaFile);

        // Create a JSON Schema object from the schema file
        
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V7);
        
        JsonSchema schema = factory.getSchema(schemaNode);

        // Load the JSON file to be validated
        File jsonFile = new File("/workspaces/json-schema-validator/src/main/resources/data.json");
        JsonNode jsonNode = JsonLoader.fromFile(jsonFile);

        // Validate the JSON file against the schema
        Set<ValidationMessage> errors = schema.validate(jsonNode);

        // Print any validation errors
        for (ValidationMessage error : errors) {
            System.out.println(error.getMessage());
        }
    }
}

package com.networknt.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.SpecVersion.VersionFlag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if(error.getType().equalsIgnoreCase("required")){
                Map<String, String> requiredMap=  new HashMap<>();
                JsonNode requiredNode = schemaNode.get("custom");
                if(requiredNode.isObject()){                
                    if(requiredNode.isObject()){
                    requiredNode.fields().forEachRemaining(entry->{
                    requiredMap.put(entry.getKey(), entry.getValue().asText());
                     });
                    }

                }
             
                String input = error.getMessage()   ;
                String fieldName = null;
                Pattern pattern = Pattern.compile("\\$.\\w+");
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                fieldName = matcher.group();
             }

        System.out.println("fieldName-->"+fieldName); // prints "$.companyName"
        System.out.println( requiredMap.get(fieldName));

            }
            else {
                System.out.println(error.getMessage());

            }
        }
    }
}

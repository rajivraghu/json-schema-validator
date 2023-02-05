package com.networknt.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.SpecVersion.VersionFlag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonSchemaValidator {

    public static void main(String[] args) throws IOException {
        // Load the JSON Schema file
        File schemaFile = new File("/workspace/json-schema-validator/src/main/resources/schema.json");
        JsonNode schemaNode = JsonLoader.fromFile(schemaFile);

        boolean sendMultipleInputErrors = true;

        List<InputErrorDTO> inputErrors = new ArrayList<InputErrorDTO>();

        // Create a JSON Schema object from the schema file

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V7);

        JsonSchema schema = factory.getSchema(schemaNode);

        // Load the JSON file to be validated
        File jsonFile = new File("/workspace/json-schema-validator/src/main/resources/data.json");
        JsonNode jsonNode = JsonLoader.fromFile(jsonFile);

        // Validate the JSON file against the schema
        Set<ValidationMessage> errors = schema.validate(jsonNode);

        // Print any validation errors
        String fieldName = null;
        int count =0;
        Map<String, String> requiredMap = new HashMap<>();
        for (ValidationMessage error : errors) {
            if (error.getType().equalsIgnoreCase("required")) {
                if(count ==0)
                extracted(schemaNode, requiredMap);

                String input = error.getMessage();
                System.out.println("input"+input);
                Pattern pattern = Pattern.compile("(.*):");
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    fieldName = matcher.group(1);
                }

                 System.out.println("fieldName-->"+fieldName); // prints "$.companyName"
                 System.out.println( requiredMap.get(fieldName));

                InputErrorDTO inputError = new InputErrorDTO();
                inputError.setFieldName(fieldName);
                inputError.setErrorCode(requiredMap.get(fieldName));
                inputErrors.add(inputError);
                if (!sendMultipleInputErrors)
                    break;

            } else {

                System.out.println(error.getPath()+error.getMessage());
                InputErrorDTO inputError = new InputErrorDTO();
                inputError.setFieldName(error.getPath());
                inputError.setErrorCode(error.getMessage());
                inputErrors.add(inputError);
                if (!sendMultipleInputErrors)
                    break;

            }
            count++;
        }

        System.out.println("list.." + inputErrors);

    }

    private static void extracted(JsonNode schemaNode, Map<String, String> requiredMap) {
        JsonNode requiredNode = schemaNode.get("custom");
        if (requiredNode.isObject()) {
            requiredNode.fields().forEachRemaining(entry -> {
                requiredMap.put(entry.getKey(), entry.getValue().asText());
            });
        }
    }

}

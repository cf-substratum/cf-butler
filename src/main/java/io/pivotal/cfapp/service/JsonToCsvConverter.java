package io.pivotal.cfapp.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

import org.springframework.stereotype.Service;

@Service
public class JsonToCsvConverter {

    private final ObjectMapper mapper;

    public JsonToCsvConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String convert(JsonNode jsonTree) throws JsonProcessingException {
        Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        return csvMapper
            .writerFor(JsonNode.class)
            .with(csvSchema)
            .writeValueAsString(jsonTree);
    }

    public String convert(String json) throws JsonProcessingException, IOException {
        return convert(mapper.readTree(json));
    }
}
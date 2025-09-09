package com.gng.test.file_converter.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gng.test.file_converter.model.Data;
import org.springframework.stereotype.Component;

@Component
public class JsonSerializer implements Serializer {

    private final ObjectWriter objectWriter = new ObjectMapper().writer();

    @Override
    public String serialize(Data data) throws JsonProcessingException {
        if (data != null) {
            return objectWriter.writeValueAsString(data);
        }

        return "";
    }
}

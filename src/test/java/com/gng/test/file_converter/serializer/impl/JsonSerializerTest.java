package com.gng.test.file_converter.serializer.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gng.test.file_converter.model.Data;
import com.gng.test.file_converter.model.DataItem;
import com.gng.test.file_converter.serializer.JsonSerializer;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class JsonSerializerTest {

    private JsonSerializer jsonSerializer = new JsonSerializer();

    @Test
    void serializeShouldConvertObjectToJson() throws JsonProcessingException {
        Data data = new Data(Collections.singletonList(new DataItem(UUID.fromString("18148426-89e1-11ee-b9d1-0242ac120002"),
                "1X1D14",
                "John Smith",
                "Likes Apricots",
                "Rides A Bike",
                6.2,
                12.1)));

        String json = jsonSerializer.serialize(data);

        assertEquals("{\"dataItems\":[{\"uuid\":\"18148426-89e1-11ee-b9d1-0242ac120002\",\"id\":\"1X1D14\",\"name\":\"John Smith\",\"likes\":\"Likes Apricots\",\"transport\":\"Rides A Bike\",\"avgSpeed\":6.2,\"topSpeed\":12.1}]}", json);
    }

    @Test
    void serializeShouldHandleEmptyDataItemsList() throws JsonProcessingException {
        Data data = new Data(Collections.EMPTY_LIST);

        String json = jsonSerializer.serialize(data);

        assertEquals("{\"dataItems\":[]}", json);
    }

    @Test
    void serializeShouldHandleNullDataItemsList() throws JsonProcessingException {
        Data data = new Data();

        String json = jsonSerializer.serialize(data);

        assertEquals("{\"dataItems\":null}", json);
    }

    @Test
    void serializeShouldHandleEmptyStringForNullData() throws JsonProcessingException {
        String json = jsonSerializer.serialize(null);

        assertEquals("", json);
    }
}

package com.gng.test.file_converter.serializer;

import com.gng.test.file_converter.model.Data;

public interface Serializer {

    public String serialize(Data data) throws Exception;
}

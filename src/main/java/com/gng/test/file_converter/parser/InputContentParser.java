package com.gng.test.file_converter.parser;

import com.gng.test.file_converter.model.Data;

public interface InputContentParser {

    public Data parse(String inputContent) throws Exception;
}

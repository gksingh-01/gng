package com.gng.test.file_converter.validator.content;

import com.gng.test.file_converter.validator.ErrorCode;
import java.util.Optional;

public interface InputContentValidator {

    public Optional<ErrorCode> validate(String inputContent);
}

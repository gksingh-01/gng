package com.gng.test.file_converter.validator.content.impl;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.content.InputContentValidator;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class NoOpInputContentValidator implements InputContentValidator {

    @Override
    public Optional<ErrorCode> validate(String inputContent) {
        return Optional.empty();
    }
}

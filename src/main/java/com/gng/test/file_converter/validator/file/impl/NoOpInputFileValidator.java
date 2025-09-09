package com.gng.test.file_converter.validator.file.impl;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.file.InputFileValidator;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class NoOpInputFileValidator implements InputFileValidator {

    @Override
    public Optional<ErrorCode> validate(MultipartFile multipartFile) {
        return Optional.empty();
    }
}

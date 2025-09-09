package com.gng.test.file_converter.validator.file.impl;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.file.InputFileValidator;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TextFileValidator implements InputFileValidator {

    @Override
    public Optional<ErrorCode> validate(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return Optional.of(ErrorCode.INPUT_FILE_EMPTY);
        }

        if (multipartFile.getOriginalFilename() != null
                && !multipartFile.getOriginalFilename().toLowerCase().endsWith(".txt")) {
            return Optional.of(ErrorCode.INVALID_INPUT_FILE_FORMAT);
        }

        return Optional.empty();
    }
}

package com.gng.test.file_converter.validator.file;

import com.gng.test.file_converter.validator.ErrorCode;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface InputFileValidator {

    public Optional<ErrorCode> validate(MultipartFile multipartFile);
}

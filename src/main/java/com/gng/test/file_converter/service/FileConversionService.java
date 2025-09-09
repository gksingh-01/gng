package com.gng.test.file_converter.service;

import com.gng.test.file_converter.validator.ErrorCode;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface FileConversionService {

    public record ConversionDto(Optional<ErrorCode> errorCode, Optional<String> result) {

    }

    public ConversionDto convert(MultipartFile multipartFile);
}

package com.gng.test.file_converter.controller;

import com.gng.test.file_converter.model.ErrorResponse;
import com.gng.test.file_converter.service.FileConversionService;
import com.gng.test.file_converter.service.FileConversionService.ConversionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileConversionController {

    private final FileConversionService fileConversionService;

    public FileConversionController(@Autowired FileConversionService fileConversionService) {
        this.fileConversionService = fileConversionService;
    }

    @PostMapping("/json")
    public ResponseEntity<?> convertToJson(@RequestPart("file") MultipartFile file) {
        ConversionDto conversionDto = fileConversionService.convert(file);

        if (conversionDto.errorCode().isPresent()) {
            ErrorResponse error = new ErrorResponse();
            error.setHttpErrorCode(HttpStatus.BAD_REQUEST.value());
            error.setDescription("Bad Request: " + conversionDto.errorCode().get().toString());
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }

        if (conversionDto.result().isPresent()) {
            byte[] dataBytes = conversionDto.result().get().getBytes();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(dataBytes.length)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=OutcomeFile.json")
                    .body(new ByteArrayResource(dataBytes));
        }

        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error"));
    }
}

package com.gng.test.file_converter.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.gng.test.file_converter.model.ErrorResponse;
import com.gng.test.file_converter.service.FileConversionService;
import com.gng.test.file_converter.service.FileConversionService.ConversionDto;
import com.gng.test.file_converter.validator.ErrorCode;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileConversionControllerTest {

    @Mock
    private MultipartFile multipartFile;
    @Mock
    private FileConversionService fileConversionService;

    @InjectMocks
    private FileConversionController fileConversionController;

    @Test
    void shouldReturnDataOnValidRequest() throws IOException {
        String result = "Test";
        byte[] resultBytes = result.getBytes();
        ConversionDto conversionDto = new ConversionDto(Optional.empty(), Optional.of(result));
        when(fileConversionService.convert(multipartFile)).thenReturn(conversionDto);

        ResponseEntity<Resource> responseEntity = (ResponseEntity<Resource>)fileConversionController.convertToJson(multipartFile);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(resultBytes, responseEntity.getBody().getContentAsByteArray());
        assertEquals("application/octet-stream",
                responseEntity.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0));
        assertEquals(String.valueOf(resultBytes.length), responseEntity.getHeaders().get(HttpHeaders.CONTENT_LENGTH).get(0));
        assertEquals("attachment; filename=OutcomeFile.json",
                responseEntity.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
    }

    @Test
    void shouldReturnErrorCode400OnBadRequest() throws IOException {
        ConversionDto conversionDto = new ConversionDto(Optional.of(ErrorCode.INVALID_INPUT_FILE_FORMAT), Optional.empty());
        when(fileConversionService.convert(multipartFile)).thenReturn(conversionDto);

        ResponseEntity<ErrorResponse> responseEntity = (ResponseEntity<ErrorResponse>)fileConversionController.convertToJson(multipartFile);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getHttpErrorCode());
        assertNotNull(errorResponse.getDescription());
    }

    @Test
    void shouldReturnErrorCode500OnInvalidConversionDto() throws IOException {
        ConversionDto conversionDto = new ConversionDto(Optional.empty(), Optional.empty());
        when(fileConversionService.convert(multipartFile)).thenReturn(conversionDto);

        ResponseEntity<ErrorResponse> responseEntity = (ResponseEntity<ErrorResponse>)fileConversionController.convertToJson(multipartFile);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCode().value());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getHttpErrorCode());
        assertNotNull(errorResponse.getDescription());
    }
}

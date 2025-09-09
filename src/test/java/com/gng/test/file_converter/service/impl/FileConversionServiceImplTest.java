package com.gng.test.file_converter.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.gng.test.file_converter.model.Data;
import com.gng.test.file_converter.parser.InputContentParser;
import com.gng.test.file_converter.serializer.Serializer;
import com.gng.test.file_converter.service.FileConversionService.ConversionDto;
import com.gng.test.file_converter.validator.content.InputContentValidator;
import com.gng.test.file_converter.validator.file.InputFileValidator;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileConversionServiceImplTest {

    @Mock
    private MultipartFile multipartFile;
    @Mock
    private InputFileValidator inputFileValidator;
    @Mock
    private InputContentValidator inputContentValidator;
    @Mock
    private InputContentParser inputContentParser;
    @Mock
    private Serializer serializer;

    @InjectMocks
    private FileConversionServiceImpl fileConversionServiceImpl;

    @Test
    void convertShouldReturnTheConvertedData() throws Exception {
        when(inputFileValidator.validate(multipartFile)).thenReturn(Optional.empty());
        String inputContent = "Test";
        when(multipartFile.getBytes()).thenReturn(inputContent.getBytes());
        when(inputContentValidator.validate(inputContent)).thenReturn(Optional.empty());
        Data data = new Data();
        when(inputContentParser.parse(inputContent)).thenReturn(data);
        String converted = "converted";
        when(serializer.serialize(data)).thenReturn(converted);

        ConversionDto conversionDto = fileConversionServiceImpl.convert(multipartFile);

        assertTrue(conversionDto.errorCode().isEmpty());
        assertTrue(conversionDto.result().isPresent());
        assertEquals(converted, conversionDto.result().get());
    }

    @Test
    void convertShouldThrowRuntimeErrorIfParserThrowsError() throws Exception {
        when(inputFileValidator.validate(multipartFile)).thenReturn(Optional.empty());
        String inputContent = "Test";
        when(multipartFile.getBytes()).thenReturn(inputContent.getBytes());
        when(inputContentValidator.validate(inputContent)).thenReturn(Optional.empty());
        when(inputContentParser.parse(inputContent)).thenThrow(new Exception());

        assertThrows(RuntimeException.class, () -> fileConversionServiceImpl.convert(multipartFile));
    }

    @Test
    void convertShouldThrowRuntimeErrorIfSerializerThrowsError() throws Exception {
        when(inputFileValidator.validate(multipartFile)).thenReturn(Optional.empty());
        String inputContent = "Test";
        when(multipartFile.getBytes()).thenReturn(inputContent.getBytes());
        when(inputContentValidator.validate(inputContent)).thenReturn(Optional.empty());
        Data data = new Data();
        when(inputContentParser.parse(inputContent)).thenReturn(data);
        when(serializer.serialize(data)).thenThrow(new Exception());

        assertThrows(RuntimeException.class, () -> fileConversionServiceImpl.convert(multipartFile));
    }
}

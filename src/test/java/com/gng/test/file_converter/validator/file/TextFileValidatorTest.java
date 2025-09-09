package com.gng.test.file_converter.validator.file;

import static com.gng.test.file_converter.validator.ErrorCode.INPUT_FILE_EMPTY;
import static com.gng.test.file_converter.validator.ErrorCode.INVALID_INPUT_FILE_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.file.impl.TextFileValidator;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class TextFileValidatorTest {

    @Mock
    private MultipartFile multipartFile;

    private TextFileValidator textFileValidator = new TextFileValidator();

    @ParameterizedTest
    @ValueSource(strings = {"txt", "TXT"})
    void validateShouldPassValidFile(String fileExtension) {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("input." + fileExtension);

        Optional<ErrorCode> result = textFileValidator.validate(multipartFile);

        assertTrue(result.isEmpty());
    }

    @Test
    void validateShouldFailEmptyFiles() {
        Optional<ErrorCode> result = textFileValidator.validate(null);

        assertEquals(INPUT_FILE_EMPTY, result.get());

        when(multipartFile.isEmpty()).thenReturn(true);

        result = textFileValidator.validate(multipartFile);

        assertEquals(INPUT_FILE_EMPTY, result.get());
    }

    @Test
    void validateShouldFailFilesInDifferentFormat() {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("input.xml");

        Optional<ErrorCode> result = textFileValidator.validate(multipartFile);

        assertEquals(INVALID_INPUT_FILE_FORMAT, result.get());
    }

    @Test
    void validateShouldPassFilesWithoutName() {
        when(multipartFile.isEmpty()).thenReturn(false);

        Optional<ErrorCode> result = textFileValidator.validate(multipartFile);

        assertTrue(result.isEmpty());
    }
}

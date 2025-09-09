package com.gng.test.file_converter.validator.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.file.impl.NoOpInputFileValidator;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class NoOpInputFileValidatorTest {

    @Mock
    private MultipartFile multipartFile;

    private NoOpInputFileValidator noOpInputFileValidator = new NoOpInputFileValidator();

    @Test
    void validateShouldPassNullAndEmptyFiles() {
        Optional<ErrorCode> result = noOpInputFileValidator.validate(null);

        assertTrue(result.isEmpty());

        result = noOpInputFileValidator.validate(multipartFile);

        assertTrue(result.isEmpty());
    }
}

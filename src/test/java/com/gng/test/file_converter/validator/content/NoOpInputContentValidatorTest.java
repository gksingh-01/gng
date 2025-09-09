package com.gng.test.file_converter.validator.content;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.content.impl.NoOpInputContentValidator;
import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class NoOpInputContentValidatorTest {

    private NoOpInputContentValidator noOpInputContentValidator = new NoOpInputContentValidator();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"test"})
    void validateShouldPassForAnyContent(String inputContent) {
        Optional<ErrorCode> result = noOpInputContentValidator.validate(inputContent);

        assertTrue(result.isEmpty());
    }
}

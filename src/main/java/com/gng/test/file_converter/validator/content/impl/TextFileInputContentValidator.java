package com.gng.test.file_converter.validator.content.impl;

import static com.gng.test.file_converter.validator.ErrorCode.INPUT_CONTENT_EMPTY;
import static com.gng.test.file_converter.validator.ErrorCode.INVALID_INPUT_CONTENT;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.content.InputContentValidator;
import io.micrometer.common.util.StringUtils;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class TextFileInputContentValidator implements InputContentValidator {

    private final String inputTextFilePatternString = "Name:\\s*[a-zA-Z0-9]+.[a-zA-Z0-9]+"
            + "(\\r?\\n|\\r)Type:\\s*TXT"
            + "(\\r?\\n|\\r)Content:\\s*"
            + "(((\\r?\\n|\\r)"
            + "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
            + "\\|[a-zA-Z0-9]+"
            + "\\|[a-zA-Z0-9 ]+"
            + "\\|[a-zA-Z0-9 ]+"
            + "\\|[a-zA-Z0-9 ]+"
            + "\\|\\d+\\.\\d+"
            + "\\|\\d+\\.\\d+"
            + ")*)";
    private final Pattern inputTextFilePattern = Pattern.compile(inputTextFilePatternString);

    @Override
    public Optional<ErrorCode> validate(String inputContent) {
        if (StringUtils.isBlank(inputContent)) {
            return Optional.of(INPUT_CONTENT_EMPTY);
        }

        if (!inputTextFilePattern.matcher(inputContent).matches()) {
            return Optional.of(INVALID_INPUT_CONTENT);
        }

        return Optional.empty();
    }
}

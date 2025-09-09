package com.gng.test.file_converter.validator.content;

import static com.gng.test.file_converter.validator.ErrorCode.INPUT_CONTENT_EMPTY;
import static com.gng.test.file_converter.validator.ErrorCode.INVALID_INPUT_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.content.impl.TextFileInputContentValidator;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class TextFileInputContentValidatorTest {

    private TextFileInputContentValidator textFileInputContentValidator = new TextFileInputContentValidator();

    static Stream<Arguments> validInputContentProvider() {
        return Stream.of(
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent:"),
                Arguments.of("Name:EntryFile.txt\nType:TXT\nContent:\n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name:   EntryFile.txt\nType:   TXT\nContent:   \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1")
        );
    }

    @ParameterizedTest
    @MethodSource("validInputContentProvider")
    void validateShouldPassOnValidContent(String inputContent) {
        Optional<ErrorCode> result = textFileInputContentValidator.validate(inputContent);

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void validateShouldFailOnNullOrEmptyContent(String inputContent) {
        Optional<ErrorCode> result = textFileInputContentValidator.validate(inputContent);

        assertEquals(INPUT_CONTENT_EMPTY, result.get());
    }

    static Stream<Arguments> invalidInputContentProvider() {
        return Stream.of(
                Arguments.of("Test"),
                Arguments.of("Type: TXT\nContent: "),
                Arguments.of("Name: EntryFile.txt\nContent: "),
                Arguments.of("Type: TXT\nContent: \nTest"),
                Arguments.of("Type: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120G02|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002||John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002||John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14||Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith||Rides A Bike|6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots||6.2|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike||12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|62|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|A|12.1"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12"),
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|A")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInputContentProvider")
    void validateShouldFailOnInvalidContent(String inputContent) {
        Optional<ErrorCode> result = textFileInputContentValidator.validate(inputContent);

        assertEquals(INVALID_INPUT_CONTENT, result.get());
    }
}

package com.gng.test.file_converter.parser.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gng.test.file_converter.model.Data;
import com.gng.test.file_converter.model.DataItem;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class TextFileInputContentParserTest {

    private TextFileInputContentParser textFileInputContentParser = new TextFileInputContentParser();

    static Stream<Arguments> validFileContentProvider() {
        return Stream.of(
                Arguments.of("Name: EntryFile.txt\nType: TXT\nContent: \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name:EntryFile.txt\nType:TXT\nContent:\n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1"),
                Arguments.of("Name:   EntryFile.txt\nType:   TXT\nContent:   \n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1")
        );
    }

    @Test
    void parseShouldReturnDataForContent() throws IOException {
        String inputContent = "\n18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1";

        Data data = textFileInputContentParser.parse(inputContent);

        assertEquals(1, data.getDataItems().size());
        DataItem dataItem = data.getDataItems().get(0);
        assertEquals(UUID.fromString("18148426-89e1-11ee-b9d1-0242ac120002"), dataItem.getUuid());
        assertEquals("1X1D14", dataItem.getId());
        assertEquals("John Smith", dataItem.getName());
        assertEquals("Likes Apricots", dataItem.getLikes());
        assertEquals("Rides A Bike", dataItem.getTransport());
        assertEquals(6.2d, dataItem.getAvgSpeed());
        assertEquals(12.1d, dataItem.getTopSpeed());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void parseShouldReturnEmptyDataForEmptyContent(String inputContent) throws IOException {
        Data data = textFileInputContentParser.parse(inputContent);

        assertNotNull(data);
        assertNotNull(data.getDataItems());
        assertTrue(data.getDataItems().isEmpty());
    }
}

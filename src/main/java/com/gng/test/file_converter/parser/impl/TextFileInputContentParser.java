package com.gng.test.file_converter.parser.impl;

import com.gng.test.file_converter.model.Data;
import com.gng.test.file_converter.model.DataItem;
import com.gng.test.file_converter.parser.InputContentParser;
import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;

@Component
public class TextFileInputContentParser implements InputContentParser {

    private final String textFileContentPatternString = "(((\\r?\\n|\\r)"
                    + "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
                    + "\\|[a-zA-Z0-9]+"
                    + "\\|[a-zA-Z0-9 ]+"
                    + "\\|[a-zA-Z0-9 ]+"
                    + "\\|[a-zA-Z0-9 ]+"
                    + "\\|\\d+\\.\\d+"
                    + "\\|\\d+\\.\\d+"
                    + ")+)"
            ;
    private final Pattern textFileContentPattern = Pattern.compile(textFileContentPatternString);
    private final String[] fileDataCsvHeaders = {"UUID", "ID", "Name", "Likes", "Transport", "Avg Speed", "Top Speed"};

    @Override
    public Data parse(String inputContent) throws IOException {
        if (StringUtils.isNotBlank(inputContent)) {
            Matcher matcher = textFileContentPattern.matcher(inputContent);
            if (matcher.find()) {
                CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                        .setDelimiter('|')
                        .setHeader(fileDataCsvHeaders)
                        .build();
                CSVParser csvParser = csvFormat.parse(new StringReader(matcher.group(0).replaceFirst("\\r?\\n|\\r", "")));
                return new Data(csvParser.stream()
                        .map(csvRecord -> new DataItem(UUID.fromString(csvRecord.get("UUID")),
                                csvRecord.get("ID"),
                                csvRecord.get("Name"),
                                csvRecord.get("Likes"),
                                csvRecord.get("Transport"),
                                Double.parseDouble(csvRecord.get("Avg Speed")),
                                Double.parseDouble(csvRecord.get("Top Speed"))))
                        .collect(Collectors.toList()));
            }
        }

        return new Data(Collections.EMPTY_LIST);
    }
}

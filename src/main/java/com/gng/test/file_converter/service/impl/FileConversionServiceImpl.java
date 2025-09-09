package com.gng.test.file_converter.service.impl;

import com.gng.test.file_converter.model.Data;
import com.gng.test.file_converter.parser.InputContentParser;
import com.gng.test.file_converter.serializer.Serializer;
import com.gng.test.file_converter.service.FileConversionService;
import com.gng.test.file_converter.validator.ErrorCode;
import com.gng.test.file_converter.validator.content.InputContentValidator;
import com.gng.test.file_converter.validator.file.InputFileValidator;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileConversionServiceImpl implements FileConversionService {

    private final InputFileValidator inputFileValidator;
    private final InputContentValidator inputContentValidator;
    private final InputContentParser inputContentParser;
    private final Serializer serializer;

    public FileConversionServiceImpl(InputFileValidator inputFileValidator,
            InputContentValidator inputContentValidator, InputContentParser inputContentParser,
            Serializer serializer) {
        this.inputFileValidator = inputFileValidator;
        this.inputContentValidator = inputContentValidator;
        this.inputContentParser = inputContentParser;
        this.serializer = serializer;
    }

    @Override
    public ConversionDto convert(MultipartFile multipartFile) {
        try {
            Optional<ErrorCode> result = inputFileValidator.validate(multipartFile);
            if (result.isPresent()) {
                return new ConversionDto(result, Optional.empty());
            }

            String inputContent = new String(multipartFile.getBytes());

            result = inputContentValidator.validate(inputContent);
            if (result.isPresent()) {
                return new ConversionDto(result, Optional.empty());
            }

            Data data = inputContentParser.parse(inputContent);

            String conversion = serializer.serialize(data);

            return new ConversionDto(Optional.empty(), Optional.of(conversion));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

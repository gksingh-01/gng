package com.gng.test.file_converter.config;

import com.gng.test.file_converter.parser.InputContentParser;
import com.gng.test.file_converter.serializer.Serializer;
import com.gng.test.file_converter.service.FileConversionService;
import com.gng.test.file_converter.service.impl.FileConversionServiceImpl;
import com.gng.test.file_converter.validator.content.InputContentValidator;
import com.gng.test.file_converter.validator.content.impl.NoOpInputContentValidator;
import com.gng.test.file_converter.validator.content.impl.TextFileInputContentValidator;
import com.gng.test.file_converter.validator.file.InputFileValidator;
import com.gng.test.file_converter.validator.file.impl.NoOpInputFileValidator;
import com.gng.test.file_converter.validator.file.impl.TextFileValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public InputFileValidator inputFileValidator(@Value("${validate.input}") Boolean validateInput) {
        if (validateInput != null && validateInput) {
            return new TextFileValidator();
        }
        return new NoOpInputFileValidator();
    }

    @Bean
    public InputContentValidator inputContentValidator(@Value("${validate.input}") Boolean validateInput) {
        if (validateInput != null && validateInput) {
            return new TextFileInputContentValidator();
        }
        return new NoOpInputContentValidator();
    }

    @Bean
    public FileConversionService fileConversionService(InputFileValidator inputFileValidator,
            InputContentValidator inputContentValidator,
            InputContentParser inputContentParser,
            Serializer serializer) {
        return new FileConversionServiceImpl(inputFileValidator,
                inputContentValidator,
                inputContentParser,
                serializer);
    }
}

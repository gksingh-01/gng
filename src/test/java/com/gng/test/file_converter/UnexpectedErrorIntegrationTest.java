package com.gng.test.file_converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gng.test.file_converter.interceptor.IpInterceptor;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest()
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UnexpectedErrorIntegrationTest {

    @MockitoBean
    private IpInterceptor ipInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSuccess() throws Exception {
        when(ipInterceptor.preHandle(any(), any(), any())).thenThrow(new RuntimeException());

        byte[] content = IOUtils.resourceToByteArray("/input.txt");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "input.txt",
                MediaType.TEXT_PLAIN_VALUE, content);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/json")
                        .file(mockMultipartFile))
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"status\":\"Error\",\"httpErrorCode\":500,\"description\":\"Internal Server Error\"}",
                result);
    }
}

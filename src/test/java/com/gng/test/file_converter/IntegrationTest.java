package com.gng.test.file_converter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.test.file_converter.model.IpApiDataModel;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest(properties = "ip.api.url=http://localhost:8081")
@AutoConfigureMockMvc
@EnableWireMock({@ConfigureWireMock(name = "file-conversion", port = 8081)})
@ExtendWith(MockitoExtension.class)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSuccess() throws Exception {
        stubFor(get(urlEqualTo("/json/127.0.0.1?fields=status,country,countryCode,isp"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(new ObjectMapper().writeValueAsString(
                                new IpApiDataModel("success", "Great Britain", "GB", "BT")))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        byte[] content = IOUtils.resourceToByteArray("/input.txt");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "input.txt",
                MediaType.TEXT_PLAIN_VALUE, content);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/json")
                        .file(mockMultipartFile))
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"dataItems\":[{\"uuid\":\"18148426-89e1-11ee-b9d1-0242ac120002\",\"id\":\"1X1D14\",\"name\":\"John Smith\",\"likes\":\"Likes Apricots\",\"transport\":\"Rides A Bike\",\"avgSpeed\":6.2,\"topSpeed\":12.1},{\"uuid\":\"3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7\",\"id\":\"2X2D24\",\"name\":\"Mike Smith\",\"likes\":\"Likes Grape\",\"transport\":\"Drives an SUV\",\"avgSpeed\":35.0,\"topSpeed\":95.5},{\"uuid\":\"1afb6f5d-a7c2-4311-a92d-974f3180ff5e\",\"id\":\"3X3D35\",\"name\":\"Jenny Walters\",\"likes\":\"Likes Avocados\",\"transport\":\"Rides A Scooter\",\"avgSpeed\":8.5,\"topSpeed\":15.3}]}",
                result);
    }

    @Test
    void testBadRequest() throws Exception {
        stubFor(get(urlEqualTo("/json/127.0.0.1?fields=status,country,countryCode,isp"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(new ObjectMapper().writeValueAsString(
                                new IpApiDataModel("success", "Great Britain", "GB", "BT")))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        byte[] content = "Test".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "input.txt",
                MediaType.TEXT_PLAIN_VALUE, content);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/json")
                        .file(mockMultipartFile))
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"status\":\"Error\",\"httpErrorCode\":400,\"description\":\"Bad Request: INVALID_INPUT_CONTENT\"}",
                result);
    }

    @Test
    void testForbiddenCountry() throws Exception {
        stubFor(get(urlEqualTo("/json/127.0.0.1?fields=status,country,countryCode,isp"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(new ObjectMapper().writeValueAsString(
                                new IpApiDataModel("success", "Spain", "GB", "BT")))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        byte[] content = IOUtils.resourceToByteArray("/input.txt");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "input.txt",
                MediaType.TEXT_PLAIN_VALUE, content);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/json")
                        .file(mockMultipartFile))
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"status\":\"Error\",\"httpErrorCode\":403,\"description\":\"Your IP country is blacklisted.\"}",
                result);
    }

    @Test
    void testForbiddenIsp() throws Exception {
        stubFor(get(urlEqualTo("/json/127.0.0.1?fields=status,country,countryCode,isp"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(new ObjectMapper().writeValueAsString(
                                new IpApiDataModel("success", "Great Britain", "GB", "AWS")))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        byte[] content = IOUtils.resourceToByteArray("/input.txt");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "input.txt",
                MediaType.TEXT_PLAIN_VALUE, content);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/json")
                        .file(mockMultipartFile))
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"status\":\"Error\",\"httpErrorCode\":403,\"description\":\"Your IP ISP is blacklisted.\"}",
                result);
    }

    @Test
    void testUnresolvedIsp() throws Exception {
        stubFor(get(urlEqualTo("/json/127.0.0.1?fields=status,country,countryCode,isp"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(new ObjectMapper().writeValueAsString(
                                new IpApiDataModel("fail", null, null, null)))
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        byte[] content = IOUtils.resourceToByteArray("/input.txt");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "input.txt",
                MediaType.TEXT_PLAIN_VALUE, content);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/json")
                        .file(mockMultipartFile))
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"status\":\"Error\",\"httpErrorCode\":403,\"description\":\"\"}",
                result);
    }
}

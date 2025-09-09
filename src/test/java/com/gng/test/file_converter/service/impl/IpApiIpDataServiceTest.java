package com.gng.test.file_converter.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.gng.test.file_converter.model.IpApiDataModel;
import com.gng.test.file_converter.service.IpDataService.IpDataDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class IpApiIpDataServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ResponseEntity<IpApiDataModel> responseEntity;

    @InjectMocks
    private IpApiIpDataService ipApiIpDataService;

    @Test
    void resolveShouldReturnDataIfIPAPIReturnsSuccessResponse() {
        IpApiDataModel ipApiDataModel = new IpApiDataModel("success", "Great Britain", "GB", "BT");

        when(restTemplate.getForEntity(anyString(), eq(IpApiDataModel.class))).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn(ipApiDataModel);

        IpDataDto ipDataDto = ipApiIpDataService.resolve("0.0.0.1");

        assertTrue(ipDataDto.isSuccess());
        assertEquals(ipApiDataModel.getCountry(), ipDataDto.country());
        assertEquals(ipApiDataModel.getCountryCode(), ipDataDto.countryCode());
        assertEquals(ipApiDataModel.getIsp(), ipDataDto.isp());
    }

    @Test
    void resolveShouldFlagAsFailureIfIPAPIReturnsErrorResponse() {
        when(restTemplate.getForEntity(anyString(), eq(IpApiDataModel.class))).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        IpDataDto ipDataDto = ipApiIpDataService.resolve("0.0.0.1");

        assertFalse(ipDataDto.isSuccess());
        assertNull(ipDataDto.country());
        assertNull(ipDataDto.countryCode());
        assertNull(ipDataDto.isp());
    }

    @Test
    void resolveShouldFlagAsFailureIfIPAPIReturnsFailureResponse() {
        IpApiDataModel ipApiDataModel = new IpApiDataModel("fail", "Great Britain", "GB", "BT");

        when(restTemplate.getForEntity(anyString(), eq(IpApiDataModel.class))).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn(ipApiDataModel);

        IpDataDto ipDataDto = ipApiIpDataService.resolve("0.0.0.1");

        assertFalse(ipDataDto.isSuccess());
        assertNull(ipDataDto.country());
        assertNull(ipDataDto.countryCode());
        assertNull(ipDataDto.isp());
    }

    @Test
    void resolveShouldHandleExceptions() {
        when(restTemplate.getForEntity(anyString(), eq(IpApiDataModel.class))).thenThrow(new RuntimeException());

        IpDataDto ipDataDto = ipApiIpDataService.resolve("0.0.0.1");

        assertFalse(ipDataDto.isSuccess());
        assertNull(ipDataDto.country());
        assertNull(ipDataDto.countryCode());
        assertNull(ipDataDto.isp());
    }
}

package com.gng.test.file_converter.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gng.test.file_converter.Constants;
import com.gng.test.file_converter.model.ErrorResponse;
import com.gng.test.file_converter.service.IpDataService.IpDataDto;
import com.gng.test.file_converter.service.impl.IpApiIpDataService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class IpInterceptorTest {

    @Mock
    private IpApiIpDataService ipDataService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private ServletOutputStream servletOutputStream;

    @InjectMocks
    private IpInterceptor ipInterceptor;
    private ObjectReader objectReader = new ObjectMapper().readerFor(ErrorResponse.class);

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(ipInterceptor, "countriesBlacklist", Set.of("Canada"));
        ReflectionTestUtils.setField(ipInterceptor, "ispsBlacklist", Set.of("AWS"));
    }

    @Test
    void shouldAllowCallsFromSuccessfullyResolvedWhitelistedIps() throws Exception {
        when(httpServletRequest.getRemoteAddr()).thenReturn("0.0.0.1");
        IpDataDto ipDataDto = new IpDataDto(true, "Great Britain", "GB", "BT");
        when(ipDataService.resolve(anyString())).thenReturn(ipDataDto);

        boolean result = ipInterceptor.preHandle(httpServletRequest, httpServletResponse, null);

        assertTrue(result);
        verify(httpServletRequest, times(1)).setAttribute(
                Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY, ipDataDto);
    }

    @Test
    void shouldBlockCallsFromCountryBlacklistedIps() throws Exception {
        when(httpServletRequest.getRemoteAddr()).thenReturn("0.0.0.1");
        IpDataDto ipDataDto = new IpDataDto(true, "Canada", "Ca", "BT");
        when(ipDataService.resolve(anyString())).thenReturn(ipDataDto);
        when(httpServletResponse.getOutputStream()).thenReturn(servletOutputStream);

        boolean result = ipInterceptor.preHandle(httpServletRequest, httpServletResponse, null);

        assertFalse(result);
        verify(httpServletResponse, times(1)).setStatus(HttpStatus.FORBIDDEN.value());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(servletOutputStream, times(1)).print(stringArgumentCaptor.capture());
        ErrorResponse errorResponse = objectReader.readValue(stringArgumentCaptor.getValue());
        assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getHttpErrorCode());
        assertNotNull(errorResponse.getDescription());
        verify(httpServletRequest, times(1)).setAttribute(
                Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY, ipDataDto);
    }

    @Test
    void shouldBlockCallsFromIspBlacklistedIps() throws Exception {
        when(httpServletRequest.getRemoteAddr()).thenReturn("0.0.0.1");
        IpDataDto ipDataDto = new IpDataDto(true, "Great Britain", "GB", "AWS");
        when(ipDataService.resolve(anyString())).thenReturn(ipDataDto);
        when(httpServletResponse.getOutputStream()).thenReturn(servletOutputStream);

        boolean result = ipInterceptor.preHandle(httpServletRequest, httpServletResponse, null);

        assertFalse(result);
        verify(httpServletResponse, times(1)).setStatus(HttpStatus.FORBIDDEN.value());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(servletOutputStream, times(1)).print(stringArgumentCaptor.capture());
        ErrorResponse errorResponse = objectReader.readValue(stringArgumentCaptor.getValue());
        assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getHttpErrorCode());
        assertNotNull(errorResponse.getDescription());
        verify(httpServletRequest, times(1)).setAttribute(
                Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY, ipDataDto);
    }

    @Test
    void shouldBlockCallsFromUnresolvedIps() throws Exception {
        when(httpServletRequest.getRemoteAddr()).thenReturn("0.0.0.1");
        IpDataDto ipDataDto = new IpDataDto(false, null, null, null);
        when(ipDataService.resolve(anyString())).thenReturn(ipDataDto);
        when(httpServletResponse.getOutputStream()).thenReturn(servletOutputStream);

        boolean result = ipInterceptor.preHandle(httpServletRequest, httpServletResponse, null);

        assertFalse(result);
        verify(httpServletResponse, times(1)).setStatus(HttpStatus.FORBIDDEN.value());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(servletOutputStream, times(1)).print(stringArgumentCaptor.capture());
        ErrorResponse errorResponse = objectReader.readValue(stringArgumentCaptor.getValue());
        assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getHttpErrorCode());
        assertNotNull(errorResponse.getDescription());
        verify(httpServletRequest, times(1)).setAttribute(
                Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY, ipDataDto);
    }
}

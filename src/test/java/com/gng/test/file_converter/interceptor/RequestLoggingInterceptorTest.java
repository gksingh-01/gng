package com.gng.test.file_converter.interceptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gng.test.file_converter.Constants;
import com.gng.test.file_converter.model.RequestLog;
import com.gng.test.file_converter.service.IpDataService.IpDataDto;
import com.gng.test.file_converter.service.RequestLoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RequestLoggingInterceptorTest {

    @Mock
    private RequestLoggingService requestLoggingService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Test
    void preHandleShouldSetRequestReceivedTime() throws Exception {
        requestLoggingInterceptor.preHandle(httpServletRequest, httpServletResponse, null);

        verify(httpServletRequest, times(1)).setAttribute(eq(Constants.REQUEST_START_TIMESTAMP_REQUEST_ATTRIBUTE_KEY), anyLong());
    }

    @Test
    void afterCompletionShouldLogRequest() throws Exception {
        when(httpServletRequest.getAttribute(Constants.REQUEST_START_TIMESTAMP_REQUEST_ATTRIBUTE_KEY)).thenReturn(System.currentTimeMillis());
        IpDataDto ipDataDto = new IpDataDto(true, "Great Britain", "GB", "BT");
        when(httpServletRequest.getAttribute(Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY)).thenReturn(ipDataDto);
        when(httpServletRequest.getRemoteAddr()).thenReturn("0.0.0.1");
        when(httpServletRequest.getServletPath()).thenReturn("/json");
        when(httpServletResponse.getStatus()).thenReturn(200);

        requestLoggingInterceptor.afterCompletion(httpServletRequest, httpServletResponse, null, null);

        ArgumentCaptor<RequestLog> requestLogArgumentCaptor = ArgumentCaptor.forClass(RequestLog.class);
        verify(requestLoggingService, times(1)).log(requestLogArgumentCaptor.capture());
        RequestLog requestLog = requestLogArgumentCaptor.getValue();
        assertNotNull(requestLog.getId());
        assertEquals("/json", requestLog.getPath());
        assertTrue(requestLog.getTimestamp() > 0);
        assertEquals(200, requestLog.getResponseCode());
        assertEquals("0.0.0.1", requestLog.getIpAddress());
        assertEquals("GB", requestLog.getCountryCode());
        assertEquals("BT", requestLog.getIsp());
        assertTrue(requestLog.getTimeTakenMillis() >= 0);
    }

    @Test
    void afterCompletionShouldHandleServiceErrors() throws Exception {
        when(httpServletRequest.getAttribute(Constants.REQUEST_START_TIMESTAMP_REQUEST_ATTRIBUTE_KEY)).thenReturn(System.currentTimeMillis());
        IpDataDto ipDataDto = new IpDataDto(true, "Great Britain", "GB", "BT");
        when(httpServletRequest.getAttribute(Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY)).thenReturn(ipDataDto);
        when(httpServletRequest.getRemoteAddr()).thenReturn("0.0.0.1");
        when(httpServletRequest.getServletPath()).thenReturn("/json");
        when(httpServletResponse.getStatus()).thenReturn(200);
        doThrow(new RuntimeException()).when(requestLoggingService).log(any());

        assertDoesNotThrow(() -> requestLoggingInterceptor.afterCompletion(httpServletRequest, httpServletResponse, null, null));
    }
}

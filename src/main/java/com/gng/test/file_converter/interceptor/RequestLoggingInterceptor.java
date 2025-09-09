package com.gng.test.file_converter.interceptor;

import com.gng.test.file_converter.Constants;
import com.gng.test.file_converter.model.RequestLog;
import com.gng.test.file_converter.service.IpDataService.IpDataDto;
import com.gng.test.file_converter.service.RequestLoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    private final RequestLoggingService requestLoggingService;

    public RequestLoggingInterceptor(@Autowired RequestLoggingService requestLoggingService) {
        this.requestLoggingService = requestLoggingService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(Constants.REQUEST_START_TIMESTAMP_REQUEST_ATTRIBUTE_KEY, System.currentTimeMillis());
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long requestTime = (Long) request.getAttribute(Constants.REQUEST_START_TIMESTAMP_REQUEST_ATTRIBUTE_KEY);
        IpDataDto ipDataDto = (IpDataDto) request.getAttribute(Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY);

        Long timeTaken = null;
        if (requestTime != null) {
            timeTaken = System.currentTimeMillis() - requestTime;
        }

        try {
            requestLoggingService.log(new RequestLog(UUID.randomUUID(),
                    request.getServletPath(),
                    requestTime,
                    response.getStatus(),
                    request.getRemoteAddr(),
                    (ipDataDto != null ? ipDataDto.countryCode(): null),
                    (ipDataDto != null ? ipDataDto.isp() : null),
                    timeTaken));
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}

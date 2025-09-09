package com.gng.test.file_converter.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gng.test.file_converter.Constants;
import com.gng.test.file_converter.model.ErrorResponse;
import com.gng.test.file_converter.service.IpDataService.IpDataDto;
import com.gng.test.file_converter.service.impl.IpApiIpDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IpInterceptor implements HandlerInterceptor {

    private final ObjectWriter objectWriter = new ObjectMapper().writer();
    private final IpApiIpDataService ipDataService;

    @Value("#{'${countries.blacklist}'.split(',')}")
    private Set<String> countriesBlacklist;
    @Value("#{'${isps.blacklist}'.split(',')}")
    private Set<String> ispsBlacklist;

    public IpInterceptor(@Autowired IpApiIpDataService ipDataService) {
        this.ipDataService = ipDataService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();

        IpDataDto ipDataDto = ipDataService.resolve(ipAddress);
        request.setAttribute(Constants.IP_DATA_DTO_REQUEST_ATTRIBUTE_KEY, ipDataDto);

        if (!ipDataDto.isSuccess()
                || countriesBlacklist.contains(ipDataDto.country())
                || ispsBlacklist.contains(ipDataDto.isp())) {
            String description;
            if (!ipDataDto.isSuccess()) {
                description = "";
            } else if (countriesBlacklist.contains(ipDataDto.country())) {
                description = "Your IP country is blacklisted.";
            } else {
                description = "Your IP ISP is blacklisted.";
            }

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), description);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            try {
                response.getOutputStream().print(objectWriter.writeValueAsString(errorResponse));
            } catch (Exception e) {

            }

            return false;
        }

        return true;
    }
}

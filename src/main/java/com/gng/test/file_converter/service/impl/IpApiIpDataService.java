package com.gng.test.file_converter.service.impl;

import com.gng.test.file_converter.model.IpApiDataModel;
import com.gng.test.file_converter.service.IpDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IpApiIpDataService implements IpDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpApiIpDataService.class);

    private final RestTemplate restTemplate;

    @Value("${ip.api.url}")
    private String ipApiUrl;

    public IpApiIpDataService(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public IpDataDto resolve(String ipAddress) {
        try {
            ResponseEntity<IpApiDataModel> responseEntity
                    = restTemplate.getForEntity(ipApiUrl + "/json/" + ipAddress + "?fields=status,country,countryCode,isp",
                    IpApiDataModel.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                IpApiDataModel ipApiDataModel = responseEntity.getBody();
                if ("success".equals(ipApiDataModel.getStatus())) {
                    return new IpDataDto(true,
                            ipApiDataModel.getCountry(),
                            ipApiDataModel.getCountryCode(),
                            ipApiDataModel.getIsp());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }

        return new IpDataDto(false, null, null, null);
    }
}

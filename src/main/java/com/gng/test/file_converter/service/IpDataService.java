package com.gng.test.file_converter.service;

public interface IpDataService {

    record IpDataDto(boolean isSuccess,
                            String country,
                            String countryCode,
                            String isp) {

    }

    public IpDataDto resolve(String ipAddress);
}

package com.gng.test.file_converter.model;

public class IpApiDataModel {

    private String status;
    private String country;
    private String countryCode;
    private String isp;

    public IpApiDataModel() {

    }

    public IpApiDataModel(String status, String country, String countryCode, String isp) {
        this.status = status;
        this.country = country;
        this.countryCode = countryCode;
        this.isp = isp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }
}

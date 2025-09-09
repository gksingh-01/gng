package com.gng.test.file_converter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "request")
public class RequestLog {

    @Id
    private UUID id;
    private String path;
    private Long timestamp;
    private Integer responseCode;
    private String ipAddress;
    private String countryCode;
    private String isp;
    private Long timeTakenMillis;

    public RequestLog() {

    }

    public RequestLog(UUID id, String path, Long timestamp, Integer responseCode, String ipAddress,
            String countryCode, String isp, Long timeTakenMillis) {
        this.id = id;
        this.path = path;
        this.timestamp = timestamp;
        this.responseCode = responseCode;
        this.ipAddress = ipAddress;
        this.countryCode = countryCode;
        this.isp = isp;
        this.timeTakenMillis = timeTakenMillis;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public Long getTimeTakenMillis() {
        return timeTakenMillis;
    }

    public void setTimeTakenMillis(Long timeTakenMillis) {
        this.timeTakenMillis = timeTakenMillis;
    }
}

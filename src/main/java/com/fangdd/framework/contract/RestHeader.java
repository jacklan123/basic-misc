package com.fangdd.framework.contract;

import org.springframework.http.HttpHeaders;

import java.io.Serializable;

/**
 * @author lantian
 */
public class RestHeader implements Serializable {
    public static final String USER_ID = "User-Id";
    public static final String XF_USER_ID = "Xf-User-Id";
    public static final String TOKEN = "Token";
    public static final String DEVICE_ID = "Device-Id";
    public static final String DEVICE_TOKEN = "Device-Token";
    public static final String APP_VERSION_NAME = "App-Version-Name";
    public static final String APP_VERSION_CODE = "App-Version-Code";
    public static final String SIGN = "Sign";
    public static final String TIMESTAMP = "Timestamp";
    public static final String PLATFORM = "Platform";
    public static final String PLATFORM_OS = "Platform-OS";
    public static final String PLATFORM_VERSION = "Platform-Version";
    public static final String BUNDLE_TYPE = "Bundle-Type";
    public static final String CITY_ID = "City-Id";
    public static final String LNG = "Lng";
    public static final String LAT = "Lat";
    private Long userId;
    private Long xfUserId;
    private String token;
    private String deviceId;
    private String deviceToken;
    private String appVersionCode;
    private String appVersionName;
    private String platform;
    private String platformOS;
    private String platformVersion;
    private String sign;
    private Long timestamp;
    private Long cityId;
    private Double lng;
    private Double lat;
    private Integer bundleType;

    public RestHeader() {
    }

    public RestHeader initialize(HttpHeaders headers) {
        if (headers.containsKey("User-Id")) {
            this.userId = Long.parseLong(headers.getFirst("User-Id"));
        }

        if (headers.containsKey("Xf-User-Id")) {
            this.xfUserId = Long.parseLong(headers.getFirst("Xf-User-Id"));
        }

        if (headers.containsKey("Token")) {
            this.token = headers.getFirst("Token");
        }

        if (headers.containsKey("Device-Id")) {
            this.deviceId = headers.getFirst("Device-Id");
        }

        if (headers.containsKey("Device-Token")) {
            this.deviceToken = headers.getFirst("Device-Token");
        }

        if (headers.containsKey("App-Version-Name")) {
            this.appVersionName = headers.getFirst("App-Version-Name");
        }

        if (headers.containsKey("App-Version-Code")) {
            this.appVersionCode = headers.getFirst("App-Version-Code");
        }

        if (headers.containsKey("Sign")) {
            this.sign = headers.getFirst("Sign");
        }

        if (headers.containsKey("Timestamp")) {
            this.timestamp = Long.parseLong(headers.getFirst("Timestamp"));
        }

        if (headers.containsKey("Platform")) {
            this.platform = headers.getFirst("Platform");
        }

        if (headers.containsKey("Platform-OS")) {
            this.platformOS = headers.getFirst("Platform-OS");
        }

        if (headers.containsKey("Platform-Version")) {
            this.platformVersion = headers.getFirst("Platform-Version");
        }

        if (headers.containsKey("Bundle-Type")) {
            this.bundleType = Integer.parseInt(headers.getFirst("Bundle-Type"));
        }

        if (headers.containsKey("City-Id")) {
            this.cityId = Long.parseLong(headers.getFirst("City-Id"));
        }

        if (headers.containsKey("Lat")) {
            this.lat = Double.parseDouble(headers.getFirst("Lat"));
        }

        if (headers.containsKey("Lng")) {
            this.lng = Double.parseDouble(headers.getFirst("Lng"));
        }

        return this;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getXfUserId() {
        return this.xfUserId;
    }

    public void setXfUserId(Long xfUserId) {
        this.xfUserId = xfUserId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAppVersionCode() {
        return this.appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformOS() {
        return this.platformOS;
    }

    public void setPlatformOS(String platformOS) {
        this.platformOS = platformOS;
    }

    public String getPlatformVersion() {
        return this.platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getCityId() {
        return this.cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Double getLng() {
        return this.lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getBundleType() {
        return this.bundleType;
    }

    public void setBundleType(Integer bundleType) {
        this.bundleType = bundleType;
    }
}

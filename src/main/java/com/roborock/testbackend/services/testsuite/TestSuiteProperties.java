package com.roborock.testbackend.services.testsuite;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("testsuite")
public class TestSuiteProperties {
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

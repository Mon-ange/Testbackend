package com.roborock.testbackend.services.testsuite;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service")
public class TestSuiteProperties {
    private String location = "reports";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

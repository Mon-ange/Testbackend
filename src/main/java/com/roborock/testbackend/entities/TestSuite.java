package com.roborock.testbackend.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TestSuite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String productVersion;
    private String client;
    private String mobilePlatform;
    private String robotSn;
    private String robotMacAddress;
    private String robotName;
    private int passedCases;
    private int failedCases;
    private String reportFile;
    private String logCheckResultFile;

    public TestSuite() {}
    public TestSuite(String productName, String productVersion, String client, String mobilePlatform, String robotSn,
                     String robotMacAddress, String robotName) {
        this.productName = productName;
        this.productVersion = productVersion;
        this.client = client;
        this.mobilePlatform = mobilePlatform;
        this.robotSn = robotSn;
        this.robotMacAddress = robotMacAddress;
        this.robotName = robotName;
    }

    @Override
    public String toString() {
        return "id = " + id + "productName: " + productName + " productVersion: " + productVersion + " client: " + client
                + "mobilePlatform: " + mobilePlatform + " robotSn: " + robotSn + " robotMacAddress: " + robotMacAddress
                + " robotName: " + robotName;
    }

    public Long getId() {
        return id;
    }

    public void setReportFile(String reportFileName) {
        this.reportFile = reportFileName;
    }

    public void setLogCheckResultFile(String logCheckResultFileName) {
        this.logCheckResultFile = logCheckResultFileName;
    }
}
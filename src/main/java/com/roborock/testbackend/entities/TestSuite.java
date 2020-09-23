package com.roborock.testbackend.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private int totalCases;
    private int passedCases;
    private int failedCases;
    private int skippedCases;
    private String cleanRecord;
    private String directory;
    private String reportFile;
    private String logCheckResultFile;

    //@Column(columnDefinition = "ENUM('测试中', '已完成')")
    //@Enumerated(EnumType.STRING)
    private STATUS status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

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
                + " robotName: " + robotName + " status: " + status + " startTime: " + startTime + " endTime: " + endTime
                + " directory: " + directory;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public String getClient() {
        return client;
    }

    public String getMobilePlatform() {
        return mobilePlatform;
    }

    public String getRobotSn() {
        return robotSn;
    }

    public String getRobotMacAddress() {
        return robotMacAddress;
    }

    public String getRobotName() {
        return robotName;
    }

    public int getPassedCases() {
        return passedCases;
    }

    public int getFailedCases() {
        return failedCases;
    }

    public String getCleanRecord() { return cleanRecord; }

    public String getReportFile() {
        return reportFile;
    }

    public String getLogCheckResultFile() {
        return logCheckResultFile;
    }

    public void setReportFile(String reportFileName) {
        this.reportFile = reportFileName;
    }

    public void setLogCheckResultFile(String logCheckResultFileName) {
        this.logCheckResultFile = logCheckResultFileName;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return this.directory;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public STATUS getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setCleanRecord(String cleanRecord) {
        this.cleanRecord = cleanRecord;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public void setPassedCases(int passedCases) {
        this.passedCases = passedCases;
    }

    public void setFailedCases(int failedCases) {
        this.failedCases = failedCases;
    }

    public int getSkippedCases() {
        return skippedCases;
    }

    public void setSkippedCases(int skippedCases) {
        this.skippedCases = skippedCases;
    }

}

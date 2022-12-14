package com.roborock.testbackend.services.testsuite;

import com.roborock.testbackend.entities.TestSuite;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TestSuiteService {
    long create(TestSuite testSuite) throws IOException;
    void update(long suiteId, int totalCases, int passedCases, int failedCases, int skippedCases, MultipartFile zipFile) throws IOException;
    List<TestSuite> browse();
}

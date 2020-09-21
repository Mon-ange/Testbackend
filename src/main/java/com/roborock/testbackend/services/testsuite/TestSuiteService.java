package com.roborock.testbackend.services.testsuite;

import com.roborock.testbackend.entities.TestSuite;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TestSuiteService {
    long create(TestSuite testSuite) throws IOException;
    void update(long testSuiteId, MultipartFile zipFile) throws IOException;
    List<TestSuite> browse();
}

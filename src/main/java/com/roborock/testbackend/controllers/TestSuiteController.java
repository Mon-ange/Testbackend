package com.roborock.testbackend.controllers;

import com.roborock.testbackend.entities.TestSuite;
import com.roborock.testbackend.repositories.TestSuiteRepository;
import com.roborock.testbackend.services.testsuite.TestSuiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path="/suite")
public class TestSuiteController {
    private final Logger logger = LoggerFactory.getLogger(TestSuiteController.class);

    @Autowired
    private TestSuiteService testSuiteService;

    @PostMapping("/new")
    public ResponseEntity<Long> newTestSuite(@RequestParam String productName, @RequestParam String productVersion,
                                             @RequestParam String client, @RequestParam String mobilePlatform,
                                             @RequestParam String robotSn, @RequestParam String robotMacAddress,
                                             @RequestParam String robotName) throws IOException {
        TestSuite testSuite = new TestSuite(productName, productVersion, client, mobilePlatform, robotSn, robotMacAddress, robotName);
        long testSuiteId = testSuiteService.create(testSuite);
        return new ResponseEntity<>(testSuiteId, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateTestSuite(@RequestParam("testSuiteId") String testSuiteId,
                                                  @RequestParam("total_cases") String totalCases,
                                                  @RequestParam("passed_cases") String passedCases,
                                                  @RequestParam("failed_cases") String failedCases,
                                                  @RequestParam("skipped_cases") String skippedCases,
                                                  @RequestParam("zipFile") MultipartFile zipFile) throws IOException {
        testSuiteService.update(Long.parseLong(testSuiteId),
                Integer.parseInt(totalCases), Integer.parseInt(passedCases), Integer.parseInt(failedCases), Integer.parseInt(skippedCases),
                zipFile);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/browse")
    public List<TestSuite> browse() {
        return testSuiteService.browse();
    }
}

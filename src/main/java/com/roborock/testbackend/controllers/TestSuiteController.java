package com.roborock.testbackend.controllers;

import com.roborock.testbackend.entities.TestSuite;
import com.roborock.testbackend.services.repository.TestSuiteRepository;
import com.roborock.testbackend.services.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(path="/suite")
public class TestSuiteController {
    @Autowired
    private TestSuiteRepository testSuiteRepository;

    @Autowired
    private StorageService storageService;

    private final Logger logger = LoggerFactory.getLogger(TestSuiteController.class);

    @PostMapping("/new")
    public ResponseEntity<Long> newTestSuite(@RequestParam String productName, @RequestParam String productVersion,
                                             @RequestParam String client, @RequestParam String mobilePlatform,
                                             @RequestParam String robotSn, @RequestParam String robotMacAddress,
                                             @RequestParam String robotName) {
        TestSuite testSuite = new TestSuite(productName, productVersion, client, mobilePlatform, robotSn, robotMacAddress, robotName);
        TestSuite newRecord = testSuiteRepository.save(testSuite);
        return new ResponseEntity<>(newRecord.getId(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateTestSuite(@RequestParam("testSuiteId") String testSuiteId, @RequestParam("file") MultipartFile file) {
        storageService.store(file);
        String fileName = file.getOriginalFilename();
        System.out.println("testSuiteId = " + testSuiteId);
        System.out.println("fileName = " + fileName);
        Optional<TestSuite> testSuiteOptional = testSuiteRepository.findById(Long.parseLong(testSuiteId));
        TestSuite testSuite = testSuiteOptional.get();
        System.out.println("testSuite = " + testSuite);
        testSuite.setReportFile(fileName);
        testSuiteRepository.save(testSuite);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}

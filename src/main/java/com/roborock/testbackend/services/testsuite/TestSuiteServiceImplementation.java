package com.roborock.testbackend.services.testsuite;

import com.roborock.testbackend.entities.STATUS;
import com.roborock.testbackend.entities.TestSuite;
import com.roborock.testbackend.repositories.TestSuiteRepository;
import com.roborock.testbackend.services.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TestSuiteServiceImplementation implements TestSuiteService {
    private final Path rootLocation;
    private final String location; //upload-dir

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    public TestSuiteServiceImplementation(TestSuiteProperties properties) {
        this.location = properties.getLocation();
        this.rootLocation = Paths.get(this.location);
    }

    @Override
    public long create(TestSuite testSuite) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        testSuite.setStartTime(now);
        testSuite.setStatus(STATUS.inprogress);
        String testSuiteDir = "ota-bat-" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss").format(now);
        Path testSuitePath = this.rootLocation.resolve(testSuiteDir);
        Files.createDirectories(testSuitePath);
        testSuite.setDirectory(this.location + File.separator + testSuiteDir);
        System.out.println("to be saved testSuite = " + testSuite);
        TestSuite newRecord = testSuiteRepository.save(testSuite);
        System.out.println("newRecord = " + newRecord);
        return newRecord.getId();
    }

    @Override
    public void update(long testSuiteId, MultipartFile reportFile, MultipartFile logResultFile) throws IOException {
        Optional<TestSuite> testSuiteOptional = testSuiteRepository.findById(testSuiteId);
        TestSuite testSuite = testSuiteOptional.get();
        Path suiteDir = Paths.get(testSuite.getDirectory());
        storageService.store(reportFile, suiteDir);
        storageService.store(logResultFile, suiteDir);

        String reportFileName = reportFile.getOriginalFilename();
        String logResultFileName = logResultFile.getOriginalFilename();
        LocalDateTime now = LocalDateTime.now();
        testSuite.setEndTime(now);
        testSuite.setStatus(STATUS.complete);
        testSuite.setReportFile(reportFileName);
        testSuite.setLogCheckResultFile(logResultFileName);
        System.out.println("to be saved testSuite = " + testSuite);
        testSuiteRepository.save(testSuite);
    }

    @Override
    public List<TestSuite> browse() {
        List<TestSuite> testSuites = testSuiteRepository.findAllByOrderByIdDesc();
        for (TestSuite testSuite: testSuites) {
            String dir = testSuite.getDirectory();
            testSuite.setReportFile(dir + File.separator + testSuite.getReportFile());
            testSuite.setLogCheckResultFile(dir + File.separator + testSuite.getLogCheckResultFile());
        }
        return testSuites;
    }
}

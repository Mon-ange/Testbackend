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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
        System.out.println("to be saved testSuite = " + testSuite);
        TestSuite newRecord = testSuiteRepository.save(testSuite);
        System.out.println("newRecord = " + newRecord);
        return newRecord.getId();
    }

    @Override
    public void update(long testSuiteId, int totalCases, int passedCases, int failedCases, int skippedCases, MultipartFile zipFile) throws IOException {
        Optional<TestSuite> testSuiteOptional = testSuiteRepository.findById(testSuiteId);
        TestSuite testSuite = testSuiteOptional.get();
        storageService.store(zipFile, rootLocation);
        String zipFileName = zipFile.getOriginalFilename();
        String dirName = zipFileName.substring(0, zipFileName.lastIndexOf('.'));
        Path testSuiteDir = this.rootLocation.resolve(dirName);
        testSuite.setDirectory(this.location + File.separator + dirName);
        String cleanRecordFileName = getFileFromDir(testSuiteDir, "screenshot");
        String reportFileName = getFileFromDir(testSuiteDir, "report");
        String logResultFileName = getFileFromDir(testSuiteDir, "log_check_result");
        LocalDateTime now = LocalDateTime.now();
        testSuite.setEndTime(now);
        testSuite.setStatus(STATUS.complete);
        testSuite.setTotalCases(totalCases);
        testSuite.setPassedCases(passedCases);
        testSuite.setFailedCases(failedCases);
        testSuite.setSkippedCases(skippedCases);
        testSuite.setCleanRecord(cleanRecordFileName);
        testSuite.setReportFile(reportFileName);
        testSuite.setLogCheckResultFile(logResultFileName);
        System.out.println("to be saved testSuite = " + testSuite);
        testSuiteRepository.save(testSuite);
    }

    private String getFileFromDir(Path testSuiteDir, String subDir) throws IOException {
        Path subPath = testSuiteDir.resolve(subDir);
        File subDirFile = new File(subPath.toString());
        File[] files = subDirFile.listFiles();
        return subDir + File.separator + files[0].getName();
    }

    @Override
    public List<TestSuite> browse() {
        List<TestSuite> testSuites = testSuiteRepository.findAllByOrderByIdDesc();
        for (TestSuite testSuite: testSuites) {
            String dir = testSuite.getDirectory();
            testSuite.setCleanRecord(dir + File.separator + testSuite.getCleanRecord());
            testSuite.setReportFile(dir + File.separator + testSuite.getReportFile());
            testSuite.setLogCheckResultFile(dir + File.separator + testSuite.getLogCheckResultFile());
        }
        return testSuites;
    }
}

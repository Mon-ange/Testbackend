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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<File> cleanRecordFiles = getFilesFromDir(testSuiteDir, "screenshot");
        List<File> reportFiles = getFilesFromDir(testSuiteDir, "report");
        List<File> logResultFiles = getFilesFromDir(testSuiteDir, "log_check_result");
        LocalDateTime now = LocalDateTime.now();
        testSuite.setEndTime(now);
        testSuite.setStatus(STATUS.complete);
        testSuite.setTotalCases(totalCases);
        testSuite.setPassedCases(passedCases);
        testSuite.setFailedCases(failedCases);
        testSuite.setSkippedCases(skippedCases);
        testSuite.setCleanRecord(cleanRecordFiles);
        testSuite.setReportFile(reportFiles);
        testSuite.setLogCheckResultFile(logResultFiles);
        System.out.println("to be saved testSuite = " + testSuite);
        testSuiteRepository.save(testSuite);
    }

    private List<File> getFilesFromDir(Path testSuiteDir, String subDir) throws IOException {
        Path subPath = testSuiteDir.resolve(subDir);
        File subDirFile = new File(subPath.toString());
        File[] files = subDirFile.listFiles();
        return Arrays.asList(files);
    }

    @Override
    public List<TestSuite> browse() {
        List<TestSuite> testSuites = testSuiteRepository.findAllByOrderByIdDesc();
        return testSuites;
    }
}

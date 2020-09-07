package com.roborock.testbackend.services.repository;

import com.roborock.testbackend.entities.TestSuite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestSuiteRepository extends CrudRepository<TestSuite, Long> {
    List<TestSuite> findAll();
}

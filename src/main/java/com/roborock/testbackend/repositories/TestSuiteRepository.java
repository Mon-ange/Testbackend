package com.roborock.testbackend.repositories;

import com.roborock.testbackend.entities.TestSuite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestSuiteRepository extends CrudRepository<TestSuite, Long> {
    List<TestSuite> findAllByOrderByIdDesc();
}

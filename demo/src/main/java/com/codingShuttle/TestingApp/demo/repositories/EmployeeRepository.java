package com.codingShuttle.TestingApp.demo.repositories;

import com.codingShuttle.TestingApp.demo.entities.Employee;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface EmployeeRepository extends JpaRepositoryImplementation<Employee, Long> {
    List<Employee> findByEmail(String email);
}
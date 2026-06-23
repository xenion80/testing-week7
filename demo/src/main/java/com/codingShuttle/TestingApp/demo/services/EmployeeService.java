package com.codingShuttle.TestingApp.demo.services;

import com.codingShuttle.TestingApp.demo.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto getEmployeeById(Long id);


    EmployeeDto createNewEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id,EmployeeDto employeeDto);
    void deleteEmployee(Long id);
}

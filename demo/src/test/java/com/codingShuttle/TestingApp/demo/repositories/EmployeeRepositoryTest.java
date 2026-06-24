package com.codingShuttle.TestingApp.demo.repositories;

import com.codingShuttle.TestingApp.demo.TestContainerCofiguration;
import com.codingShuttle.TestingApp.demo.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Import(TestContainerCofiguration.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeRepositoryTest {
    @Autowired
    private  EmployeeRepository employeeRepository;

    private Employee employee;
    @BeforeEach
    void setup(){
        employee=Employee.builder()
                .name("Karan")
                .email("karan@gmail.com")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnEmployee() {

        //Arrange,given
        employeeRepository.save(employee);

        //Act,when
        List<Employee> employeeList=employeeRepository.findByEmail(employee.getEmail());

        //Assert,then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList(){
        //Given
        String email="notPresent.1234@gmail.com";

        //When
        List<Employee> employeeList=employeeRepository.findByEmail(email);

        //Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }
    
}
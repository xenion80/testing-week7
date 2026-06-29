package com.codingShuttle.TestingApp.demo.controllers;

import com.codingShuttle.TestingApp.demo.TestContainerCofiguration;
import com.codingShuttle.TestingApp.demo.dto.EmployeeDto;
import com.codingShuttle.TestingApp.demo.entities.Employee;
import com.codingShuttle.TestingApp.demo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;


class EmployeeControllerTestIT extends AbstractIntegrationTest{

    @Autowired
    private EmployeeRepository employeeRepository;



    @BeforeEach
    void setUp(){

        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());
        webTestClient.get()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());

    }

    @Test
    void testGetEmployeeById_Failure(){
        webTestClient.get()
                .uri("/employee/1")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee(){
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()//we are creating a employee so id cannot be checked
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
    }
    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testUpdateEmployee_whenAttemptingToUpdateTheEmail_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Random Name");
        testEmployeeDto.setEmail("RAndom@gmail.com");

        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }
    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());
        testEmployeeDto.setName("Random Name");
        testEmployeeDto.setSalary(500L);

        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }
    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        webTestClient.delete()
                .uri("/emloyee/1")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);

        webTestClient.delete()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

    }

}
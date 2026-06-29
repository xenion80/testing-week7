package com.codingShuttle.TestingApp.demo.controllers;

import com.codingShuttle.TestingApp.demo.TestContainerCofiguration;
import com.codingShuttle.TestingApp.demo.dto.EmployeeDto;
import com.codingShuttle.TestingApp.demo.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerCofiguration.class)
public class AbstractIntegrationTest {

    @Autowired
     WebTestClient webTestClient;

    Employee testEmployee =Employee.builder()
//                .id(1L)

            .name("Karan")
                .email("karan@gmail.com")
                .salary(200L)
                .build();
    EmployeeDto testEmployeeDto=EmployeeDto.builder()
//                .id(1L)

            .name("Karan")
                .email("karan@gmail.com")
                .salary(200L)
                .build();
}

package com.codingShuttle.TestingApp.demo.services.impl;

import com.codingShuttle.TestingApp.demo.TestContainerCofiguration;
import com.codingShuttle.TestingApp.demo.dto.EmployeeDto;
import com.codingShuttle.TestingApp.demo.entities.Employee;
import com.codingShuttle.TestingApp.demo.exceptions.ResourceNotFoundException;
import com.codingShuttle.TestingApp.demo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(TestContainerCofiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;
    private Employee mockEmployee;
    private EmployeeDto mockemployeeDto;




    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @BeforeEach
    void setUp(){
        mockEmployee=Employee.builder()
                .id(1L)
                .name("karan")
                .email("karan@gmail.com")
                .salary(200L)
                .build();
        mockemployeeDto=modelMapper.map(mockEmployee,EmployeeDto.class);
    }
    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
        //assign
        Long id= mockEmployee.getId();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));


        //act
        EmployeeDto employeeDto=employeeService.getEmployeeById(id);


        //assert
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository,times(1)).findById(id);
    }
    @Test
    void testGetEmployeeById_whenEmployeeIsNotPresent_thenThrowException(){
        //arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act and assert
        assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);



        //assert
    }
    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee(){
        //assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());

        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
        //act
        EmployeeDto employeeDto=employeeService.createNewEmployee(mockemployeeDto);


        //assert



        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockemployeeDto.getEmail());
        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee capturedEmployee=employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());

    }
    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException(){
        //arrange
        when(employeeRepository.findByEmail(mockemployeeDto.getEmail())).thenReturn(List.of(mockEmployee));

        //act
        assertThatThrownBy(()->employeeService.createNewEmployee(mockemployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployee.getEmail());
        verify(employeeRepository).findByEmail(mockemployeeDto.getEmail());
        verify(employeeRepository,never()).save(any());



        //assert
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        //arrage
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
//act and assert
        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockemployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }
    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_thenThrowException(){/*need supervision again in this test case didn't
    understand how its changing the email beforehand and checking if it was equal afterward*/
        when(employeeRepository.findById(mockemployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockemployeeDto.setName("RAndom");
        mockemployeeDto.setEmail("random@gamil.com");

        //act and assert
        assertThatThrownBy(()->employeeService.updateEmployee(mockemployeeDto.getId(),mockemployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(mockemployeeDto.getId());
        verify(employeeRepository,never()).save(any());

    }
    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee(){
        //arrange
        when(employeeRepository.findById(mockemployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockemployeeDto.setName("Random name");
        mockemployeeDto.setSalary(199L);

        Employee newEmployee=modelMapper.map(mockemployeeDto,Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        //act
        EmployeeDto updatedEmployeeDto=employeeService.updateEmployee(mockemployeeDto.getId(),mockemployeeDto);

        assertThat(updatedEmployeeDto).isEqualTo(mockemployeeDto);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());



    }
    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        when(employeeRepository.existsById(1L)).thenReturn(false);

        //act
        assertThatThrownBy(()->employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: "+1L);
        verify(employeeRepository,never()).deleteById(anyLong());
    }
    @Test
    void testDeleteEmployee_whenEmployeeIsValid_thenDeleteEmployee(){
        //arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertThatCode(()->employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();
        verify(employeeRepository).deleteById(1L);
    }

}
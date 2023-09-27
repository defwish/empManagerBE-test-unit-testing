package com.employee.service;

import com.employee.exceptions.EntityNotFoundException;
import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Arrays;
import com.employee.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;

public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        // Create a pageable object (you can use PageRequest for testing)
        Pageable pageable = PageRequest.of(0, 10);

        // Create a list of employees for the expected result
        List<Employee> employees = Arrays.asList(
                new Employee("1L", "John", "john@example.com", "Manager"),
                new Employee("2L", "Alice", "alice@example.com", "Developer")
                // Add more employees as needed
        );

        // Mock the behavior of the repository to return a Page<Employee> with the employees
        Page<Employee> page = new PageImpl<>(employees, pageable, employees.size());
        when(employeeRepository.findAll(pageable)).thenReturn(page);

        // Call the service method
        ResponseEntity<Page<Employee>> result = employeeService.getAllEmployees(pageable);

        // Assertions: Verify that the result matches the expected employees
        assertThat(result).isEqualTo(page);
    }


    @Test
    public void testCreateEmployee() {
        // Mock data
        Employee employeeToCreate = new Employee("1L", "John", "john@example.com", "Manager");

        // Mock the repository response
        when(employeeRepository.save(employeeToCreate)).thenReturn(employeeToCreate);

        // Perform the service method call
        Employee result = employeeService.createEmployee(employeeToCreate);

        // Verify the result
        assertEquals(employeeToCreate, result);

        verify(employeeRepository, times(1)).save(employeeToCreate);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void testUpdateEmployee() {
        // Mock data
        Long employeeId = 1L;
        Employee updatedEmployee = new Employee("1L", "Updated John", "updated@example.com", "Updated Manager");

        // Mock the repository response
        Optional<Employee> employeeOptional = Optional.of(updatedEmployee);
        when(employeeRepository.findById(employeeId)).thenReturn(employeeOptional);
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);

        // Perform the service method call
        Object result = (Employee) employeeService.updateEmployee(employeeId, updatedEmployee);

        // Verify the result
        assertEquals(updatedEmployee, result);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(updatedEmployee);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void testUpdateEmployeeNotFound() {
        // Mock data
        Long employeeId = 1L;
        Employee updatedEmployee = new Employee("1L", "Updated John", "updated@example.com", "Updated Manager");

        // Mock the repository response
        Optional<Employee> employeeOptional = Optional.empty();
        when(employeeRepository.findById(employeeId)).thenReturn(employeeOptional);

        // Perform the service method call and expect an exception
        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.updateEmployee(employeeId, updatedEmployee);
        });

        verify(employeeRepository, times(1)).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void testDeleteEmployee() {
        // Mock data
        Long employeeId = 1L;

        // Mock the repository response with a constructed Employee object
        Employee mockEmployee = new Employee("John", "john@example.com", "Manager","Manager");
        mockEmployee.setId(employeeId);
        Optional<Employee> employeeOptional = Optional.of(mockEmployee);
        when(employeeRepository.findById(employeeId)).thenReturn(employeeOptional);

        // Perform the service method call
        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }


    @Test
    public void testDeleteEmployeeNotFound() {
        // Mock data
        Long employeeId = 1L;

        // Mock the repository response
        Optional<Employee> employeeOptional = Optional.empty();
        when(employeeRepository.findById(employeeId)).thenReturn(employeeOptional);

        // Perform the service method call and expect an exception
        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.deleteEmployee(employeeId);
        });

        verify(employeeRepository, times(1)).findById(employeeId);
        verifyNoMoreInteractions(employeeRepository);
    }
}


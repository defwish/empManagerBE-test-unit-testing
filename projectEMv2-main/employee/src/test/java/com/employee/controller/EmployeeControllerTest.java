package com.employee.controller;

import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        // Mock data
        List<Employee> employees = List.of(
                new Employee("1L", "John", "john@example.com", "Manager"),
                new Employee("2L", "Jane", "jane@example.com", "Engineer")
        );
        Page<Employee> employeePage = Page.empty(); // Mock an empty page for simplicity
        Pageable pageable = Pageable.unpaged();

        when(employeeService.getAllEmployees(pageable)).thenReturn((ResponseEntity<Page<Employee>>) employeePage);

        // Perform the controller method call
        ResponseEntity<Page<Employee>> response = employeeController.getAllEmployees(pageable);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeePage, response.getBody());

        verify(employeeService, times(1)).getAllEmployees(pageable);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void testCreateEmployee() {
        // Mock data
        Employee employee = new Employee("1L", "John", "john@example.com", "Manager");

        when(employeeService.createEmployee(employee)).thenReturn(employee);

        // Perform the controller method call
        ResponseEntity<Employee> response = employeeController.createEmployee(employee);

        // Verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employee, response.getBody());

        verify(employeeService, times(1)).createEmployee(employee);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void testUpdateEmployee() {
        // Mock data
        Long employeeId = 1L;
        Employee updatedEmployee = new Employee("1L", "Updated John", "updated@example.com", "Updated Manager");
        Optional<Employee> employeeOptional = Optional.of(updatedEmployee);

        when(employeeService.updateEmployee(employeeId, updatedEmployee)).thenReturn(updatedEmployee);
        when(employeeService.updateEmployee(employeeId, updatedEmployee)).thenReturn(updatedEmployee);

        // Perform the controller method call
        ResponseEntity<Employee> response = employeeController.updateEmployee(employeeId, updatedEmployee);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEmployee, response.getBody());

        verify(employeeService, times(1)).updateEmployee(employeeId, updatedEmployee);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void testDeleteEmployee() {
        // Mock data
        Long employeeId = 1L;

        // Perform the controller method call
        ResponseEntity<String> response = employeeController.deleteEmployee(employeeId);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Angajatul cu ID-ul: " + employeeId + " a fost sters.", response.getBody());

        verify(employeeService, times(1)).deleteEmployee(employeeId);
        verifyNoMoreInteractions(employeeService);
    }
}

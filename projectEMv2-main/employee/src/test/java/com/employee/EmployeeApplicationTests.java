package com.employee;

import com.employee.controller.EmployeeController;
import com.employee.repository.EmployeeRepository;
import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import com.employee.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private EmployeeService employeeService;

	@MockBean
	private EmployeeRepository employeeRepository;


	@Autowired
	private EmployeeController employeeController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void contextLoads() {
		// Ensure that the context loads without errors
	}



	@Test
	public void testGetAllEmployees() throws Exception {
		// Create a list of employees
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee("1L", "John", "john@example.com", "Manager"));
		employees.add(new Employee("2L", "Alice", "alice@example.com", "Engineer"));
		// Add more employees as needed

		// Create a Page object from the list
		Page<Employee> page = new PageImpl<>(employees);

		// Create a ResponseEntity containing the Page
		ResponseEntity<Page<Employee>> responseEntity = ResponseEntity.ok(page);

		// Mock the service response
		given(employeeService.getAllEmployees(any(Pageable.class))).willReturn(responseEntity);

		// Perform the GET request and assert the results
		mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content", hasSize(employees.size())));

		// Add more assertions as needed
	}


	@Test
	public void testCreateEmployee() throws Exception {
		// Create a new Employee object
		Employee employee = new Employee("John", "john@example.com", "Manager", "Company");

		// Mock the service response
		when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

		// Perform a POST request to create an employee
		mockMvc.perform(post("/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(employee)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())))
				.andExpect(jsonPath("$.position", is(employee.getPosition())));

		// Add more assertions as needed
	}




	public static String asJsonString(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



	@Test
	public void testCreateEmployeeSuccess() throws Exception {
		// Create a new Employee object
		Employee employee = new Employee("1L", "John", "john@example.com", "Manager");

		// Mock the service response
		when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

		// Perform a POST request to create an employee
		mockMvc.perform(post("/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(employee)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())))
				.andExpect(jsonPath("$.position", is(employee.getPosition())));

		// Add more assertions as needed
	}





	@Test
	public void testUpdateEmployee() throws Exception {
		// Create an employee
		Employee updatedEmployee = new Employee("1L", "Updated John", "updatedjohn@example.com", "Updated Manager");

		// Mock the service response
		given(employeeService.updateEmployee(anyLong(), any(Employee.class))).willReturn(updatedEmployee);

		// Perform the PUT request and assert the results
		mockMvc.perform(MockMvcRequestBuilders.put("/employees/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"Updated John\",\"email\":\"updatedjohn@example.com\",\"position\":\"Updated Manager\"}"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(updatedEmployee.getId()))
				.andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
				.andExpect(jsonPath("$.email").value(updatedEmployee.getEmail()))
				.andExpect(jsonPath("$.position").value(updatedEmployee.getPosition()));

		// Add more assertions as needed
	}

	@Test
	public void testUpdateEmployeeNotFound() throws Exception {
		// Mock the service to throw EntityNotFoundException
		willThrow(EntityNotFoundException.class).given(employeeService).updateEmployee(anyLong(), any(Employee.class));

		// Perform the PUT request and assert the result is 404 Not Found
		mockMvc.perform(MockMvcRequestBuilders.put("/employees/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"Updated John\",\"email\":\"updatedjohn@example.com\",\"position\":\"Updated Manager\"}"))
				.andExpect(status().isNotFound());

		// Add more assertions as needed
	}

	@Test
	public void testDeleteEmployee() throws Exception {
		// Mock the service to delete the employee
		BDDMockito.doNothing().when(employeeService).deleteEmployee(anyLong());

		// Perform the DELETE request and assert the result is 200 OK
		mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
				.andExpect(status().isOk());

		// Add more assertions as needed
	}

	@Test
	public void testDeleteEmployeeNotFound() throws Exception {
		// Mock the service to throw EntityNotFoundException
		willThrow(EntityNotFoundException.class).given(employeeService).deleteEmployee(anyLong());

		// Perform the DELETE request and assert the result is 404 Not Found
		mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
				.andExpect(status().isNotFound());

		// Add more assertions as needed
	}
}

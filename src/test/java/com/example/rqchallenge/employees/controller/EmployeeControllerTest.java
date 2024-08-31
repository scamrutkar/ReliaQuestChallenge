package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeeDto;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private List<Employee> employees;

    @BeforeEach
    void setup(){
        employees = Arrays.asList(
                new Employee("1","Sagar Amrutkar", "10000", "25"),
                new Employee("2","Shweta Amrutkar", "50000", "23"),
                new Employee("3","Nikhil Amrutkar", "60000", "26"),
                new Employee("3","Pallavi Amrutkar", "100000", "25")
        );
    }

    @Test
    public void testGetAllEmployee_Success() throws Exception {
        Mockito.when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4));
        ;
    }

    @Test
    public void testGetEmployeeById_Success() throws Exception {
        Mockito.when(employeeService.getEmployeeById("1")).thenReturn(new Employee("1","Sagar Amrutkar", "10000", "25"));

        mockMvc.perform(get("/api/v1/employees/{id}",1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"1\",\"employee_name\":\"Sagar Amrutkar\",\"employee_salary\":\"10000\",\"employee_age\":\"25\",\"profile_image\":null}"));
    }

    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        Mockito.when(employeeService.getEmployeeById("1")).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/api/v1/employees/{id}",1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found"));
    }

    @Test
    public void testGetHighestSalary_Success() throws Exception {

        Mockito.when(employeeService.getHighestSalaryOfEmployees()).thenReturn(100000);

        mockMvc.perform(get("/api/v1/employees/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(content().string("100000"));
    }

    @Test
    public void testGetTopTenHighestSalaryEmployees_Success() throws Exception {

        Mockito.when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employees/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4));
    }

    @Test
    public void testCreateEmployee_Success() throws Exception {

        EmployeeDto mockEmp = new EmployeeDto("1","Sagar","10000","25","");

        Mockito.when(employeeService.createEmployee(new HashMap<>())).thenReturn(mockEmp);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(new HashMap<>())))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\n" +
                        "            \"id\" : \"1\",\n" +
                        "            \"name\": \"Sagar\",\n" +
                        "                \"salary\": \"10000\",\n" +
                        "                \"age\": \"25\",\n" +
                        "            \"profileImage\" : \"\"\n" +
                        "        }"));

    }

    @Test
    public void testCreateEmployee_TooManyRequests() throws Exception {

        Mockito.when(employeeService.createEmployee(new HashMap<>())).thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(new HashMap<>())))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Too many requests - please try again later."));

    }

    @Test
    public void testDeleteEmployee_Success() throws Exception {

        Mockito.when(employeeService.deleteEmployeeById("1")).thenReturn("1");

        mockMvc.perform(delete("/api/v1/employees/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testDeleteEmployee_NotFound() throws Exception {

        Mockito.when(employeeService.deleteEmployeeById("1")).thenThrow(new NoSuchElementException());

        mockMvc.perform(delete("/api/v1/employees/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found"));
    }

    @Test
    public void testDeleteEmployee_TooManyRequests() throws Exception {

        Mockito.when(employeeService.deleteEmployeeById("1")).thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        mockMvc.perform(delete("/api/v1/employees/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Too many requests - please try again later."));
    }
}

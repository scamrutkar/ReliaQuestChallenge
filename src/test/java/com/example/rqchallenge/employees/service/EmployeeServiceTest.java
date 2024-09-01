package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.constant.EmployeeConstant;
import com.example.rqchallenge.employees.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private RestClient restClient;

    private EmployeesResponse employeesResponse = new EmployeesResponse();


    @BeforeEach
    public void setup(){

        Employee emp1 = new Employee("1","Sagar","10345","26");
        Employee emp2 = new Employee("2","Shweta","65438","24");
        List<Employee> employees = Arrays.asList( emp1,emp2);

        employeesResponse.setData(employees);
        employeesResponse.setStatus("Success");

    }

    @Test
    public void testGetAllEmployees_Success(){
        Mockito.when(restClient.execute(EmployeeConstant.GET_ALL_EMPLOYEES_URI,HttpMethod.GET, EmployeesResponse.class)).thenReturn(employeesResponse);
        assertEquals(2, employeeService.getAllEmployees().size());
    }

    @Test
    public void testGetEmployeesByNameSearch_Success(){
        Mockito.when(restClient.execute(EmployeeConstant.GET_ALL_EMPLOYEES_URI,HttpMethod.GET, EmployeesResponse.class)).thenReturn(employeesResponse);
        assertEquals(1, employeeService.getEmployeesByNameSearch("Sa").size());
    }

    @Test
    public void testGetEmployeeById_Success(){

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setData(new Employee("1","Sagar","10345","26"));
        employeeResponse.setStatus("Success");

        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("id", "1");

        Mockito.when(restClient.execute(EmployeeConstant.GET_EMPLOYEE_BY_ID_URI,
                HttpMethod.GET, EmployeeResponse.class, pathVariable)).thenReturn(employeeResponse);
        assertEquals("Sagar", employeeService.getEmployeeById("1").getName());
    }

    @Test
    public void testGetEmployeeById_NotFund(){

        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("id", "3");

        EmployeeResponse mockEmployeeResponse = new EmployeeResponse();
        mockEmployeeResponse.setStatus("Failed");
        mockEmployeeResponse.setData(null);

        Mockito.when(restClient.execute(EmployeeConstant.GET_EMPLOYEE_BY_ID_URI,
                HttpMethod.GET, EmployeeResponse.class, pathVariable)).thenReturn(mockEmployeeResponse);

        assertThrows(NoSuchElementException.class, ()->{employeeService.getEmployeeById("3");});
    }

    @Test
    public void testGetEmployeeHighestSalary_Success(){
        Mockito.when(restClient.execute(EmployeeConstant.GET_ALL_EMPLOYEES_URI,HttpMethod.GET, EmployeesResponse.class)).thenReturn(employeesResponse);
        assertEquals(65438, employeeService.getHighestSalaryOfEmployees());
    }

    @Test
    public void testGetHighestSalaryOfEmployees_Success(){
        Mockito.when(restClient.execute(EmployeeConstant.GET_ALL_EMPLOYEES_URI,HttpMethod.GET, EmployeesResponse.class)).thenReturn(employeesResponse);
        assertEquals(2, employeeService.getTopTenHighestEarningEmployeeNames().size());
    }

    @Test
    public void testCreateEmployee_Success(){
        EmployeeResponseDto mockResponse = new EmployeeResponseDto();
        mockResponse.setData(new EmployeeDto("1","Sagar","2500","26",""));
        Mockito.when(restClient.execute(EmployeeConstant.CREATE_EMPLOYEES_URI,
                HttpMethod.POST, EmployeeResponseDto.class, new HashMap<>(), new HashMap<>())).thenReturn(mockResponse);
        assertEquals("Sagar", employeeService.createEmployee(new HashMap<>()).getName());
    }

    @Test
    public void testCreateEmployee_TooManyRequests(){

        Mockito.when(restClient.execute(EmployeeConstant.CREATE_EMPLOYEES_URI,
                HttpMethod.POST, EmployeeResponseDto.class, new HashMap<>(), new HashMap<>())).thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));
        assertThrows(HttpClientErrorException.class, () -> {employeeService.createEmployee(new HashMap<>()).getName();});
    }

    @Test
    public void testDeleteEmployeeById_Success(){

        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("id", "1234");

        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("status","success");
        jsonNode.put("data","1234");

        Mockito.when(restClient.execute(EmployeeConstant.DELETE_EMPLOYEE_BY_ID_URI,
                HttpMethod.DELETE, JsonNode.class, pathVariable)).thenReturn(jsonNode);

        assertEquals("1234", employeeService.deleteEmployeeById("1234"));
    }

    @Test
    public void testDeleteEmployeeById_NotFound(){

        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("id", "1234");

        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("status","success");

        Mockito.when(restClient.execute(EmployeeConstant.DELETE_EMPLOYEE_BY_ID_URI,
                HttpMethod.DELETE, JsonNode.class, pathVariable)).thenReturn(jsonNode);

        assertThrows(NoSuchElementException.class, () -> {employeeService.deleteEmployeeById("1234");});
    }

    @Test
    public void testDeleteEmployeeById_TooManyRequests(){

        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("id", "1234");

        Mockito.when(restClient.execute(EmployeeConstant.DELETE_EMPLOYEE_BY_ID_URI,
                HttpMethod.DELETE, JsonNode.class, pathVariable)).thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        assertThrows(HttpClientErrorException.class, () -> {employeeService.deleteEmployeeById("1234");});
    }
}

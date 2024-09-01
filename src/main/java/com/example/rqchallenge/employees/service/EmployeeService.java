package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.constant.EmployeeConstant;
import com.example.rqchallenge.employees.data.DataProvider;
import com.example.rqchallenge.employees.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements IEmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
    private final RestClient restClient;
    private final DataProvider dataProvider;

    public EmployeeService(RestClient restClient, DataProvider dataProvider){
        this.restClient = restClient;
        this.dataProvider = dataProvider;
    }

    @Override
    public List<Employee> getAllEmployees() {
        EmployeesResponse empResponse = getEmployeesData();
        return new ArrayList<>(empResponse.getData());
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        EmployeesResponse empResponse = getEmployeesData();
        return empResponse.getData().stream()
                .filter( emp -> emp.getName().contains(searchString) || emp.getName().equalsIgnoreCase(searchString))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(String id) {
        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("id", id);
        try {
            EmployeeResponse empResponse = restClient.execute(EmployeeConstant.GET_EMPLOYEE_BY_ID_URI,
                    HttpMethod.GET, EmployeeResponse.class, pathVariable);
            if(empResponse.getData() == null) {
                LOGGER.info("[getEmployeeById] Employee data doesn't exists with empId : {}", id);
                throw new NoSuchElementException();
            }
            return empResponse.getData();
        }catch(HttpClientErrorException e){
            LOGGER.info("[getEmployeeById] Too many requests on dummy rest host, checking mock data.");
            Optional<Employee> opEmp = dataProvider.getData()
                    .getData().stream()
                    .filter(emp -> emp.getId().equals(id))
                    .findFirst();

            if(opEmp.isPresent()) return opEmp.get();

            throw new NoSuchElementException();
        }catch(Exception e){
            LOGGER.info("[getEmployeeById] Internal server error, checking mock data.");
            Optional<Employee> opEmp = dataProvider.getData()
                    .getData().stream()
                    .filter(emp -> emp.getId().equals(id))
                    .findFirst();

            if(opEmp.isPresent()) return opEmp.get();

            throw new NoSuchElementException();
        }
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        EmployeesResponse empResponse = getEmployeesData();
        return empResponse.getData().stream()
                .map(emp -> Integer.valueOf(emp.getSalary()))
                .sorted(Collections.reverseOrder())
                .limit(1)
                .findFirst()
                .get();
    }

    @Override
    public List<Employee> getTopTenHighestEarningEmployeeNames() {
        EmployeesResponse empResponse = getEmployeesData();
        return empResponse.getData().stream()
                .sorted((e1,e2)->Integer.valueOf(e2.getSalary()).compareTo(Integer.valueOf(e1.getSalary())))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto createEmployee(Map<String, Object> employee) throws HttpClientErrorException{
        EmployeeResponseDto empResponse = restClient.execute(EmployeeConstant.CREATE_EMPLOYEES_URI,
                HttpMethod.POST, EmployeeResponseDto.class, new HashMap<>(), employee);
        return empResponse.getData();
    }

    @Override
    public String deleteEmployeeById(String id) {
        Map<String, String> pathVariable = new HashMap<>();
        pathVariable.put("id", id);
        JsonNode empResponse = restClient.execute(EmployeeConstant.DELETE_EMPLOYEE_BY_ID_URI,
                HttpMethod.DELETE, JsonNode.class, pathVariable);
        if(empResponse.path("data").isMissingNode() || empResponse.path("data") == null)
            throw new NoSuchElementException();
        return id;
    }

    private EmployeesResponse getEmployeesData() {
        EmployeesResponse empResponse;
        try{
            empResponse = restClient.execute(EmployeeConstant.GET_ALL_EMPLOYEES_URI,
                    HttpMethod.GET, EmployeesResponse.class);
        }catch(HttpClientErrorException e){
            LOGGER.info("[getAllEmployee] Too many requests on dummy host, mocking the response");
            empResponse = dataProvider.getData();
        }catch(Exception e){
            LOGGER.info("[getAllEmployee] Internal Server on dummy host, mocking the response");
            empResponse = dataProvider.getData();
        }
        return empResponse;
    }
}

package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.constant.EmployeeConstant;
import com.example.rqchallenge.employees.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public interface IEmployeeController {

    @GetMapping()
    ResponseEntity<List<Employee>> getAllEmployees() throws IOException;

    @GetMapping(EmployeeConstant.EMPLOYEE_SEARCH_URI)
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping(EmployeeConstant.EMPLOYEE_ID_URI)
    ResponseEntity<?> getEmployeeById(@PathVariable String id);

    @GetMapping(EmployeeConstant.HIGHEST_SALARY_URI)
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @GetMapping(EmployeeConstant.TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES_URI)
    ResponseEntity<List<Employee>> getTopTenHighestEarningEmployeeNames();

    @PostMapping()
    ResponseEntity<?> createEmployee(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping(EmployeeConstant.EMPLOYEE_ID_URI)
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

}

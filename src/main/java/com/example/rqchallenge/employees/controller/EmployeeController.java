package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.constant.EmployeeConstant;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(EmployeeConstant.EMPLOYEE_URI)
public class EmployeeController implements IEmployeeController{

    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    IEmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        LOGGER.info("[getAllEmployee] fetching all employees");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        LOGGER.info("[getEmployeesByNameSearch] fetching employee having name starts and matches with {} ", searchString);
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @Override
    public ResponseEntity<?> getEmployeeById(String id) {
        try {
            LOGGER.info("[getEmployeeById] fetching employee having employee id {} ", id);
            return ResponseEntity.ok(employeeService.getEmployeeById(id));
        }catch(NoSuchElementException e){
            LOGGER.error("[getEmployeeById] Employee not found having employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }catch (Exception e){
            LOGGER.error("[getEmployeeById] Exception while fetching an employee ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        LOGGER.info("[getHighestSalaryOfEmployees] fetching employee highest salary.");
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @Override
    public ResponseEntity<List<Employee>> getTopTenHighestEarningEmployeeNames() {
        LOGGER.info("[getHighestSalaryOfEmployees] fetching top ten highest employees salary.");
        return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
    }

    @Override
    public ResponseEntity<?> createEmployee(Map<String, Object> employeeInput) {
        try {
            LOGGER.info("[createEmployee] request received to create an employee.");
            return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeInput));
        }catch (HttpClientErrorException e){
            LOGGER.error("[createEmployee] too many request on dummy host, please try again.");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests - please try again later.");
        }catch (Exception e){
            LOGGER.error("[createEmployee] Exception while creating an employee ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        try {
            LOGGER.info("[deleteEmployeeById] request received to delete an employee.");
            return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
        }catch (HttpClientErrorException e){
            LOGGER.error("[deleteEmployeeById] too many request on dummy host, please try again.");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests - please try again later.");
        }catch (NoSuchElementException e){
            LOGGER.error("[deleteEmployeeById] Employee not found having employee id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }catch (Exception e){
            LOGGER.error("[deleteEmployeeById] Exception while deleting an employee ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

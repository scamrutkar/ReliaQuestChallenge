package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeeDto;

import java.util.List;
import java.util.Map;

public interface IEmployeeService {

    List<Employee> getAllEmployees();

    List<Employee> getEmployeesByNameSearch(String searchString);

    Employee getEmployeeById(String id);

    Integer getHighestSalaryOfEmployees();

    List<Employee> getTopTenHighestEarningEmployeeNames();

    EmployeeDto createEmployee(Map<String, Object> employee);

    String deleteEmployeeById(String id);
}

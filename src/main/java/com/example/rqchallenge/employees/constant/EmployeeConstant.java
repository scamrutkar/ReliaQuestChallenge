package com.example.rqchallenge.employees.constant;

public class EmployeeConstant {

    public static final String PROTOCOL = "https";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String DUMMY_API_HOST = "dummy.restapiexample.com";
    public static final String GET_ALL_EMPLOYEES_API = "api/v1/employees";
    public static final String GET_EMPLOYEES_BY_ID_API = "api/v1/employee/{id}";
    public static final String CREATE_EMPLOYEE = "api/v1/create";
    public static final String DELETE_EMPLOYEES_BY_ID_API = "api/v1/delete/{id}";

    public static final String GET_ALL_EMPLOYEES_URI = String.format("%s://%s/%s",PROTOCOL,DUMMY_API_HOST,GET_ALL_EMPLOYEES_API);
    public static final String GET_EMPLOYEE_BY_ID_URI = String.format("%s://%s/%s",PROTOCOL,DUMMY_API_HOST,GET_EMPLOYEES_BY_ID_API);
    public static final String CREATE_EMPLOYEES_URI = String.format("%s://%s/%s",PROTOCOL,DUMMY_API_HOST,CREATE_EMPLOYEE);
    public static final String DELETE_EMPLOYEE_BY_ID_URI = String.format("%s://%s/%s",PROTOCOL,DUMMY_API_HOST,DELETE_EMPLOYEES_BY_ID_API);


    public static final String EMPLOYEE_URI = "/api/v1/employees";
    public static final String EMPLOYEE_SEARCH_URI = "/search/{searchString}";
    public static final String EMPLOYEE_ID_URI = "/{id}";
    public static final String HIGHEST_SALARY_URI = "/highestSalary";
    public static final String TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES_URI = "/topTenHighestEarningEmployeeNames";
}

package com.example.rqchallenge.employees.model;

public class EmployeeResponseDto {

    private String status;

    private EmployeeDto data;

    private String message;

    public EmployeeResponseDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmployeeDto getData() {
        return data;
    }

    public void setData(EmployeeDto data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmployeeResponseDto{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}

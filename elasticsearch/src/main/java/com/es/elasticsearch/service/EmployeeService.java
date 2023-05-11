package com.es.elasticsearch.service;

import java.io.IOException;
import java.util.List;

import com.es.elasticsearch.entity.Employee;

public interface EmployeeService {
	Employee checkValidEmployee(String employeeID, String password) throws IOException;

	String saveEmployeeData(Employee employee);

	List<Employee> getEmployeeList();
}

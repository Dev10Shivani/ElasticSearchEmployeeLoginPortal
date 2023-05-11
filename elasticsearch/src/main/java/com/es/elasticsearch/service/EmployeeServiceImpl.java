package com.es.elasticsearch.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.elasticsearch.Repository.ElasticSearchQueryEmployee;
import com.es.elasticsearch.entity.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private ElasticSearchQueryEmployee elasticSearchQuery;

	@Override
	public Employee checkValidEmployee(String employeeID, String password) {
		LOGGER.info(">>>> checkValidEmployee()");
		try {
			Employee employee = elasticSearchQuery.getDocumentByEmployeeId(employeeID);
			LOGGER.info(">>>> : employee : {} ", employee);
			if (null != employee) {
				if (employeeID.equals(employee.getId()) && password.equals(employee.getPassword())) {
					return employee;
				} else {
					LOGGER.error(">>>>Invalid employee");
				}
			}
		} catch (Exception e) {
			LOGGER.error(">>>> failed to fetch employee for employeeId : {} , exception : {} ", employeeID,
					e.getMessage());
		}
		return null;
	}

	@Override
	public String saveEmployeeData(Employee employee) {
		LOGGER.info(">>>> saveEmployeeData() : employee : {} ", employee);
		String response = "";
		try {
			response = elasticSearchQuery.createUpdateDocumentEmployee(employee);
		} catch (Exception e) {
			LOGGER.error(">>>> error on saveEmployeeData() : {} , exception : {} ", employee, e.getMessage());
		}
		return response;
	}

	@Override
	public List<Employee> getEmployeeList() {
		LOGGER.info(">>>> getEmployeeList() ");
		try {
			List<Employee> employees = elasticSearchQuery.searchAllEmployeeDocuments();
			if (null != employees && !employees.isEmpty()) {
				LOGGER.info(">>>> getEmployeeList(), employees:{}", employees.toString());
				return employees;
			}
		} catch (Exception e) {
			LOGGER.error(">>>> error on getEmployeeList(), exception : {} ", e.getMessage());
		}
		return null;
	}

	/*
	 * List<Employee> employees = elasticSearchQuery.searchByKeyword(employeeID); if
	 * (null != employees && !employees.isEmpty()) {
	 * LOGGER.info(">>>> getEmployeeList(), employees:{}", employees.toString());
	 * 
	 * if(1==employees.size()) { Employee emp = employees.get(0);
	 * System.out.println(">>> employee : " + emp); return emp; } }
	 */
}

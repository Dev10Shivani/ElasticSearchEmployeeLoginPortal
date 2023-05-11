package com.es.elasticsearch.controller;

import com.es.elasticsearch.Repository.ElasticSearchQueryEmployee;
import com.es.elasticsearch.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchControllerEmployee {

    @Autowired
    private ElasticSearchQueryEmployee elasticSearchQuery;

    @PostMapping("/createUpdateDocumentEmployee")
    public ResponseEntity<Object> createUpdateDocumentEmployee(@RequestBody Employee employee) throws IOException {
        String response = elasticSearchQuery.createUpdateDocumentEmployee(employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getEmployeeDocument")
    public ResponseEntity<Object> getDocumentByEmpId(@RequestParam String employeeId) throws IOException {
        Employee employee =  elasticSearchQuery.getDocumentByEmployeeId(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/deleteEmployeeDocument")
    public ResponseEntity<Object> deleteDocumentByEmployeeId(@RequestParam String employeeId) throws IOException {
        String response =  elasticSearchQuery.deleteDocumentByEmployeeId(employeeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchEmployeeDocument")
    public ResponseEntity<Object> searchAllEmployeeDocument() throws IOException {
        List<Employee> employees = elasticSearchQuery.searchAllEmployeeDocuments();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}


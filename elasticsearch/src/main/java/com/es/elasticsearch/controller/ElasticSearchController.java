package com.es.elasticsearch.controller;

import com.es.elasticsearch.Repository.ElasticSearchQuery;
import com.es.elasticsearch.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchController {

    @Autowired
    private ElasticSearchQuery elasticSearchQuery;

    @PostMapping("/createOrUpdateDocument")
    public ResponseEntity<Object> createOrUpdateDocument(@RequestBody Employee employee) throws IOException {
        String response = elasticSearchQuery.createOrUpdateDocument(employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getDocument")
    public ResponseEntity<Object> getDocumentById(@RequestParam String userId) throws IOException {
        Employee employee =  elasticSearchQuery.getDocumentById(userId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<Object> deleteDocumentById(@RequestParam String userId) throws IOException {
        String response =  elasticSearchQuery.deleteDocumentById(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchDocument")
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        List<Employee> employees = elasticSearchQuery.searchAllDocuments();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}


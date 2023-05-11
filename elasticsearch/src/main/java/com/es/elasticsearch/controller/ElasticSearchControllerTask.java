package com.es.elasticsearch.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.es.elasticsearch.Repository.ElasticSearchQueryTask;
import com.es.elasticsearch.entity.Task;

@RestController
public class ElasticSearchControllerTask {

	@Autowired
	private ElasticSearchQueryTask elasticSearchQuery;

	@PostMapping("/createOrUpdateTaskDocument")
	public ResponseEntity<Object> createOrUpdateDocument(@RequestBody Task task) throws IOException {
		String response = elasticSearchQuery.createOrUpdateDocument(task);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getDocument")
	public ResponseEntity<Object> getDocumentById(@RequestParam String taskId) throws IOException {
		Task task = elasticSearchQuery.getDocumentById(taskId);
		return new ResponseEntity<>(task, HttpStatus.OK);
	}

	@DeleteMapping("/deleteDocument")
	public ResponseEntity<Object> deleteDocumentById(@RequestParam String taskId) throws IOException {
		String response = elasticSearchQuery.deleteDocumentById(taskId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/searchDocument")
	public ResponseEntity<Object> searchAllDocument() throws IOException {
		List<Task> tasks = elasticSearchQuery.searchAllDocuments();
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}
}

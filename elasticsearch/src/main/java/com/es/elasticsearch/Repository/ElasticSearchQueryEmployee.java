package com.es.elasticsearch.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.es.elasticsearch.entity.Employee;
import com.es.elasticsearch.entity.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

@Repository
public class ElasticSearchQueryEmployee {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "employees";


    public String createUpdateDocumentEmployee(Employee employee) throws IOException {

        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(employee.getId())
                .document(employee)
        );
        if (response.result().name().equals("Created")) {
            return new StringBuilder("Document has been successfully created.").toString();
        } else if (response.result().name().equals("Updated")) {
            return new StringBuilder("Document has been successfully updated.").toString();
        }
        return new StringBuilder("Error while performing the operation.").toString();
    }

    public Employee getDocumentByEmployeeId(String employeeId) throws IOException {
        Employee employee = null;
		GetResponse<Employee> response = elasticsearchClient.get(g -> g.index(indexName).id(employeeId), Employee.class);

        if (response.found()) {
            employee = response.source();
            System.out.println("getDocumentByEmployeeId >>>> Employee name " + employee.getFirstName() + " " + employee.getLastName());
        } else {
            System.out.println("Employee not found");
        }

        return employee;
    }

    public String deleteDocumentByEmployeeId(String employeeId) throws IOException {

        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(employeeId));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("Employee with id " + deleteResponse.id() + " has been deleted.").toString();
        }
        System.out.println("Employee not found");
        return new StringBuilder("Employee with id " + deleteResponse.id() + " does not exist.").toString();

    }

    public List<Employee> searchAllEmployeeDocuments() throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Employee.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Employee> employees = new ArrayList<>();
        for (Hit object : hits) {
            //System.out.print(((Employee) object.source()));
            employees.add((Employee) object.source());
        }
        return employees;
    }
    
    public List<Employee> searchByKeyword(String keyword) throws IOException {
		SearchRequest searchRequest =  SearchRequest.of(s -> s.index(indexName).query(q -> q.multiMatch(
		         t -> t .fields("id", "firstName", "lastName", "email", "password").query(keyword))));
		SearchResponse searchResponse =  elasticsearchClient.search(searchRequest, Employee.class);
		List<Hit> hits = searchResponse.hits().hits();
		List<Employee> employees = new ArrayList<>();
		for(Hit object : hits){
			employees.add((Employee) object.source()); 
		}
		return employees;
	}
    
    public Map<String, String> FetchEmpIdName() throws IOException {
    	SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Employee.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Employee> employees = new ArrayList<>();
        for (Hit object : hits) {
           // System.out.print(((Employee) object.source()));
            employees.add((Employee) object.source());
        }
        
        Map<String, String> empMap=new HashMap<>();  
        for (Employee e : employees) {
            empMap.put(e.getId().toString(), e.getFirstName().toString() + ' '+ e.getLastName().toString());
        }
        
		/*
		 * Set set=empMap.entrySet(); Iterator itr=set.iterator(); while(itr.hasNext()){
		 * Map.Entry entry=(Map.Entry)itr.next(); System.out.println("data... "+
		 * entry.getKey()+" "+entry.getValue()); }
		 */ 
        return empMap;
	}
}


package com.es.elasticsearch.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.es.elasticsearch.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "employees";


    public String createOrUpdateDocument(Employee employee) throws IOException {

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

    public Employee getDocumentById(String userId) throws IOException {
        Employee employee = null;
        GetResponse<Employee> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(userId),
                Employee.class
        );

        if (response.found()) {
            employee = response.source();
            System.out.println("User name " + employee.getFirstName() + " " + employee.getLastName());
        } else {
            System.out.println("User not found");
        }

        return employee;
    }

    public String deleteDocumentById(String userId) throws IOException {

        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(userId));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("User with id " + deleteResponse.id() + " has been deleted.").toString();
        }
        System.out.println("Product not found");
        return new StringBuilder("User with id " + deleteResponse.id() + " does not exist.").toString();

    }

    public List<Employee> searchAllDocuments() throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Employee.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Employee> employees = new ArrayList<>();
        for (Hit object : hits) {

            System.out.print(((Employee) object.source()));
            employees.add((Employee) object.source());

        }
        return employees;
    }
}


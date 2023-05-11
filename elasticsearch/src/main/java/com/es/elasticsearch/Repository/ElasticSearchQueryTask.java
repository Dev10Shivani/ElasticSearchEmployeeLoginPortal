package com.es.elasticsearch.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.transform.Settings;

import com.es.elasticsearch.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Repository
public class ElasticSearchQueryTask {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "tasks";

	/*
	 * public void createSettings() throws ExecutionException, InterruptedException
	 * { Settings indexSettings = ImmutableSettings.settingsBuilder()
	 * .put("number_of_shards", 1) .put("number_of_replicas", 1) .build();
	 * CreateIndexRequest indexRequest = new CreateIndexRequest(index,
	 * indexSettings); client.admin().indices().create(indexRequest).actionGet(); }
	 */
    
    public String createOrUpdateDocument(Task task) throws IOException {

        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(task.getId())
                .document(task)
        );
        if (response.result().name().equals("Created")) {
            return new StringBuilder("Document has been successfully created.").toString();
        } else if (response.result().name().equals("Updated")) {
            return new StringBuilder("Document has been successfully updated.").toString();
        }
        return new StringBuilder("Error while performing the operation.").toString();
    }

    public Task getDocumentById(String taskId) throws IOException {
    	Task task = null;
        GetResponse<Task> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(taskId),
                        Task.class
        );

        if (response.found()) {
            task = response.source();
            System.out.println("Task name: " + task.getTitle());
        } else {
            System.out.println("Task not found");
        }

        return task;
    }

    public String deleteDocumentById(String taskId) throws IOException {

        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(taskId));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("Task with id " + deleteResponse.id() + " has been deleted.").toString();
        }
        System.out.println("Task not found");
        return new StringBuilder("Task with id " + deleteResponse.id() + " does not exist.").toString();

    }

    public List<Task> searchAllDocuments() throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Task.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Task> tasks = new ArrayList<>();
        for (Hit object : hits) {
            //System.out.print(((Task) object.source()));
            tasks.add((Task) object.source());
        }
        return tasks;
    }
    
	public List<Task> searchTaskByKeyword(String keyword) throws IOException {
		SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName)
				.query(q -> q.multiMatch(t -> t.fields("id", "title", "description", "assignTo").query(keyword))));
		SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Task.class);
		List<Hit> hits = searchResponse.hits().hits();
		List<Task> tasks = new ArrayList<>();
		for (Hit object : hits) {
			//System.out.print(((Task) object.source()));
			tasks.add((Task) object.source());
		}
		return tasks;
	}
	
	public List<Task> getTasksByEmpId(String EmpId) throws IOException {
		SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName)
				.query(q -> q.match(t -> t.field("empId").query(EmpId))));
		SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Task.class);
		List<Hit> hits = searchResponse.hits().hits();
		List<Task> tasks = new ArrayList<>();
		for (Hit object : hits) {
			System.out.print(((Task) object.source()));
			tasks.add((Task) object.source());
		}
        return tasks;
    }
}


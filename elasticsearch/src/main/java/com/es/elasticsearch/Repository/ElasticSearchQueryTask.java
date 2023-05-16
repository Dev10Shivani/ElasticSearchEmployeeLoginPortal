package com.es.elasticsearch.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.es.elasticsearch.entity.Task;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Repository
public class ElasticSearchQueryTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchQueryTask.class);

	private final String indexName = "tasks";

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	@Autowired
	private TaskESRepository taskESRepository;

	public String createOrUpdateDocument(Task task) throws IOException {

		IndexResponse response = elasticsearchClient.index(i -> i.index(indexName).id(task.getId()).document(task));
		if (response.result().name().equals("Created")) {
			return new StringBuilder("Document has been successfully created.").toString();
		} else if (response.result().name().equals("Updated")) {
			return new StringBuilder("Document has been successfully updated.").toString();
		}
		return new StringBuilder("Error while performing the operation.").toString();
	}

	public Task getDocumentById(String taskId) throws IOException {
		Task task = null;
		GetResponse<Task> response = elasticsearchClient.get(g -> g.index(indexName).id(taskId), Task.class);

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
		SearchResponse searchResponse = elasticsearchClient.search(s -> {
			return s.index(indexName).from(0).size(10)
					.sort(so -> so.field(FieldSort.of(f -> f.field("id.keyword").order(SortOrder.Asc))));
		}, Task.class);
		List<Hit> hits = searchResponse.hits().hits();
		List<Task> tasks = new ArrayList<>();
		for (Hit object : hits) {
			// System.out.print(((Task) object.source()));
			tasks.add((Task) object.source());
		}
		return tasks;
	}

	public List<Task> searchTaskByKeyword(String keyword) throws IOException {
		SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName).size(10)
				.sort(so -> so.field(FieldSort.of(f -> f.field("id.keyword").order(SortOrder.Asc))))
				.query(q -> q.multiMatch(t -> t.fields("id", "title", "description", "assignTo").query(keyword))));
		SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Task.class);

		List<Hit> hits = searchResponse.hits().hits();
		List<Task> tasks = new ArrayList<>();
		for (Hit object : hits) {
			// System.out.print(((Task) object.source()));
			tasks.add((Task) object.source());
		}
		return tasks;
	}

	public Page<Task> findPaginated(int pageNo, int pageSize) {
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		return this.taskESRepository.findAll(pageable);
	}

	public List<Task> getTasksByEmpId(String EmpId) throws IOException {
		SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName)
				.sort(so -> so.field(FieldSort.of(f -> f.field("id.keyword").order(SortOrder.Asc))))
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

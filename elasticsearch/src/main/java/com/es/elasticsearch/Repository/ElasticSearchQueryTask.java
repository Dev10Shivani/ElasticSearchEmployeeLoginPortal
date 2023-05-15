package com.es.elasticsearch.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.transform.Settings;

import com.es.elasticsearch.entity.Task;
import com.es.elasticsearch.helper.Indices;
import com.es.elasticsearch.helper.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties.Async;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

@Repository
public class ElasticSearchQueryTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchQueryTask.class);

	private final String indexName = "tasks";

	// private final List<String> INDICES_TO_CREATE = List.of(Indices.TASK_INDEX);

	// private final RestHighLevelClient client;

	// private static final ObjectMapper Mapper = new ObjectMapper();

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	/*
	 * @Autowired public ElasticSearchQueryTask(RestHighLevelClient client) {
	 * this.client = client; }
	 * 
	 * @PostConstruct public void creatIndex() { final String settings =
	 * Util.loadAsString("static/mapping/task.json");
	 * 
	 * for (final String index : INDICES_TO_CREATE) { try { boolean indexExists =
	 * client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
	 * if (indexExists) { continue; }
	 * 
	 * final String mappings = Util.loadAsString("static/mapping/" + index +
	 * ".json"); if (settings == null || mappings == null) {
	 * LOGGER.error("failed to create index : " + index); continue; }
	 * 
	 * final CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
	 * createIndexRequest.settings(settings, XContentType.JSON);
	 * createIndexRequest.mapping(settings, XContentType.JSON);
	 * 
	 * client.indices().create(createIndexRequest, RequestOptions.DEFAULT); } catch
	 * (final Exception e) { LOGGER.error(e.getMessage(), e); } } }
	 * 
	 * public boolean index(final Task task) { try { final String taskAsString =
	 * Mapper.writeValueAsString(task);
	 * 
	 * IndexRequest request = new IndexRequest(Indices.TASK_INDEX);
	 * request.id(task.getId()); request.source(taskAsString, XContentType.JSON);
	 * 
	 * IndexResponse response = client.index(request, RequestOptions.DEFAULT);
	 * return response != null && response.status(.equals(RestStatus.OK))
	 * 
	 * }catch (final Exception e) { LOGGER.error(e.getMessage(), e); } }
	 */

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
		SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Task.class);
		List<Hit> hits = searchResponse.hits().hits();
		List<Task> tasks = new ArrayList<>();
		for (Hit object : hits) {
			// System.out.print(((Task) object.source()));
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
			// System.out.print(((Task) object.source()));
			tasks.add((Task) object.source());
		}
		return tasks;
	}

	public List<Task> getTasksByEmpId(String EmpId) throws IOException {
		SearchRequest searchRequest = SearchRequest
				.of(s -> s.index(indexName).query(q -> q.match(t -> t.field("empId").query(EmpId))));
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

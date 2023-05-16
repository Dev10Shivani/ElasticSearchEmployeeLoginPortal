package com.es.elasticsearch.Repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.es.elasticsearch.entity.Task;

public interface TaskESRepository extends ElasticsearchRepository<Task, String> {

}

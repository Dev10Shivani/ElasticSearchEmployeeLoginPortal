package com.es.elasticsearch.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.format.annotation.DateTimeFormat;

import com.es.elasticsearch.helper.Indices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = Indices.TASK_INDEX)
/* @Setting(settingPath = "static/mapping/task.json") */
public class Task {
	@Id
	@Field(type = FieldType.Keyword)
	private String id;

	@Field(type = FieldType.Text, name = "title")
	private String title;

	@Field(type = FieldType.Text, name = "description")
	private String description;

	@Field(type = FieldType.Text, name = "assignTo")
	private String assignTo;
	
	@Field(type = FieldType.Text, name = "empId")
	private String empId;

	@Field(type = FieldType.Text, name = "approxEstimation")
	private String approxEstimation;

	@Field(type = FieldType.Date, name = "expectedDueDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expectedDueDate;

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", description=" + description + ", assignTo=" + assignTo
				+ ", empId=" + empId + ", approxEstimation=" + approxEstimation + ", expectedDueDate=" + expectedDueDate
				+ "]";
	}
}

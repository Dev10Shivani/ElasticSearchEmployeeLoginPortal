package com.es.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "tasks")
public class Task {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "title")
    private String taskTitle;

    @Field(type = FieldType.Text, name = "description")
    private String taskDescription;

    @Field(type = FieldType.Text, name = "assignTo")
    private String assignTo;

    @Field(type = FieldType.Date, name = "approxEstimation")
    private Date approxEstimation;

    @Field(type = FieldType.Date, name = "expectedDueDate")
    private Date expectedDueDate;
}

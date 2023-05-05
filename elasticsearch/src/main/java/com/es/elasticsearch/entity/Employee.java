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
@Document(indexName = "employees")
public class Employee {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "firstname")
    private String firstName;

    @Field(type = FieldType.Text, name = "lastname")
    private String lastName;

    @Field(type = FieldType.Text, name = "email")
    private String email;

    @Field(type = FieldType.Text, name = "password")
    private String password;
}
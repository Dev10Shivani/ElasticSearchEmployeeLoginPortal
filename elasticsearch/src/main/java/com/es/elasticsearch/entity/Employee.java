package com.es.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.es.elasticsearch.helper.Indices;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = Indices.EMPLOYEE_INDEX)
/* @Setting(settingPath = "static/employee.json") */
public class Employee {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text, name = "firstName")
    private String firstName;

    @Field(type = FieldType.Text, name = "lastName")
    private String lastName;

    @Field(type = FieldType.Text, name = "email")
    private String email;

    @Field(type = FieldType.Text, name = "password")
    private String password;

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + "]";
	}
}

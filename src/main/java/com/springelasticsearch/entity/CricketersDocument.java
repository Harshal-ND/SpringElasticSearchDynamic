package com.springelasticsearch.entity;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Document(indexName = "harshal_index_2")
public class CricketersDocument {

	@Id
	private Integer cricketerId;
	private String firstName;
	private String lastName;
	@Transient
	private Date dateOfBirth;
	private String country;
	private String battingStyle;
	private String bowlingStyle;
	private int matchesPlayed;
	private int runsScored;
	private int wicketsTaken;
	private String description;
}

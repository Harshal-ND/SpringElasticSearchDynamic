package com.springelasticsearch.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "cricketers")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Cricketers {

	@Id
	@Column(name = "cricketer_id")
	private int cricketerId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "country")
	private String country;

	@Column(name = "batting_style")
	private String battingStyle;

	@Column(name = "bowling_style")
	private String bowlingStyle;

	@Column(name = "matches_played")
	private int matchesPlayed;

	@Column(name = "runs_scored")
	private int runsScored;

	@Column(name = "wickets_taken")
	private int wicketsTaken;

	@Column(name = "description")
	private String description;

}

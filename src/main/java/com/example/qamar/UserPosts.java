package com.example.qamar;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserPosts {

	private int id;
	@JsonProperty("user_id")
	private int userId;
	private String title;
	private String body;
}

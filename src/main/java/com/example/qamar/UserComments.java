package com.example.qamar;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserComments {

	private int id;
	@JsonProperty("post_id")
	private String postId;
	private String name;
	private String email;
	private String body;

}

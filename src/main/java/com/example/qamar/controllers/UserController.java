package com.example.qamar.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.qamar.User;
import com.example.qamar.UserComments;
import com.example.qamar.UserInfo;
import com.example.qamar.UserPosts;
import com.example.qamar.config.AuthRequest;
import com.example.qamar.config.JwtService;
import com.example.qamar.service.UserInfoService;

@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserInfoService service;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	RestTemplate restTemplate;

	// New User Creation
	@PostMapping("/addNewUser")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		return service.addUser(userInfo);
	}

	@GetMapping("/user/users")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public List<User> getAllusers() {

		final String ROOT_URI = "https://gorest.co.in/public/v2/users";

		ResponseEntity<User[]> response = restTemplate.getForEntity(ROOT_URI, User[].class);
		return Arrays.asList(response.getBody());

	}

	@GetMapping("/user/users/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Optional<User> getUserById(@PathVariable int id) {
		final String ROOT_URI = "https://gorest.co.in/public/v2/users";

		ResponseEntity<User[]> response = restTemplate.getForEntity(ROOT_URI, User[].class);
		Optional<User> user = Arrays.asList(response.getBody()).stream().filter(users -> users.getId() == id)
				.findFirst();

		return user.isPresent() ? user : null; // Instead of null you can also return empty Object

		// -----------If the url with id provides the single details based on id, the
		// following solution can be used with return type User----------//
		/*
		 * final String ROOT_URI = "https://gorest.co.in/public/v2/users/"+id;
		 * ResponseEntity<User> response = restTemplate.getForEntity(ROOT_URI,
		 * User.class); return Arrays.asList(response.getBody());
		 */

	}

	@GetMapping("/user/posts")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public List<UserPosts> getAllPosts() {

		final String ROOT_URI = "https://gorest.co.in/public/v2/posts";

		ResponseEntity<UserPosts[]> response = restTemplate.getForEntity(ROOT_URI, UserPosts[].class);
		return Arrays.asList(response.getBody());

	}

	@GetMapping("/user/posts/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Optional<UserPosts> getPostbyId(@PathVariable int id) {

		final String ROOT_URI = "https://gorest.co.in/public/v2/posts/";

		ResponseEntity<UserPosts[]> response = restTemplate.getForEntity(ROOT_URI, UserPosts[].class);
		Optional<UserPosts> posts = Arrays.asList(response.getBody()).stream().filter(post -> post.getId() == id)
				.findFirst();

		return posts.isPresent() ? posts : null; // Instead of null you can also return empty Employee Object
		
//-----------If the url with id provides the single details based on id, 
		// the following solution can be used with return type UserPosts----------//

		/*
		 * final String ROOT_URI = "https://gorest.co.in/public/v2/posts/"+id;
		 * ResponseEntity<UserPosts> response = restTemplate.getForEntity(ROOT_URI,
		 * UserPosts.class); return Arrays.asList(response.getBody());
		 */

	}

	@GetMapping("/user/comments")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public List<UserComments> getAllComments() {

		final String ROOT_URI = "https://gorest.co.in/public/v2/comments";

		ResponseEntity<UserComments[]> response = restTemplate.getForEntity(ROOT_URI, UserComments[].class);
		return Arrays.asList(response.getBody());

	}

	@GetMapping("/user/comments/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Optional<UserComments> getCommentsById(@PathVariable int id) {

		final String ROOT_URI = "https://gorest.co.in/public/v2/comments/";

		ResponseEntity<UserComments[]> response = restTemplate.getForEntity(ROOT_URI, UserComments[].class);
		Optional<UserComments> comments = Arrays.asList(response.getBody()).stream()
				.filter(comment -> comment.getId() == id).findFirst();

		return comments.isPresent() ? comments : null; // Instead of null you can also return empty Employee Object

//-----------If the url with id provides the single details based on id, 
		// the following solution can be used with return type UserPosts----------//

		/*
		 * final String ROOT_URI = "https://gorest.co.in/public/v2/comments/" + id;
		 * ResponseEntity<UserComments> response = restTemplate.getForEntity(ROOT_URI,
		 * UserComments.class); return Arrays.asList(response.getBody());
		 */

	}

	@PostMapping("/generateToken")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

}

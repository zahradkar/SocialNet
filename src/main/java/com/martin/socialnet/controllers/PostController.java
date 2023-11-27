package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.*;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/create")
	public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid NewPostDTO data) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't post as anonymous user.");

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return ResponseEntity.status(CREATED).body(postService.createPost(data, userDetails.getUsername()));
	}

	@PutMapping
	public ResponseEntity<Post> updateExistingPost(@RequestBody UpdatedPostDTO post, long userId) {
		return ResponseEntity.ok().body(postService.updateExistingPost(post, userId));
	}

	@GetMapping("/all")
	public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
		return ResponseEntity.ok(postService.getAllPost());
	}

	@PostMapping("/{id}/upvote")
	public ResponseEntity<VoteResponseDTO> upvote(@PathVariable long id) throws Exception {
		// TODO improve
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't vote as anonymous user.");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.status(ACCEPTED).body(postService.upvote(id, userDetails.getUsername()));
	}

	@PostMapping("/{postId}/downvote")
	public ResponseEntity<VoteResponseDTO> downvote(@PathVariable long postId) throws Exception {
		// TODO improve
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't vote as anonymous user.");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.status(ACCEPTED).body(postService.downvote(postId, userDetails.getUsername()));
	}

	@GetMapping("/loggedUser")
	public ResponseEntity<VotesResponseDTO> getUserVotes() throws Exception {
		// TODO improve
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't perform this action as anonymous user.");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(postService.getUserVotes(userDetails.getUsername()));
	}

	@DeleteMapping("/{id}/delete")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) throws Exception {
		// TODO authentication
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Log in first!");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		System.out.println(userDetails.getUsername());

		postService.deletePost(id, userDetails.getUsername());
		return ResponseEntity.status(NO_CONTENT).build();
	}
}

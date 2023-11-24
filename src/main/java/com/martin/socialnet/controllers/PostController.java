package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.LikeUpDTO;
import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.PostResponseDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.exceptions.UpvoteAlreadyExistsException;
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

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/create")
	public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid NewPostDTO post) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't post as anonymous user.");

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return ResponseEntity.status(CREATED).body(postService.createPost(post, userDetails.getUsername()));
	}

	@PutMapping
	public ResponseEntity<Post> updateExistingPost(@RequestBody UpdatedPostDTO post, long userId) {
		return ResponseEntity.ok().body(postService.updateExistingPost(post, userId));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<Post>> getAllPostsOfAUser(@PathVariable long userId) {
		return ResponseEntity.ok(postService.getAllPostOfAUser(userId));
	}

	@PostMapping("/{postId}/upvote")
	public ResponseEntity<Boolean> upvote(@PathVariable long postId) throws Exception, UpvoteAlreadyExistsException {
		// TODO improve
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't post as anonymous user.");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.status(ACCEPTED).body(postService.upvote(postId, userDetails.getUsername()));
	}

	@PostMapping("/{postId}/downvote")
	public ResponseEntity<Void> downvote() {
		// TODO everything
		return null;
	}
}

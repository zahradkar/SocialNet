package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PostMapping("/create")
	public ResponseEntity<Post> createPost(@RequestBody @Valid NewPostDTO post) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			// todo Handle unauthenticated case
			throw new Exception("some auth exception");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't post as anonymous user.");

		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok().body(postService.createPost(post, user));
	}

	/*@PostMapping("/create")
	public String createPost(@ModelAttribute Post post) {
		// Get the currently authenticated user.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();

		// Set the user ID on the post entity.
		post.setUserId(user.getId());

		// Save the post to the database.
		postRepository.save(post);

		// Redirect the user to their profile page.
		return "redirect:/profile";
	}*/


	@PutMapping
	public ResponseEntity<Post> updateExistingPost(@RequestBody UpdatedPostDTO post, long userId) {
		return ResponseEntity.ok().body(postService.updateExistingPost(post, userId));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<Post>> getAllPostsOfAUser(@PathVariable long userId) {
		return ResponseEntity.ok(postService.getAllPostOfAUser(userId));
	}
}

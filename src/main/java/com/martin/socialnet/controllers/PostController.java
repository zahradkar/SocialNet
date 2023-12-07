package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.*;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
		return ResponseEntity.status(CREATED).body(postService.createPost(data, UserController.getAuthUsername()));
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
		return ResponseEntity.status(ACCEPTED).body(postService.upvote(id, UserController.getAuthUsername()));
	}

	@PostMapping("/{postId}/downvote")
	public ResponseEntity<VoteResponseDTO> downvote(@PathVariable long postId) throws Exception {
		return ResponseEntity.status(ACCEPTED).body(postService.downvote(postId, UserController.getAuthUsername()));
	}

	@GetMapping("/loggedUser")
	public ResponseEntity<VotesResponseDTO> getUserVotes() throws Exception {
		return ResponseEntity.ok(postService.getUserVotes(UserController.getAuthUsername()));
	}

	@DeleteMapping("/{id}/delete")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) throws Exception {
		postService.deletePost(id, UserController.getAuthUsername());
		return ResponseEntity.status(NO_CONTENT).build();
	}
}

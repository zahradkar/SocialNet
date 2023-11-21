package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.CommentResponseDTO;
import com.martin.socialnet.dtos.NewCommentDTO;
import com.martin.socialnet.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {
	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/add")
	public ResponseEntity<CommentResponseDTO> addComment(@RequestBody @Valid NewCommentDTO data) {
		// TODO everything
		return ResponseEntity.ok(commentService.addComment(data.comment()));
	}
}

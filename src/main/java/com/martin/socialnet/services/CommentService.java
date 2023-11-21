package com.martin.socialnet.services;

import com.martin.socialnet.dtos.CommentResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
	CommentResponseDTO addComment(String comment);
}

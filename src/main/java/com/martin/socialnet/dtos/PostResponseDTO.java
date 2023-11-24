package com.martin.socialnet.dtos;

public record PostResponseDTO(
		String title,
		String content,
		String author,
		long postId
) {
}

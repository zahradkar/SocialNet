package com.martin.socialnet.dtos;

public record PostResponseDTO(
		long postId,
		String title,
		String content,
		String author,
		long createdAt,
		int likes
) {
}

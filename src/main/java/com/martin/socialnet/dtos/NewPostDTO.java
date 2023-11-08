package com.martin.socialnet.dtos;

import jakarta.validation.constraints.NotBlank;

public record NewPostDTO(
		@NotBlank
		String title,
		String mediaSource
) {
}

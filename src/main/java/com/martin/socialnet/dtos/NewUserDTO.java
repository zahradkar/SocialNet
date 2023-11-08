package com.martin.socialnet.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record NewUserDTO(
		@Length(min = 4)
		@NotBlank
		String username,
		@Length(min = 15, max = 50)
		@NotBlank
		String password
) {
}

package com.martin.socialnet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record UserDTO(
		// TODO update
		@Length(min = 4)
		@NotBlank
		String username,
		@Length(min = 15, max = 50)
		@NotBlank
		String password,
		@Email // todo improve this validation
		String email,
		@Length(max = 30)
		String firstName,
		@Length(max = 50)
		String lastName,
		String profilePictureURL,
		LocalDate birthday,
		String location
) {
}

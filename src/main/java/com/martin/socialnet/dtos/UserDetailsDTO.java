package com.martin.socialnet.dtos;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

// TODO test whole record
public record UserDetailsDTO(
		@Email // TODO improve validation
		String email,
		String firstName,
		String lastName,
		String profilePictureURL,
		LocalDate birthday,
		String location
) {
}

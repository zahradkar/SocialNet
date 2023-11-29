package com.martin.socialnet.dtos;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

// TODO test whole record
public record UserDetailsDTO(
		String firstName,
		String lastName,
		@Email
		String email, // TODO improve validation
		String profilePictureURL,
		LocalDate birthday,
		String location,
		long createdAt, // used only in response
		long updatedAt // used only in response
) {
}

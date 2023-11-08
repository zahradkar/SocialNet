package com.martin.socialnet.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@GetMapping("/check-login")
	public ResponseEntity<String> checkLogin(HttpServletRequest request) {
		// Check if the user is logged in (you might use Spring Security or another authentication mechanism)
		if (request.getUserPrincipal() == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
		return ResponseEntity.ok("User is logged in");
	}

	@GetMapping("/login")
	public String login() {
		return "";
	}
}

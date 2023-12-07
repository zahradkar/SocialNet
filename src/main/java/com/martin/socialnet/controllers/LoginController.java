package com.martin.socialnet.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@GetMapping("/check-login")
	public ResponseEntity<String> checkLogin(HttpServletRequest request) {
		// Checks if the user is logged in
		System.out.println(request.getUserPrincipal());
		if (request.getUserPrincipal() == null)
			throw new BadCredentialsException("Wrong password or username");
		return ResponseEntity.ok().header("Content-Type", "text/plain").body(request.getUserPrincipal().getName());
	}

	@GetMapping("/login")
	public String login() {
		return "";
	}
}

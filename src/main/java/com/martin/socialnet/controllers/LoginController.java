package com.martin.socialnet.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LoginController {

	@GetMapping("/check-login")
	public ResponseEntity<String> checkLogin(HttpServletRequest request) {
		// Checks if the user is logged in
		System.out.println(request.getUserPrincipal());
		if (request.getUserPrincipal() == null)
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login controller: User is not logged in");
//			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password or username");
			throw new BadCredentialsException("Wrong password or username");
		return ResponseEntity.ok().header("Content-Type", "text/plain").body(request.getUserPrincipal().getName());
	}

	@GetMapping("/login")
	public String login() {
		return "";
	}
}

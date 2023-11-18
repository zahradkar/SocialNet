package com.martin.socialnet.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class ExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

	// TODO review exception handler below
	@org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
	private ResponseEntity<String> runtimeExceptionHandler(Exception e) {
		// handles IllegalArgumentException and NumberFormatException
		return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
	private ResponseEntity<String> authenticationExceptionHandler(Exception e) {
		return ResponseEntity.status(CONFLICT).body(e.getMessage());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
	private ResponseEntity<String> badCredentialExceptionHandler(Exception e) {
		return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
	private ResponseEntity<String> notExistingURLExceptionHandler(Exception e) {
		return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
	}
}

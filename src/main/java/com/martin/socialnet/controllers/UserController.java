package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable long id) {
		return ResponseEntity.ok().body(userService.getUserById(id));
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody @Valid NewUserDTO user) throws AuthenticationException {
		return ResponseEntity.ok().body(userService.saveNewUser(user.username(), user.password()));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<User> updateUserById(@RequestBody @PathVariable long id, NewUserDTO user) {
		return ResponseEntity.ok().body(userService.updateUserById(id, user));
	}
}

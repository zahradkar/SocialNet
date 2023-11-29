package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.dtos.UserDetailsDTO;
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
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody @Valid NewUserDTO user) throws AuthenticationException {
		return ResponseEntity.ok(userService.registerNewUser(user.username(), user.password()));
	}

	@PostMapping("/userDetails")
	public ResponseEntity<Void> setUserDetails(@RequestBody @Valid UserDetailsDTO d) {
		return ResponseEntity.ok(userService.setUserDetails(d.firstName(), d.lastName(),d.email(),d.location(),d.profilePictureURL(),d.birthday()));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<User> updateUserById(@RequestBody @PathVariable long id, NewUserDTO user) {
		return ResponseEntity.ok(userService.updateUserById(id, user));
	}
}

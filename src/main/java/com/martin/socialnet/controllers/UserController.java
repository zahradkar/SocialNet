package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.dtos.UserDetailsDTO;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	protected static String getAuthUsername() throws Exception {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			throw new Exception("You have to log in first!");

		if (authentication.getPrincipal() == "anonymousUser")
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't perform this action as anonymous user.");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userDetails.getUsername();
	}

	@GetMapping("/{id}")
//	public ResponseEntity<User> getUser(@PathVariable long id) {
	public ResponseEntity<User> getUser(@PathVariable String id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@RequestBody @Valid NewUserDTO user) throws AuthenticationException {
		userService.registerNewUser(user.username(), user.password());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/setDetails")
	public ResponseEntity<UserDetailsDTO> setUserDetails(@RequestBody @Valid UserDetailsDTO dto) throws Exception {
		return ResponseEntity.ok(userService.setUserDetails(getAuthUsername(), dto.firstName(), dto.lastName(), dto.email(), dto.location(), dto.profilePictureURL(), dto.birthday()));
	}

	@GetMapping("/getDetails")
	public ResponseEntity<UserDetailsDTO> getUserDetails() throws Exception {
		return ResponseEntity.ok(userService.getUserDetails(getAuthUsername()));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<User> updateUserById(@RequestBody @PathVariable long id, NewUserDTO user) {
		return ResponseEntity.ok(userService.updateUserById(id, user));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteUser() throws Exception {
		userService.deleteUser(getAuthUsername());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}

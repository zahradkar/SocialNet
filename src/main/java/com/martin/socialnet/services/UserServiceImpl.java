package com.martin.socialnet.services;

import com.martin.socialnet.config.SecurityUser;
import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.dtos.UserDetailsDTO;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
//	public User getUserById(long id) {
	public User getUserById(String username) {
		// TODO validate id
		var user = userRepository.getReferenceById(username);
		logger.debug(user.getUsername());
		return user;
	}

	@Override
	public User updateUserById(long id, NewUserDTO user) {
		return null;
	}

	@Override
	public UserDetailsDTO setUserDetails(String username, String firstName, String lastName, String email, String location, String photoURL, LocalDate birthday) throws AuthenticationException {
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		if (email != null && userRepository.existsByEmail(email) && !user.getEmail().equals(email))
			throw new AuthenticationException("E-mail already exists in the database!");

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setLocation(location);
		user.setProfilePictureURL(photoURL);
		user.setBirthday(birthday);
		long updatedAt = System.currentTimeMillis();
		user.setUpdatedAt(updatedAt);

		userRepository.save(user);
		return new UserDetailsDTO(firstName, lastName, email, photoURL, birthday, location, user.getCreatedAt(), updatedAt);
	}

	@Override
	public UserDetailsDTO getUserDetails(String username) {
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		return new UserDetailsDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfilePictureURL(), user.getBirthday(), user.getLocation(), user.getCreatedAt(), user.getUpdatedAt());
	}

	@Override
	public void registerNewUser(String username, String password) throws AuthenticationException {
		if (userRepository.existsUsersByUsername(username))
			throw new AuthenticationException("Username already exist!");
		// TODO consider return UserDetailsDTO
		userRepository.save(new User(username, passwordEncoder.encode(password)));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findByUsername(username);
		return user.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
	}

//	@Override
//	public void deleteUser(String username) {
//		userRepository.deleteById(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!")).getId());
//	}

	@Override
	public void deleteUser(String username) {
//		userRepository.deleteById(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!")).getId());
		userRepository.deleteById(username);
	}
}

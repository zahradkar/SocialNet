package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.repositories.UserRepository;
import com.martin.socialnet.config.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

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
	public User getUserById(long id) {
		// TODO validate id
		var user = userRepository.getReferenceById(id);
		logger.debug(user.getUsername());
		return user;
	}

	@Override
	public User updateUserById(long id, NewUserDTO user) {
		return null;
	}

	@Override
	public User registerNewUser(String username, String password) throws AuthenticationException {
		if (userRepository.existsUsersByUsername(username))
			throw new AuthenticationException("Username already exist!");
		return userRepository.save(new User(username, passwordEncoder.encode(password)));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findByUsername(username);
		return user.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
	}
}

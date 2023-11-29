package com.martin.socialnet.services;

import com.martin.socialnet.config.SecurityUser;
import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.dtos.UserDetailsDTO;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.repositories.UserRepository;
import jakarta.transaction.Transactional;
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

	/*@Override
	public Void setUserDetails(String username, String name, String surname, String email, String location, String photoURL, LocalDate birthday) {
		logger.debug("name: " + name);
		logger.debug("surname: " + surname);
		logger.debug("e-mail: " + email);
		logger.debug("location: " + location);
		logger.debug("photo: " + photoURL);
		logger.debug("birthday: " + birthday);
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		user.setFirstName(name);
		user.setLastName(surname);
		user.setEmail(email);
		user.setLocation(location);
		user.setProfilePictureURL(photoURL);
		user.setBirthday(birthday);
		user.setUpdatedAt(System.currentTimeMillis());
		userRepository.save(user);
		return null;
	}*/
	@Override
	public UserDetailsDTO setUserDetails(String username, UserDetailsDTO dto) throws Exception {
		logger.debug("first name: " + dto.firstName());
		logger.debug("last name: " + dto.lastName());
		logger.debug("e-mail: " + dto.email());
		logger.debug("location: " + dto.location());
		logger.debug("photo: " + dto.profilePictureURL());
		logger.debug("birthday: " + dto.birthday());

		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		if (userRepository.existsByEmail(dto.email()) && !user.getEmail().equals(dto.email()))
			throw new AuthenticationException("E-mail already exists in the database!");

		user.setFirstName(dto.firstName());
		user.setLastName(dto.lastName());
		user.setEmail(dto.email());
		user.setLocation(dto.location());
		user.setProfilePictureURL(dto.profilePictureURL());
		user.setBirthday(dto.birthday());
		long updatedAt = System.currentTimeMillis();
		user.setUpdatedAt(updatedAt);
		userRepository.save(user);

		return new UserDetailsDTO(dto.firstName(), dto.lastName(), dto.email(), dto.profilePictureURL(), dto.birthday(), dto.location(), user.getCreatedAt(), updatedAt);
	}

	@Override
	public UserDetailsDTO getUserDetails(String username) {
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		return new UserDetailsDTO(user.getFirstName(), user.getLastName(),user.getEmail(),user.getProfilePictureURL(),user.getBirthday(),user.getLocation(),user.getCreatedAt(),user.getUpdatedAt());
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

	@Override
	public void deleteUser(String username) {
		// TODO debug (does not work)
//		userRepository.deleteByUsername(username);
		userRepository.deleteById(3L);
//		userRepository.deleteById(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!")).getId());
	}
}

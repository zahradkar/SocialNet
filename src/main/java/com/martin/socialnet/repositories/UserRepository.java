package com.martin.socialnet.repositories;

import com.martin.socialnet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// @Repository NOT NECESSARY USE IT 'cause is an interface
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsUsersByUsername(String username);

	Optional<User> findByUsername(String username);

	boolean existsByEmail(String email);

	void deleteByUsername(String username);
}

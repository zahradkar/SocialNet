package com.martin.socialnet.repositories;

import com.martin.socialnet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsUsersByUsername(String username);

	Optional<User> findByUsername(String username);
}

package com.martin.socialnet.repositories;

import com.martin.socialnet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsUsersByUsername(String username);
}

package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.entities.User;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public interface UserService {
	User getUserById(long id);
	User registerNewUser(String username, String password) throws AuthenticationException;

	User updateUserById(long id, NewUserDTO user);
}

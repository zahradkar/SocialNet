package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.dtos.UserDetailsDTO;
import com.martin.socialnet.entities.User;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public interface UserService {
	User getUserById(long id);
	void registerNewUser(String username, String password) throws AuthenticationException;

	User updateUserById(long id, NewUserDTO user);

	UserDetailsDTO setUserDetails(String username, UserDetailsDTO dto) throws Exception;

	UserDetailsDTO getUserDetails(String username);

	void deleteUser(String username);
//	Void setUserDetails(String username, String name, String surname, String email, String location, String photoURL, LocalDate birthday);
}

package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewUserDTO;
import com.martin.socialnet.entities.User;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;

@Service
public interface UserService {
	User getUserById(long id);
	User registerNewUser(String username, String password) throws AuthenticationException;

	User updateUserById(long id, NewUserDTO user);

	Void setUserDetails(String name, String surname, String email, String location, String photoURL, LocalDate birthday);
}

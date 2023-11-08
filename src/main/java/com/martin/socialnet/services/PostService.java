package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService{
	Post createPost(NewPostDTO post, User user);

	Post updateExistingPost(UpdatedPostDTO post, long userId);

	List<Post> getAllPostOfAUser(long userId);
}

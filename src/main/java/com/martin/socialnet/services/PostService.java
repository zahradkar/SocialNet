package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.PostResponseDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.entities.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
	PostResponseDTO createPost(NewPostDTO post, String username);

	Post updateExistingPost(UpdatedPostDTO post, long userId);

	List<Post> getAllPostOfAUser(long userId);
}

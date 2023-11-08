package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
	private final PostRepository postRepository;

	public PostServiceImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public Post createPost(NewPostDTO dto, User user) {
		if (user == null) {
			logger.debug("User: " + String.valueOf(user));
			throw new IllegalArgumentException("Error: unable to create new post due to missing user to be bound with");
		}
		var post = new Post(dto.title(), dto.mediaSource());

		return postRepository.save(new Post());
	}

	@Override
	public Post updateExistingPost(UpdatedPostDTO post, long userId) {
		return null;
	}

	@Override
	public List<Post> getAllPostOfAUser(long userId) {
		return null;
	}
}

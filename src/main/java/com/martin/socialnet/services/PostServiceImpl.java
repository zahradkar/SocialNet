package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.repositories.PostRepository;
import com.martin.socialnet.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Post createPost(NewPostDTO dto, String username) {
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Error: unable to create new post due to missing user to be bound with"));
		return postRepository.save(new Post(dto.title(), dto.content(), user));
	}

	@Override
	public Post updateExistingPost(UpdatedPostDTO post, long userId) {
		// TODO everything
		return null;
	}

	@Override
	public List<Post> getAllPostOfAUser(long userId) {
		// TODO everything
		return null;
	}
}

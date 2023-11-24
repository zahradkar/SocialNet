package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.PostResponseDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.exceptions.PostNotFoundException;
import com.martin.socialnet.exceptions.UpvoteAlreadyExistsException;
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
	public PostResponseDTO createPost(NewPostDTO dto, String username) {
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Error: unable to create new post due to missing user to be bound with"));
		var post = postRepository.save(new Post(dto.title(), dto.content(), user));
		return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), username, post.getCreatedAt(), post.getLikes());
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

	@Override
	public boolean upvote(long postId, String username) throws PostNotFoundException, UpvoteAlreadyExistsException {
		// TODO test
		var post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post " + postId + " was not found!"));
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Error - unable to upvote, voting user not found!"));

		if (post.getLikedByUsers().contains(user))
			throw new UpvoteAlreadyExistsException(username + " already upvoted this post. You have just one vote!");

		post.getLikedByUsers().add(user);
		postRepository.save(post);
		return true;
	}
}

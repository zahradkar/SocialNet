package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.PostResponseDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.dtos.VoteResponseDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.exceptions.PostNotFoundException;
import com.martin.socialnet.exceptions.UpvoteAlreadyExistsException;
import com.martin.socialnet.repositories.PostRepository;
import com.martin.socialnet.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
	public List<PostResponseDTO> getAllPost() {
		var posts = postRepository.findAll();
		List<PostResponseDTO> postResponses = new ArrayList<>();

		for (Post post : posts)
			postResponses.add(new PostResponseDTO(post.getId(), post.getTitle(),post.getContent(),post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName(),post.getCreatedAt(),post.getLikes()));

		return postResponses;
	}

	@Override
	public VoteResponseDTO upvote(long postId, String username) throws PostNotFoundException, UpvoteAlreadyExistsException {
		var post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post " + postId + " was not found!"));
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Error - unable to upvote, voting user not found!"));

		if (post.getLikedByUsers().contains(user)) {
			post.getLikedByUsers().remove(user);
			postRepository.save(post);
			return new VoteResponseDTO(post.getLikes(), "Vote removed!", false);
		} else
			post.getLikedByUsers().add(user);

		postRepository.save(post);
		return new VoteResponseDTO(post.getLikes(), "Voted!", true);
	}

	@Override
	public VoteResponseDTO downvote(long postId, String username) throws PostNotFoundException, UpvoteAlreadyExistsException {
		var post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post " + postId + " was not found!"));
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Error - unable to upvote, voting user not found!"));

		if (post.getDislikedByUsers().contains(user)) {
			post.getDislikedByUsers().remove(user);
			postRepository.save(post);
			return new VoteResponseDTO(post.getLikes(), "Down vote removed!", false);
		} else
			post.getDislikedByUsers().add(user);

		postRepository.save(post);
		return new VoteResponseDTO(post.getLikes(), "Down voted!", true);
	}
}

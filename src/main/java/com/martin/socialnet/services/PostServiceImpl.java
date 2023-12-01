package com.martin.socialnet.services;

import com.martin.socialnet.dtos.*;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.entities.User;
import com.martin.socialnet.exceptions.PostNotFoundException;
import com.martin.socialnet.repositories.PostRepository;
import com.martin.socialnet.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
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

	private static String getAuthor(User user) {
		String result = user.getFirstName() + " " + user.getLastName();
		return result.isBlank() ? user.getUsername() : result;
	}

	@Override
	public void deletePost(Long id, String username) throws Exception {
		// TODO update exception accordingly (return code)
		if (id == null)
			throw new Exception("Id must not be null!");
		var post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found!"));
		if (!post.getAuthor().getUsername().equals(username))
			throw new AuthenticationException("You can delete only your posts!");
		postRepository.deleteById(id);
	}

	@Override
	public PostResponseDTO createPost(NewPostDTO data, String username) {
		logger.debug(data.title());
		logger.debug(data.content());
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Error: unable to create new post due to missing user to be bound with"));
		var post = postRepository.save(new Post(data.title(), data.content(), user));

		return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), getAuthor(user), post.getCreatedAt(), post.getLikes(), user.getProfilePictureURL());
	}

	@Override
	public List<PostResponseDTO> getAllPost() {
		var posts = postRepository.findAll();
		List<PostResponseDTO> postResponses = new ArrayList<>();

		for (Post post : posts)
			postResponses.add(new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), getAuthor(post.getAuthor()), post.getCreatedAt(), post.getLikes(), post.getAuthor().getProfilePictureURL()));

		return postResponses;
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
	public VotesResponseDTO getUserVotes(String username) {
		var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));
		List<Long> likes = new ArrayList<>();
		for (int i = 0; i < user.getLikedPosts().size(); i++)
			likes.add(user.getLikedPosts().get(i).getId());

		List<Long> dislikes = new ArrayList<>();
		for (int i = 0; i < user.getDislikedPosts().size(); i++)
			dislikes.add(user.getDislikedPosts().get(i).getId());

		logger.info("Returning liked and disliked posts!");
		return new VotesResponseDTO(likes, dislikes);
	}

	@Override
	public VoteResponseDTO upvote(long postId, String username) throws PostNotFoundException {
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
	public VoteResponseDTO downvote(long postId, String username) throws PostNotFoundException {
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

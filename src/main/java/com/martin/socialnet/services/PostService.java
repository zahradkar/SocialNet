package com.martin.socialnet.services;

import com.martin.socialnet.dtos.*;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.exceptions.PostNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
	PostResponseDTO createPost(NewPostDTO post, String username);

	Post updateExistingPost(UpdatedPostDTO post, long userId);

	List<Post> getAllPostOfAUser(long userId);

	VoteResponseDTO upvote(long postId, String username) throws PostNotFoundException;

	VoteResponseDTO downvote(long postId, String username) throws PostNotFoundException;

	List<PostResponseDTO> getAllPost();

	VotesResponseDTO getUserVotes(String username);

	void deletePost(Long id, String username) throws Exception;
}

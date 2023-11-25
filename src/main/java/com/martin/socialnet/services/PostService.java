package com.martin.socialnet.services;

import com.martin.socialnet.dtos.NewPostDTO;
import com.martin.socialnet.dtos.PostResponseDTO;
import com.martin.socialnet.dtos.UpdatedPostDTO;
import com.martin.socialnet.dtos.VoteResponseDTO;
import com.martin.socialnet.entities.Post;
import com.martin.socialnet.exceptions.PostNotFoundException;
import com.martin.socialnet.exceptions.UpvoteAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
	PostResponseDTO createPost(NewPostDTO post, String username);

	Post updateExistingPost(UpdatedPostDTO post, long userId);

	List<Post> getAllPostOfAUser(long userId);

	VoteResponseDTO upvote(long postId, String username) throws PostNotFoundException, UpvoteAlreadyExistsException;

	VoteResponseDTO downvote(long postId, String username) throws PostNotFoundException, UpvoteAlreadyExistsException;

	List<PostResponseDTO> getAllPost();
}

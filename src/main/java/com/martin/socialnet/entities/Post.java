package com.martin.socialnet.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
	@Column(name = "created_at", columnDefinition = "BIGINT UNSIGNED")
	private final long createdAt = System.currentTimeMillis(); // TODO somehow improve long -> unsigned long
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Comment> comments = new ArrayList<>();
	@Id
	@Column(columnDefinition = "bigint unsigned")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	private String content; // e.g. url of an image ar webpage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private User author;
	private int likes;
	@Column(name = "updated_at", columnDefinition = "BIGINT UNSIGNED")
	private long updatedAt; // TODO somehow improve long -> unsigned long

	public Post(String title, String content, User author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}

	public Post() {
	}
//	ArrayList<String> comments; // TODO think how to do it
/*
	post_id (INT): Primary key. Unique identifier for the post.
	user_id (INT): Foreign key to the user table. Identifies the user who created the post.
	post_text (VARCHAR(255)): The text of the post.
	posted_at (DATETIME): The date and time the post was created.
	updated_at (DATETIME): The date and time the post was last updated.

	In addition to these basic columns, Facebook's post table may also include other columns to store additional information, such as:
	post_type (VARCHAR(255)): The type of post, such as "text", "photo", or "video".
	post_privacy (VARCHAR(255)): The privacy setting for the post, such as "public", "friends only", or "private".
	post_likes (INT): The number of likes the post has received.
	post_comments (INT): The number of comments the post has received.
	post_shares (INT): The number of times the post has been shared.*/
}

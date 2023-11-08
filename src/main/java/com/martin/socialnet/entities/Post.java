package com.martin.socialnet.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "posts")
public class Post {
	@Id
	@Column(columnDefinition = "bigint unsigned")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "media_source")
	private String mediaSource; // e.g. url of an image ar webpage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	private int likes;
	private String author;
	private String title;
	@Column(name = "created_at", columnDefinition = "BIGINT UNSIGNED")
	private long createdAt; // TODO somehow improve long -> unsigned long
	@Column(name = "updated_at", columnDefinition = "BIGINT UNSIGNED")
	private long updatedAt; // TODO somehow improve long -> unsigned long

	public Post(String title, String mediaSource) {
		this.title = title;
		this.mediaSource = mediaSource;
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

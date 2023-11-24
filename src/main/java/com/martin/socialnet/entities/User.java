package com.martin.socialnet.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
	@Column(name = "created_at", columnDefinition = "BIGINT UNSIGNED")
	private final long createdAt = System.currentTimeMillis(); // TODO somehow improve long -> unsigned long
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT UNSIGNED")
	private long id;
	@Column(unique = true, length = 50)
	private String username;
	@Column(length = 60)
	private String password;
	@Column(unique = true)
	private String email;
	@ManyToMany(mappedBy = "likedByUsers")
	private List<Post> likedPosts = new ArrayList<>();
	@Column(name = "first_name", length = 30)
	private String firstName;
	@Column(name = "last_name", length = 50)
	private String lastName;
	@Column(name = "profile_picture_URL")
	private String profilePictureURL;
	private LocalDate birthday;
	private String location;
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();
	@Column(name = "updated_at", columnDefinition = "BIGINT UNSIGNED")
	private long updatedAt; // TODO somehow improve long -> unsigned long

	public User(String username, String password) {
		// constructor for creating a profile
		this.username = username;
		this.password = password;
	}

	public User() {

	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public void addPost(Post post) {
		this.posts.add(post);
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProfilePictureURL() {
		return profilePictureURL;
	}

	public void setProfilePictureURL(String profilePictureURL) {
		this.profilePictureURL = profilePictureURL;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}
}

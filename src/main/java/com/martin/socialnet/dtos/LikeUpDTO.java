package com.martin.socialnet.dtos;

import com.martin.socialnet.entities.Post;
import com.martin.socialnet.entities.User;

public record LikeUpDTO(Post post, User user) {
}

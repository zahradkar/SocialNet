package com.martin.socialnet.dtos;

import com.martin.socialnet.entities.Post;

import java.util.List;

public record VotesResponseDTO(List<Long> upVotedIds, List<Long> downVotedIds) {
}

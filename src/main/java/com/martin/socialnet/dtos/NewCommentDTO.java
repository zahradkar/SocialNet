package com.martin.socialnet.dtos;

import jakarta.validation.constraints.NotBlank;

public record NewCommentDTO(@NotBlank String comment) {
}

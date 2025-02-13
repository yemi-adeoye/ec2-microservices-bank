package com.yemiadeoye.banks_ms.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record AccountRequestDto(
                @NotBlank String userId,
                @NotBlank String accountType,
                @NotBlank String createdBy

) {
}

package com.yemiadeoye.banks_ms.dtos.response;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record AccountResponseDto(
                @NotNull String userId,
                @NotNull BigDecimal accountBalance,
                @NotNull BigDecimal effectiveAccountBalance,
                @NotNull String accountNumber,
                @NotNull String accountType

) {
}

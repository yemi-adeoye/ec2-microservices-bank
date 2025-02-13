package com.yemiadeoye.banks_ms.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionRequestDto(
                @NotBlank String initiator,
                @NotNull @Min(0) BigDecimal amount,
                @NotBlank String beneficiary,
                @NotBlank String transactionDescription,
                @NotBlank String transactedBy,
                String initiatorAccount,
                @NotBlank String beneficiaryAccount,
                String comment) {
}
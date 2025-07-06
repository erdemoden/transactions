package com.woh.transactions.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDto {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String userId;
    private BigDecimal amount;
}

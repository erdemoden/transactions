package com.woh.transactions.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountUpdateCreateDto {
    private String userId;
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
}

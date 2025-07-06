package com.woh.transactions.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRegisterDto {
    private String email;
    private String userName;
    private String password;
}

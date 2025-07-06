package com.woh.transactions.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorSuccess {
    private String message;
    private boolean success;
    private Object data;
}

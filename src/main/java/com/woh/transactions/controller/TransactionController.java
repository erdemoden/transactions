package com.woh.transactions.controller;

import com.woh.transactions.dto.ErrorSuccess;
import com.woh.transactions.dto.TransferDto;
import com.woh.transactions.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ErrorSuccess transferFunds(@RequestBody TransferDto transferRequest) {
        return transactionService.transferFunds(transferRequest);
    }
    @GetMapping("account/{accountId}")
    public ErrorSuccess getTransactionHistory(@PathVariable String accountId) {
        return transactionService.getTransactionHistory(accountId);
    }
}

package com.woh.transactions.controller;

import com.woh.transactions.dto.AccountUpdateCreateDto;
import com.woh.transactions.dto.ErrorSuccess;
import com.woh.transactions.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ErrorSuccess createAccount(@RequestBody AccountUpdateCreateDto accountUpdateCreateDto) {
        return accountService.createAccount(accountUpdateCreateDto);
    }
    @PutMapping("/{id}")
    public ErrorSuccess updateAccount(@PathVariable String id, @RequestBody AccountUpdateCreateDto accountUpdateCreateDto) {
        return accountService.updateAccount(id, accountUpdateCreateDto);
    }
    @DeleteMapping("/{id}")
    public ErrorSuccess deleteAccount(@PathVariable String id) {
        return accountService.deleteAccount(id);
    }
}

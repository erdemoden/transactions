package com.woh.transactions.services;

import com.woh.transactions.dto.AccountUpdateCreateDto;
import com.woh.transactions.dto.ErrorSuccess;
import com.woh.transactions.entity.Account;
import com.woh.transactions.entity.TransferUser;
import com.woh.transactions.repository.AccountRepository;
import com.woh.transactions.repository.TransferUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransferUserRepository transferUserRepository;

    public ErrorSuccess createAccount(AccountUpdateCreateDto accountUpdateCreateDto) {
        Optional<TransferUser> transferUser = transferUserRepository.findById(UUID.fromString(accountUpdateCreateDto.getUserId()));
        if (transferUser.isEmpty()) {
            return ErrorSuccess.builder()
                    .success(false)
                    .data(null)
                    .message("User not found")
                    .build();
        }
        boolean exist = accountRepository.existsByUserAndNumberAndName(
                transferUser.get(),
                accountUpdateCreateDto.getAccountNumber(),
                accountUpdateCreateDto.getAccountName()
        );
        if (exist) {
            return ErrorSuccess.builder()
                    .success(false)
                    .data(null)
                    .message("Account already exists")
                    .build();
        }
        Account account = new Account();
        account.setUser(transferUser.get());
        account.setNumber(accountUpdateCreateDto.getAccountNumber());
        account.setName(accountUpdateCreateDto.getAccountName());
        account.setBalance(accountUpdateCreateDto.getBalance());

       return ErrorSuccess.builder()
                .success(true)
                .data(accountRepository.save(account))
                .message("Account created successfully")
                .build();
    }
    public ErrorSuccess deleteAccount(String id){
        Optional<Account> account = accountRepository.findById(UUID.fromString(id));
        if (account.isEmpty()) {
            return ErrorSuccess.builder()
                    .success(false)
                    .data(null)
                    .message("Account not found")
                    .build();
        }
        accountRepository.delete(account.get());
        return ErrorSuccess.builder()
                .success(true)
                .data(null)
                .message("Account deleted successfully")
                .build();
    }
    public ErrorSuccess updateAccount(String id, AccountUpdateCreateDto accountUpdateCreateDto) {
        Optional<Account> account = accountRepository.findById(UUID.fromString(id));
        if (account.isEmpty()) {
            return ErrorSuccess.builder()
                    .success(false)
                    .data(null)
                    .message("Account not found")
                    .build();
        }
        account.get().setName(accountUpdateCreateDto.getAccountName());
        account.get().setNumber(accountUpdateCreateDto.getAccountNumber());
        account.get().setBalance(accountUpdateCreateDto.getBalance());
        return ErrorSuccess.builder()
                .success(true)
                .data(accountRepository.save(account.get()))
                .message("Account updated successfully")
                .build();
    }
}

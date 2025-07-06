package com.woh.transactions.services;

import com.woh.transactions.dto.ErrorSuccess;
import com.woh.transactions.dto.TransferDto;
import com.woh.transactions.entity.Account;
import com.woh.transactions.entity.Transaction;
import com.woh.transactions.enums.TransactionStatus;
import com.woh.transactions.repository.AccountRepository;
import com.woh.transactions.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionLogService transactionLogService;

    @Transactional
    public ErrorSuccess transferFunds(TransferDto transferDto) {
        Transaction transaction = new Transaction();
        try {
            Optional<Account> fromAccount = accountRepository.findByIdForUpdate(UUID.fromString(transferDto.getFromAccountNumber()));
            Optional<Account> toAccount = accountRepository.findByIdForUpdate(UUID.fromString(transferDto.getToAccountNumber()));
            if (fromAccount.isEmpty() || toAccount.isEmpty()) {
                return ErrorSuccess.builder()
                        .success(false)
                        .message("One or both accounts not found")
                        .build();
            }
            if (fromAccount.get().getBalance().compareTo(transferDto.getAmount()) <= 0) {
                return ErrorSuccess.builder()
                        .success(false)
                        .message("Insufficient funds in the source account")
                        .build();
            }
            transaction.setFromAccount(fromAccount.get());
            transaction.setToAccount(toAccount.get());
            transaction.setAmount(transferDto.getAmount());
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction = transactionRepository.save(transaction);
            fromAccount.get().setBalance(fromAccount.get().getBalance().subtract(transferDto.getAmount()));
            toAccount.get().setBalance(toAccount.get().getBalance().add(transferDto.getAmount()));
            accountRepository.save(fromAccount.get());
            accountRepository.save(toAccount.get());
            return ErrorSuccess.builder()
                    .success(true)
                    .data(transaction)
                    .message("Funds transferred successfully")
                    .build();
        } catch (Exception e) {
            transactionLogService.saveFailedTransaction(transaction);
            return ErrorSuccess.builder()
                    .success(false)
                    .message("An error occurred during the transfer: " + e.getMessage())
                    .build();
        }
    }
    public ErrorSuccess getTransactionHistory(String accountId) {
        try {
            UUID accountUUID = UUID.fromString(accountId);
            Optional<Account> account = accountRepository.findById(accountUUID);
            if (account.isEmpty()) {
                return ErrorSuccess.builder()
                        .success(false)
                        .message("Account not found")
                        .build();
            }
            List<Transaction> transactions = transactionRepository.findWithAccountsByFromAccount(account.get());
            if (transactions.isEmpty()) {
                return ErrorSuccess.builder()
                        .success(false)
                        .message("No transactions found for this account")
                        .build();
            }

            return ErrorSuccess.builder()
                    .success(true)
                    .data(transactions)
                    .message("Transaction history retrieved successfully")
                    .build();
        } catch (Exception e) {
            return ErrorSuccess.builder()
                    .success(false)
                    .message("An error occurred while retrieving transaction history: " + e.getMessage())
                    .build();
        }
    }
}

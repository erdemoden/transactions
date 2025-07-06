package com.woh.transactions.services;

import com.woh.transactions.entity.Transaction;
import com.woh.transactions.enums.TransactionStatus;
import com.woh.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(transaction);
    }
}

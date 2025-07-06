package com.woh.transactions.repository;

import com.woh.transactions.entity.Account;
import com.woh.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {



    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.fromAccount fa " +
            "JOIN FETCH t.toAccount ta " +
            "WHERE fa = :fromAccount")
    List<Transaction> findWithAccountsByFromAccount(@Param("fromAccount") Account fromAccount);
}

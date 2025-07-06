package com.woh.transactions.entity;

import com.woh.transactions.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account")
    private Account toAccount;

    private BigDecimal amount;

    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}

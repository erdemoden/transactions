package com.woh.transactions.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String number;
    private String name;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany
    @JoinColumn(name = "from_account")
    private List<Transaction> sentTransactions;
    @OneToMany
    @JoinColumn(name = "to_account")
    private List<Transaction> receivedTransactions;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private TransferUser user;
}

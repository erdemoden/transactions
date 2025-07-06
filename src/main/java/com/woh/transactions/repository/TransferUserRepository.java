package com.woh.transactions.repository;

import com.woh.transactions.entity.TransferUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferUserRepository extends JpaRepository<TransferUser, UUID> {

    TransferUser findByEmail(String email);
}

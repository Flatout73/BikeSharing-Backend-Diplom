package ru.hse.BikeSharing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.BikeSharing.domain.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {


}

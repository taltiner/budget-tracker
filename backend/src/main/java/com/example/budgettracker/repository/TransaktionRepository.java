package com.example.budgettracker.repository;

import com.example.budgettracker.model.Transaktion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaktionRepository extends CrudRepository<Transaktion, Long> {
}

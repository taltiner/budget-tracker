package com.example.budgettracker.repository;

import com.example.budgettracker.model.TransaktionAusgabe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaktionAusgabeRepository extends CrudRepository<TransaktionAusgabe, Long> {
}

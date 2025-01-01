package com.example.budgettracker.repository;

import com.example.budgettracker.model.TransaktionEinnahme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaktionEinnahmeRepository extends CrudRepository<TransaktionEinnahme, Long> {
}

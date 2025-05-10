package com.finance.PersonalFinanceManager.repository;

import com.finance.PersonalFinanceManager.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}

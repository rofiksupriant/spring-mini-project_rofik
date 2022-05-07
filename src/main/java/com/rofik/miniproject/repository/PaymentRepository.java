package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByNameAndIdNot(String name, Long id);

    Optional<Payment> findByName(String name);
}
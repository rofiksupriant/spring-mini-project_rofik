package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
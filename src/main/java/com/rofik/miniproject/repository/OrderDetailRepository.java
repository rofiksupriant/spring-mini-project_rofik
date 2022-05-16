package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {
    Optional<Table> findByNumber(Integer number);

    Optional<Table> findByUuid(String uuid);
}
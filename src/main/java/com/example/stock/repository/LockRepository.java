package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    public void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    public void releaseLock(String key);
}

package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.DefaultStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final DefaultStockService defaultStockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {

        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            defaultStockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}

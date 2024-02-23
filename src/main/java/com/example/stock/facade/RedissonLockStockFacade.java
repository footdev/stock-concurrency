package com.example.stock.facade;

import com.example.stock.service.DefaultStockService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final DefaultStockService defaultStockService;

    public void decrease(Long id, Long quantity) {
        RLock rLock = redissonClient.getLock(id.toString());

        try {
            boolean available = rLock.tryLock(15, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패 !");
                return;
            }

            defaultStockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
        }
    }

}

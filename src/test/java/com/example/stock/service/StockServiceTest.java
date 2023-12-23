package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private DefaultStockService defaultStockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        System.out.println("픽스처 생성");
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        System.out.println("픽스처 삭제");
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("재고가 감소된다.")
    void decrease() {

        //given, when
        defaultStockService.decrease(1L, 1L);
        Stock stock = stockRepository.findById(1L).orElseThrow();

        //then
        assertThat(stock.getQuantity()).isEqualTo(99);
    }

    @Test
    @DisplayName("100개의 재고 감소 요청이 동시에 들어와 100개의 요청이 실패한다.")
    void decreaseConcurrency() throws InterruptedException {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertThat(stock.getId()).isEqualTo(1L);
        // given
        int threadCnt = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCnt);

        // when
        for (int i = 0; i < threadCnt; i++) {
            executorService.submit(() -> {
                try {
                    defaultStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        stock = stockRepository.findById(1L).orElseThrow();
        assertThat(stock.getQuantity()).isNotZero();
    }
}
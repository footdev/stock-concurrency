package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SynchronizedStockService implements StockService{

    private final StockRepository stockRepository;

    @Override
    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        System.out.println("start transaction");

        //Stock 조회
        Stock stock = stockRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        //재고감소
        stock.decrease(quantity);

        //변경된 값 저장
        stockRepository.saveAndFlush(stock);

        System.out.println("end transaction");
    }
}

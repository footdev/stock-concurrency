package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultStockService implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long decrease(Long id, Long quantity) {
        //Stock 조회
        Stock stock = stockRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        //재고감소
        stock.decrease(quantity);

        //변경된 값 저장
        stockRepository.saveAndFlush(stock);

        return stock.getQuantity();
    }
}

package com.jeremiasAvero.app.order.application;


import com.jeremiasAvero.app.order.domain.OrderNumberSeq;
import com.jeremiasAvero.app.order.infraestructure.out.OrderNumberSeqRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
public class OrderNumberService {
    private final OrderNumberSeqRepository repo;
    public OrderNumberService(OrderNumberSeqRepository repo){ this.repo = repo; }

    @Transactional
    public String nextNumber() {
        Long seq = repo.saveAndFlush(new OrderNumberSeq()).getId(); // autoincrement
        String year = String.valueOf(Year.now().getValue());
        return "ORD-" + year + "-" + String.format("%06d", seq);
    }
}
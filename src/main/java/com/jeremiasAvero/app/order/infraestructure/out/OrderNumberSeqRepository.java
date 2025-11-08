package com.jeremiasAvero.app.order.infraestructure.out;


import com.jeremiasAvero.app.order.domain.OrderNumberSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderNumberSeqRepository extends JpaRepository<OrderNumberSeq, Long> {}

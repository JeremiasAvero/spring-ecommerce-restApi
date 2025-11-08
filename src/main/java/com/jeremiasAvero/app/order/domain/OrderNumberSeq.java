package com.jeremiasAvero.app.order.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_number_seq")
public class OrderNumberSeq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() { return id; }
}
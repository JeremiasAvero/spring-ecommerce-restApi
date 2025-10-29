package com.jeremiasAvero.app.brand.domain;


import com.jeremiasAvero.app.products.domain.ProductEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "brands")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<ProductEntity> product;

    public BrandEntity(){}

    //GETTER, SETTER
    public String getName(){
        return this.name;
    }
    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }

}

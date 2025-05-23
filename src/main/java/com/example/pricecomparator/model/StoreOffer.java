package com.example.pricecomparator.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "store_offers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    private String store;
    private Double packageQuantity;
    private String packageUnit;
    private Double price;
    private String currency;
    private LocalDate date;
}

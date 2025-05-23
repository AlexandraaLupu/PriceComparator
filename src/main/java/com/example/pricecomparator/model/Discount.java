package com.example.pricecomparator.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String store;
    
    private Double quantity;
    private String packageUnit;

    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer percentageOfDiscount;




}

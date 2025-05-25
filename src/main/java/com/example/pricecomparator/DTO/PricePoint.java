package com.example.pricecomparator.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PricePoint {
    private String productId;
    private String store;
    private LocalDate date;
    private double price;

}

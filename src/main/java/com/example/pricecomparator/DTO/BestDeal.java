package com.example.pricecomparator.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestDeal {
    private String productId;
    private String store;
    private double finalPrice;
    private double quantity;
    private String unit;
    private double valuePerUnit;
}

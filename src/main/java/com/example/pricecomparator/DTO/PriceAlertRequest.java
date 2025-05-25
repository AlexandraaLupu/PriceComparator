package com.example.pricecomparator.DTO;

import lombok.Data;

@Data
public class PriceAlertRequest {
    private Long userId;
    private String productId;
    private double targetPrice;
}

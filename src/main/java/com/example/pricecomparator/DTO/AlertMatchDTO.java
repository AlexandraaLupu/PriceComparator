package com.example.pricecomparator.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertMatchDTO {
    private Long alertId;
    private String productId;
    private double targetPrice;
    private double finalPrice;
    private String store;
}

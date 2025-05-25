package com.example.pricecomparator.controller;

import com.example.pricecomparator.DTO.BestDeal;
import com.example.pricecomparator.DTO.PricePoint;
import com.example.pricecomparator.service.PriceTrendService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("trend/")
public class PriceTrendController {
    private final PriceTrendService priceTrendService;

    public PriceTrendController(PriceTrendService priceTrendService) {
        this.priceTrendService = priceTrendService;
    }

    @GetMapping("/product/{productId}/range")
    public List<PricePoint> getProductPriceHistory(
            @PathVariable String productId,
            @RequestParam(value = "store", required = false) String store,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return priceTrendService.getProductPriceHistory(productId, store, startDate, endDate);
    }

    @GetMapping("/best-deals")
    public List<BestDeal> getBestDeals(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return priceTrendService.getBestDealsByProductName(date);
    }

}

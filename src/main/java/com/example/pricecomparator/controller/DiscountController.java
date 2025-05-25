package com.example.pricecomparator.controller;

import com.example.pricecomparator.model.Discount;
import com.example.pricecomparator.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {
    @Autowired
    private DiscountService discountService;

    @GetMapping("/{store}")
    public List<Discount> getDiscount(@PathVariable("store") String store) {
        return discountService.getByStore(store);
    }

    @GetMapping("/top")
    public List<Discount> getTopCurrentDiscounts(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return discountService.getTopCurrentDiscounts(date);
    }

    @GetMapping("/recent")
    public List<Discount> getRecentlyAddedDiscounts(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return discountService.getRecentlyAddedDiscounts(date);
    }

}

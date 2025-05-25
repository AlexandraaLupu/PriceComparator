package com.example.pricecomparator.controller;

import com.example.pricecomparator.DTO.AlertMatchDTO;
import com.example.pricecomparator.DTO.PriceAlertRequest;
import com.example.pricecomparator.service.PriceAlertService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("alert/")
public class PriceAlertController {
    private final PriceAlertService priceAlertService;

    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @PostMapping
    public String createAlert(@RequestBody PriceAlertRequest request) {
        priceAlertService.saveAlert(request.getUserId(), request.getProductId(), request.getTargetPrice());
        return "Alert created successfully.";
    }

    @GetMapping
    public List<AlertMatchDTO> getMatchedAlerts(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return priceAlertService.getMatchedAlerts(userId, date);
    }
}

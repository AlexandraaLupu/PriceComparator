package com.example.pricecomparator.controller;

import com.example.pricecomparator.DTO.OptimizeBasketRequest;
import com.example.pricecomparator.model.StoreOffer;
import com.example.pricecomparator.service.StoreOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offers")
public class StoreOfferController {
    private final StoreOfferService storeOfferService;

    public StoreOfferController(StoreOfferService storeOfferService) {
        this.storeOfferService = storeOfferService;
    }

    @GetMapping("/{store}")
    public List<StoreOffer> getStoreOffers(@PathVariable String store) {
        return storeOfferService.getByStore(store);
    }

    @PostMapping("/optimize")
    public Map<String, List<StoreOffer>> optimizeBasket(
            @RequestBody OptimizeBasketRequest request) {
        return storeOfferService.optimizeBasket(request.getProductIds(), request.getDate());
    }


}

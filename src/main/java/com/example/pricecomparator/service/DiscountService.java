package com.example.pricecomparator.service;

import com.example.pricecomparator.model.Discount;
import com.example.pricecomparator.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<Discount> getByStore(String store) {
        return discountRepository.findByStore(store);
    }

    public List<Discount> getRecentlyAddedDiscounts(LocalDate date) {
        LocalDate dayBefore = date.minusDays(1);
        return discountRepository.findDiscountsAddedRecently(date, dayBefore);
    }
}

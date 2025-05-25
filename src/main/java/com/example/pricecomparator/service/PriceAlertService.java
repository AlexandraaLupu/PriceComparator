package com.example.pricecomparator.service;

import com.example.pricecomparator.DTO.AlertMatchDTO;
import com.example.pricecomparator.model.Discount;
import com.example.pricecomparator.model.PriceAlert;
import com.example.pricecomparator.model.Product;
import com.example.pricecomparator.model.StoreOffer;
import com.example.pricecomparator.repository.DiscountRepository;
import com.example.pricecomparator.repository.PriceAlertRepository;
import com.example.pricecomparator.repository.ProductRepository;
import com.example.pricecomparator.repository.StoreOfferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceAlertService {
    private final PriceAlertRepository priceAlertRepository;
    private final ProductRepository productRepository;
    private final StoreOfferRepository storeOfferRepository;
    private final DiscountRepository discountRepository;

    public PriceAlertService(PriceAlertRepository priceAlertRepository, ProductRepository productRepository, StoreOfferRepository storeOfferRepository, DiscountRepository discountRepository) {
        this.priceAlertRepository = priceAlertRepository;
        this.productRepository = productRepository;
        this.storeOfferRepository = storeOfferRepository;
        this.discountRepository = discountRepository;
    }


    public void saveAlert(Long userId, String productId, double targetPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        priceAlertRepository.save(new PriceAlert(null, userId, product, targetPrice));
    }

    public List<AlertMatchDTO> getMatchedAlerts(Long userId, LocalDate date) {
        // get all the alerts from a user and parse them
        // for every alert, get the latest offers for a product at a given date
        // parse the offers
        // for every offer, check if there is discount and compute the price
        // if the price is lower or equal than the target, add to the result list
        List<PriceAlert> alerts = priceAlertRepository.findByUserId(userId);
        List<AlertMatchDTO> matches = new ArrayList<>();

        for (PriceAlert alert : alerts) {
            String productId = alert.getProduct().getId();

            // get all the offers with max date but less than the param date
            List<StoreOffer> offers = storeOfferRepository.findLatestOffersForProductGroupedByStoreBeforeDate(productId, date);

            for (StoreOffer offer : offers) {
                double price = offer.getPrice();

                List<Discount> discounts = discountRepository.findActiveDiscount(
                        productId, offer.getStore(), date);

                if (!discounts.isEmpty()) {
                    price -= price * discounts.get(0).getPercentageOfDiscount() / 100.0;
                }

                if (price <= alert.getTargetPrice()) {
                    matches.add(new AlertMatchDTO(
                            alert.getId(),
                            productId,
                            alert.getTargetPrice(),
                            round(price),
                            offer.getStore()
                    ));
                }
            }


        }

        return matches;
    }

    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}

package com.example.pricecomparator.service;

import com.example.pricecomparator.DTO.BestDeal;
import com.example.pricecomparator.DTO.PricePoint;
import com.example.pricecomparator.model.Discount;
import com.example.pricecomparator.model.StoreOffer;
import com.example.pricecomparator.repository.DiscountRepository;
import com.example.pricecomparator.repository.StoreOfferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PriceTrendService {
    private final DiscountRepository discountRepository;
    private final StoreOfferRepository storeOfferRepository;

    public PriceTrendService(DiscountRepository discountRepository, StoreOfferRepository storeOfferRepository) {
        this.discountRepository = discountRepository;
        this.storeOfferRepository = storeOfferRepository;
    }

    public List<PricePoint> getProductPriceHistory(
            String productId, String store, LocalDate startDate, LocalDate endDate) {
        // get all offers by product id in a certain range
        // the store is optional
        // for every offer get the price and compute the discount if any
        // add a point with the price to the result list
        List<StoreOffer> offers = storeOfferRepository.findOffersByProductAndDateRange(
                productId, store, startDate, endDate);

        List<PricePoint> dataPoints = new ArrayList<>();
        for (StoreOffer offer : offers) {
            double originalPrice = offer.getPrice();
            List<Discount> activeDiscounts = discountRepository.findActiveDiscount(
                    productId, offer.getStore(), offer.getDate());

            Discount bestDiscount = activeDiscounts.isEmpty() ? null : activeDiscounts.get(0);

            double finalPrice = (bestDiscount != null)
                    ? originalPrice - originalPrice * bestDiscount.getPercentageOfDiscount() / 100.0
                    : originalPrice;

            dataPoints.add(new PricePoint(
                    offer.getProduct().getId(),
                    offer.getStore(),
                    offer.getDate(),
                    finalPrice
            ));
        }

        return dataPoints;
    }

    public List<BestDeal> getBestDealsByProductName(LocalDate date) {
        // i saw that a product has the same quantity and unit but the name is different for different products
        // so i assumed that you can substitute the products that have the same name based on the base
        // price per unit

        // get all offers before a date
        // for every offer get the price and compute the discount id any
        // if there is no product with the name of the offer, add an entry in the map
        // else update the best price by unit
        List<StoreOffer> offers = storeOfferRepository.findLatestOffersBeforeDate(date);

        Map<String, BestDeal> bestByName = new HashMap<>();

        for (StoreOffer offer : offers) {
            double originalPrice = offer.getPrice();

            List<Discount> activeDiscounts = discountRepository.findActiveDiscount(
                    offer.getProduct().getId(),
                    offer.getStore(),
                    offer.getDate());

            Discount bestDiscount = activeDiscounts.isEmpty() ? null : activeDiscounts.get(0); // get the last discount or no discount

            double finalPrice = (bestDiscount != null)
                    ? originalPrice - originalPrice * bestDiscount.getPercentageOfDiscount() / 100.0
                    : originalPrice;

            double unitValue = finalPrice / offer.getProduct().getPackageQuantity();

            // rounding the doubles to precision 2
            BigDecimal fp = new BigDecimal(finalPrice);
            BigDecimal uv = new BigDecimal(unitValue);
            MathContext m = new MathContext(2);
            fp = fp.round(m);
            uv = uv.round(m);

            String name = offer.getProduct().getName();

            BestDeal current = bestByName.get(name);
            if (current == null || uv.doubleValue() < current.getValuePerUnit()) {
                bestByName.put(name, new BestDeal(
                        offer.getProduct().getId(),
                        offer.getStore(),
                        fp.doubleValue(),
                        offer.getProduct().getPackageQuantity(),
                        offer.getProduct().getPackageUnit(),
                        uv.doubleValue()
                ));
            }
        }
        System.out.println("Best deals: " + bestByName.size());
        return new ArrayList<>(bestByName.values());
    }

}

package com.example.pricecomparator.service;

import com.example.pricecomparator.model.StoreOffer;
import com.example.pricecomparator.repository.StoreOfferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class StoreOfferService {
    private final StoreOfferRepository storeOfferRepository;

    public StoreOfferService(StoreOfferRepository storeOfferRepository) {
        this.storeOfferRepository = storeOfferRepository;
    }

    public List<StoreOffer> getByStore(String store) {
        return storeOfferRepository.findByStore(store);
    }

    public Map<String, List<StoreOffer>> optimizeBasket(List<String> productIds, LocalDate referenceDate) {
        //  for every product id in the list of product ids provided by the user
        // find the closest date of the offer for that product; the result can be a list if multiple stores have that product
        // get the storeOffer with the smallest price from the offers
        // add to the map the store and the list of products
        Map<String, List<StoreOffer>> storeToOffersMap = new HashMap<>();

        for (String productId : productIds) {
            List<StoreOffer> offers = storeOfferRepository.findOffersForProductAtClosestDate(productId, referenceDate);
            if (offers.isEmpty()) continue;

            StoreOffer cheapest = offers.stream()
                    .min(Comparator.comparingDouble(StoreOffer::getPrice))
                    .get();

            storeToOffersMap
                    .computeIfAbsent(cheapest.getStore(), k -> new ArrayList<>())
                    .add(cheapest);
        }

        return storeToOffersMap;
    }
}

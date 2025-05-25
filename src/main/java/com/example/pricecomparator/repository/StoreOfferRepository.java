package com.example.pricecomparator.repository;

import com.example.pricecomparator.model.StoreOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StoreOfferRepository extends JpaRepository<StoreOffer, Integer> {
    List<StoreOffer> findByStore(String store);
    @Query("""
        SELECT so FROM StoreOffer so
        WHERE so.product.id = :productId
          AND so.date = (
              SELECT MAX(innerSo.date) FROM StoreOffer innerSo
              WHERE innerSo.product.id = :productId AND innerSo.date <= :referenceDate
          )
    """)
    List<StoreOffer> findOffersForProductAtClosestDate(String productId, LocalDate referenceDate);
}

package com.example.pricecomparator.repository;

import com.example.pricecomparator.model.StoreOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
    SELECT so FROM StoreOffer so
    WHERE so.product.id = :productId
      AND so.date BETWEEN :startDate AND :endDate
      AND (:store IS NULL OR so.store = :store)
    ORDER BY so.date
""")
    List<StoreOffer> findOffersByProductAndDateRange(
            String productId,
            String store,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
    SELECT so FROM StoreOffer so
    WHERE so.date = (
        SELECT MAX(innerSo.date) FROM StoreOffer innerSo
        WHERE innerSo.product.id = so.product.id
          AND innerSo.date <= :date
    )
""")
    List<StoreOffer> findLatestOffersBeforeDate(LocalDate date);

    @Query("""
    SELECT so FROM StoreOffer so
    WHERE so.date = (
        SELECT MAX(innerSo.date) FROM StoreOffer innerSo
        WHERE innerSo.product.id = :productId
          AND innerSo.store = so.store
          AND innerSo.date <= :date
    )
    AND so.product.id = :productId
""")
    List<StoreOffer> findLatestOffersForProductGroupedByStoreBeforeDate(String productId, LocalDate date);
}

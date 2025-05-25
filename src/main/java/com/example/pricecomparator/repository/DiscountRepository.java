package com.example.pricecomparator.repository;

import com.example.pricecomparator.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStore(String store);

    @Query("""
        SELECT d FROM Discount d
        WHERE d.fromDate = :date OR d.fromDate = :dayBefore
        ORDER BY d.fromDate DESC
    """)
    List<Discount> findDiscountsAddedRecently(LocalDate date, LocalDate dayBefore);

    @Query("""
    SELECT d FROM Discount d
    WHERE d.product.id = :productId
      AND d.store = :store
      AND :date BETWEEN d.fromDate AND d.toDate
    ORDER BY d.fromDate DESC
    """)
    List<Discount> findActiveDiscount(String productId, String store, LocalDate date);
}

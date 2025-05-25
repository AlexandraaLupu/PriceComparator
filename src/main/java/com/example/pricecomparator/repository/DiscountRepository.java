package com.example.pricecomparator.repository;

import com.example.pricecomparator.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByStore(String store);
    @Query("""
        SELECT DISTINCT(d)
        FROM Discount d
        WHERE :today BETWEEN d.fromDate AND d.toDate
        ORDER BY d.percentageOfDiscount DESC
    """)
    List<Discount> findCurrentDiscountsOrdered(LocalDate today);
}

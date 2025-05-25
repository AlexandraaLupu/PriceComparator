package com.example.pricecomparator.repository;

import com.example.pricecomparator.model.PriceAlert;
import com.example.pricecomparator.model.StoreOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByUserId(Long userId);
}

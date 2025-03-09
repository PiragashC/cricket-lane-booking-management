package com.cricket.lane.booking.management.repository;

import com.cricket.lane.booking.management.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromoCodeRepository extends JpaRepository<PromoCode,String> {

    @Query("SELECT p.promoCode FROM PromoCode p " +
            "WHERE p.isActive = true")
    String getPromoCode();

    @Query("SELECT p FROM PromoCode p " +
            "WHERE p.isActive = true")
    PromoCode getPromoCodeToCalculatePrice();
}

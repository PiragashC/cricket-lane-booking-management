package com.cricket.lane.booking.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Data
public class PromoCode {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String promoCode;
    private Boolean isActive;
    private BigDecimal discount;
}

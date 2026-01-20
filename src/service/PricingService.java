package service;

import entity.Tariff;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class PricingService {

    public BigDecimal calculateCost(Tariff tariff, LocalDateTime start, LocalDateTime end) {
        long hours = Duration.between(start, end).toHours();
        if (hours == 0) hours = 1;

        return tariff.getPricePerHour().multiply(BigDecimal.valueOf(hours));
    }
}
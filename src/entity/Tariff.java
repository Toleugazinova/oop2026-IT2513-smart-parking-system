package entity;

import java.math.BigDecimal;

public class Tariff {
    private int id;
    private String spotType;
    private BigDecimal pricePerHour;

    public Tariff(int id, String spotType, BigDecimal pricePerHour) {
        this.id = id;
        this.spotType = spotType;
        this.pricePerHour = pricePerHour;
    }

    public int getId() { return id; }
    public String getSpotType() { return spotType; }
    public BigDecimal getPricePerHour() { return pricePerHour; }
}
package entity;

public class Tariff {
    private int id;
    private String spotType;
    private double pricePerHour;

    public Tariff(int id, String spotType, double pricePerHour) {
        this.id = id;
        this.spotType = spotType;
        this.pricePerHour = pricePerHour;
    }

    public int getId() { return id; }
    public double getPricePerHour() { return pricePerHour; }

    @Override
    public String toString() {
        return "Type: " + spotType + " | Price: " + pricePerHour + " tg/hour";
    }
}
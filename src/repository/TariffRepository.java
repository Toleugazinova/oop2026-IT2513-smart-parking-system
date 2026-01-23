package repository;

import db.IDatabase;
import entity.Tariff;

import java.sql.*;

public class TariffRepository {

    private final IDatabase db;

    public TariffRepository(IDatabase db) {
        this.db = db;
    }

    public void printAllTariffs() {
        String sql = "SELECT spot_type, price_per_hour FROM tariffs";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Tariffs ---");
            while (rs.next()) {
                System.out.println(rs.getString("spot_type") + " - " +
                        rs.getDouble("price_per_hour") + " KZT/hour");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getPriceByType(String type) {
        String sql = "SELECT price_per_hour FROM tariffs WHERE spot_type = ?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble("price_per_hour");

            throw new RuntimeException("Tariff not found");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
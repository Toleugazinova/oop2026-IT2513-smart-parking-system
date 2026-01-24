package repository;

import db.IDatabase;
import entity.Tariff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TariffRepository {

    private final IDatabase db;

    public TariffRepository(IDatabase db) {
        this.db = db;
    }

    public Tariff findBySpotType(String spotType) {
        String sql = "SELECT * FROM tariffs WHERE spot_type = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, spotType);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Tariff(
                        rs.getInt("id"),
                        rs.getString("spot_type"),
                        rs.getBigDecimal("price_per_hour")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

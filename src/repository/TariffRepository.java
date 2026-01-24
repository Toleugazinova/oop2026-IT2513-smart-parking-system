package repository;

import db.IDatabase;
import entity.Tariff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TariffRepository {
    private final IDatabase db;

    public TariffRepository(IDatabase db) {
        this.db = db;
    }

    public List<Tariff> getAllTariffs() {
        List<Tariff> tariffs = new ArrayList<>();
        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tariffs")) {
            while (rs.next()) {
                tariffs.add(new Tariff(rs.getInt("id"), rs.getString("spot_type"), rs.getDouble("price_per_hour")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tariffs;
    }

    public Tariff getTariffBySpotType(String spotType) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM tariffs WHERE spot_type = ?")) {
            st.setString(1, spotType);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Tariff(rs.getInt("id"), rs.getString("spot_type"), rs.getDouble("price_per_hour"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public double getPriceById(int id) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement("SELECT price_per_hour FROM tariffs WHERE id = ?")) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getDouble("price_per_hour");
        } catch (Exception e) { e.printStackTrace(); }
        return 0.0;
    }
}
package repository;

import db.IDatabase;
import entity.ParkingSpot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotRepository {
    private final IDatabase db;

    public ParkingSpotRepository(IDatabase db) {
        this.db = db;
    }

    public List<ParkingSpot> getAllSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM parking_spots ORDER BY id")) {
            while (rs.next()) {
                spots.add(new ParkingSpot(
                        rs.getInt("id"),
                        rs.getString("spot_number"),
                        rs.getBoolean("is_available"),
                        rs.getString("spot_type")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return spots;
    }

    public ParkingSpot findFirstAvailable() {
        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM parking_spots WHERE is_available = true LIMIT 1")) {
            if (rs.next()) {
                return new ParkingSpot(
                        rs.getInt("id"),
                        rs.getString("spot_number"),
                        rs.getBoolean("is_available"),
                        rs.getString("spot_type")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void updateStatus(int spotId, boolean isAvailable) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement("UPDATE parking_spots SET is_available = ? WHERE id = ?")) {
            st.setBoolean(1, isAvailable);
            st.setInt(2, spotId);
            st.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}}
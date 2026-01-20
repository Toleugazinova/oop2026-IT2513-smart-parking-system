package repository;

import db.IDatabase;
import entity.ParkingSpot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotRepository {

    private final IDatabase db;

    public ParkingSpotRepository(IDatabase db) {
        if (db == null) {
            throw new NullPointerException();
        }
        else this.db = db;
    }

    public List<ParkingSpot> findFreeSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE is_available = true";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                spots.add(new ParkingSpot(
                        rs.getInt("id"),
                        rs.getString("spot_number"),
                        rs.getBoolean("is_available"),
                        rs.getString("spot_type")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spots;
    }

    public void setAvailability(int spotId, boolean available) {
        String sql = "UPDATE parking_spots SET is_available = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, spotId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package repository;

import db.IDatabase;
import entity.ParkingSpot;
import java.sql.*;
import java.util.List;

public class ParkingSpotRepository {

    private final IDatabase db;

    public ParkingSpotRepository(IDatabase db) {
        this.db = db;
    }

    public List<ParkingSpot> printFreeSpots() {
        String sql = "SELECT spot_number, spot_type FROM parking_spots WHERE is_available = true";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Available Spots ---");
            while (rs.next()) {
                System.out.println("Spot " + rs.getString("spot_number") +
                        " | Type: " + rs.getString("spot_type"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Integer findFreeSpot(String type) {
        String sql = """
            SELECT id FROM parking_spots 
            WHERE is_available = true AND spot_type = ?
            LIMIT 1
        """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt("id");
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAvailability(int spotId, boolean available) {
        String sql = "UPDATE parking_spots SET is_available = ? WHERE id = ?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, spotId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package repository;

import db.IDatabase;
import entity.Vehicle;

import java.sql.*;

public class VehicleRepository {

    private final IDatabase db;

    public VehicleRepository(IDatabase db) {
        this.db = db;
    }

    public Vehicle findByPlate(String plate) {
        String sql = "SELECT * FROM vehicles WHERE plate_number = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("vehicle_type")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


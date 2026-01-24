package repository;

import db.IDatabase;
import entity.Vehicle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

public class VehicleRepository {
    private final IDatabase db;

    public VehicleRepository(IDatabase db) {
        this.db = db;
    }

    public Vehicle findByPlate(String plate) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM vehicles WHERE plate_number = ?")) {
            st.setString(1, plate);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Vehicle(rs.getInt("id"), rs.getString("plate_number"), rs.getString("vehicle_type"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public int createVehicle(String plate, String type) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement("INSERT INTO vehicles (plate_number, vehicle_type) VALUES (?, ?) RETURNING id")) {
            st.setString(1, plate);
            st.setString(2, type);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }
}
package repository;

import db.IDatabase;
import entity.Reservation;
import java.sql.*;

public class ReservationRepository {
    private final IDatabase db;

    public ReservationRepository(IDatabase db) {
        this.db = db;
    }

    public void createReservation(int vehicleId, int spotId, int tariffId) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(
                     "INSERT INTO reservations (vehicle_id, parking_spot_id, tariff_id, start_time, status) VALUES (?, ?, ?, now(), 'Active')")) {
            st.setInt(1, vehicleId);
            st.setInt(2, spotId);
            st.setInt(3, tariffId);
            st.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Reservation findActiveReservation(int vehicleId) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM reservations WHERE vehicle_id = ? AND status = 'Active'")) {
            st.setInt(1, vehicleId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Reservation(
                        rs.getInt("id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("parking_spot_id"),
                        rs.getInt("tariff_id"),
                        rs.getTimestamp("start_time"),
                        null,
                        "Active"
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void closeReservation(int id) {
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement("UPDATE reservations SET end_time = now(), status = 'Finished' WHERE id = ?")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
package repository;

import db.IDatabase;
import entity.Reservation;

import java.sql.*;
import java.time.LocalDateTime;

public class ReservationRepository {

    private final IDatabase db;

    public ReservationRepository(IDatabase db) {
        this.db = db;
    }

    public void startReservation(int spotId, int vehicleId) {
        String sql = """
            INSERT INTO reservations (parking_spot_id, vehicle_id, start_time)
            VALUES (?, ?, NOW())
        """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, spotId);
            ps.setInt(2, vehicleId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getActiveReservation(String plate) throws SQLException {
        String sql = """
            SELECT r.id, r.parking_spot_id, t.price_per_hour
            FROM reservations r
            JOIN vehicles v ON r.vehicle_id = v.id
            JOIN parking_spots p ON r.parking_spot_id = p.id
            JOIN tariffs t ON p.spot_type = t.spot_type
            WHERE v.plate_number = ? AND r.end_time IS NULL
        """;

        Connection c = db.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, plate);
        return ps.executeQuery();
    }

    public void finishReservation(int reservationId) {
        String sql = "UPDATE reservations SET end_time = NOW() WHERE id = ?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
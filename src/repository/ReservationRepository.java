package repository;

import db.IDatabase;
import entity.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class ReservationRepository {

    private final IDatabase db;

    public ReservationRepository(IDatabase db) {
        this.db = db;
    }

    public Reservation findActiveByVehicle(int vehicleId) {
        String sql = """
            SELECT * FROM reservations
            WHERE vehicle_id = ? AND status = 'Active'
        """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Reservation(
                        rs.getInt("id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("parking_spot_id"),
                        rs.getInt("tariff_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        null,
                        rs.getString("status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createReservation(int vehicleId, int spotId, int tariffId) {
        String sql = """
            INSERT INTO reservations (vehicle_id, parking_spot_id, tariff_id)
            VALUES (?, ?, ?)
        """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ps.setInt(2, spotId);
            ps.setInt(3, tariffId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishReservation(int reservationId) {
        String sql = """
            UPDATE reservations
            SET end_time = now(), status = 'Finished'
            WHERE id = ?
        """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

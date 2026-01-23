package repository;
import db.IDatabase;
import entity.Vehicle;
import exception.InvalidVehiclePlateException;
import java.sql.*;

public class VehicleRepository {

    private final IDatabase db;

    public VehicleRepository(IDatabase db) {
        this.db = db;
    }

    public int saveVehicle(String plate, String type) {
        String sql = """
            INSERT INTO vehicles (plate_number, vehicle_type)
            VALUES (?, ?) RETURNING id
        """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, plate);
            ps.setString(2, type);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
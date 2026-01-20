import db.IDatabase;
import edu.aitu.oop3.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.PostgresDB;
import repository.*;
import service.*;

public class Main {
    private static IDatabase db;

    public static void main(String[] args) {
        System.out.println("Connecting to Supabase...");
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Connected successfully!");
            String sql = "SELECT CURRENT_TIMESTAMP";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Database time: " + rs.getTimestamp(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to database:");
            e.printStackTrace();
        }

        ParkingSpotRepository spotRepo = new ParkingSpotRepository(db);
        VehicleRepository vehicleRepo = new VehicleRepository(db);
        TariffRepository tariffRepo = new TariffRepository(db);
        ReservationRepository reservationRepo = new ReservationRepository(db);

        PricingService pricingService = new PricingService();
        ReservationService service = new ReservationService(
                spotRepo, vehicleRepo, tariffRepo, reservationRepo, pricingService
        );

        service.reserveSpot("KZ777ERA");
        service.releaseSpot("KZ777ERA");
    }
}

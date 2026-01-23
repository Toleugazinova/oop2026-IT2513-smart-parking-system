import db.IDatabase;
import db.PostgresDB;
import exception.*;
import repository.*;
import service.*;

import java.sql.ResultSet;
import java.util.Scanner;

public class SmartParkingSystem {

    private final Scanner scanner = new Scanner(System.in);

    private IDatabase db;
    private final ParkingSpotRepository spotRepo = new ParkingSpotRepository(db);
    private final TariffRepository tariffRepo = new TariffRepository(db);
    private final VehicleRepository vehicleRepo = new VehicleRepository(db);
    private final ReservationRepository reservationRepo = new ReservationRepository(db);
    private final PricingService pricingService = new PricingService();

    public void start() {
        while (true) {
            System.out.println("Smart parking system");
            System.out.println("1. Print all available spots");
            System.out.println("2. Print tariffs");
            System.out.println("3. Park vehicle");
            System.out.println("4. Parking fee");
            System.out.println("5. Quit");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    spotRepo.printFreeSpots();
                case 2:
                    tariffRepo.printAllTariffs();
                case 3:
                    parkVehicle();
                case 4:
                    parkingFee();
                case 5:
                    return;
                }
            }
        }

    private void parkVehicle() {

        System.out.print("Enter plate number: ");
        String plate = scanner.nextLine().toUpperCase();

        System.out.print("Vehicle type: ");
        String vehicleType = scanner.nextLine();

        System.out.print("Parking type: ");
        String spotType = scanner.nextLine();

        Integer spotId = spotRepo.findFreeSpot(spotType);
        if (spotId == null) throw new NoFreeSpotsException();

        int vehicleId = vehicleRepo.saveVehicle(plate, vehicleType);

        reservationRepo.startReservation(spotId, vehicleId);
        spotRepo.setAvailability(spotId, false);

        System.out.println("Vehicle parked successfully! ");
    }

    private void parkingFee() {

        System.out.print("Enter plate number: ");
        String plate = scanner.nextLine().toUpperCase();

        try {
            ResultSet rs = reservationRepo.getActiveReservation(plate);

            int reservationId = rs.getInt("id");
            int spotId = rs.getInt("parking_spot_id");
            double pricePerHour = rs.getDouble("price_per_hour");

            System.out.print("Duration of parking (hours): ");
            int hours = scanner.nextInt();

            System.out.println("Parking fee: " + pricePerHour*hours);

            reservationRepo.finishReservation(reservationId);
            spotRepo.setAvailability(spotId, true);

            System.out.println("Spot is now available");

        } catch (Exception e) {
            throw new RuntimeException("Error while calculating fee");
        }
    }
}

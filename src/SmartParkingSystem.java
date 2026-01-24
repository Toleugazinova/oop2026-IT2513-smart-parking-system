import db.IDatabase;
import db.PostgresDB;
import entity.ParkingSpot;
import entity.Tariff;
import exception.NoFreeSpotsException;
import repository.*;
import service.PricingService;

import java.util.Scanner;

public class SmartParkingSystem {

    public static void main(String[] args) {
        IDatabase db = new PostgresDB();

        ParkingSpotRepository spotRepo = new ParkingSpotRepository(db);
        VehicleRepository vehicleRepo = new VehicleRepository(db);
        TariffRepository tariffRepo = new TariffRepository(db);
        ReservationRepository resRepo = new ReservationRepository(db);

        ReservationService resService = new ReservationService(spotRepo, vehicleRepo, tariffRepo, resRepo);
        PricingService pricingService = new PricingService(resRepo, tariffRepo, spotRepo, vehicleRepo);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Smart Parking System ---");
            System.out.println("1. Print all available spots");
            System.out.println("2. Print tariffs");
            System.out.println("3. Park vehicle");
            System.out.println("4. Parking fee");
            System.out.println("5. Quit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.println("Available Spots:");
                        for (ParkingSpot s : spotRepo.getAllSpots()) {
                            if (s.isAvailable()) System.out.println(s);
                        }
                        break;
                    case 2:
                        System.out.println("Tariffs:");
                        for (Tariff t : tariffRepo.getAllTariffs()) {
                            System.out.println(t);
                        }
                        break;
                    case 3:
                        System.out.print("Enter Plate Number (e.g. KZ01): ");
                        String plate = scanner.nextLine();
                        System.out.print("Enter Vehicle Type (sedan/suv): ");
                        String type = scanner.nextLine();
                        System.out.println(resService.parkVehicle(plate, type));
                        break;
                    case 4:
                        System.out.print("Enter Plate Number to checkout: ");
                        String payPlate = scanner.nextLine();
                        System.out.println(pricingService.calculateAndPay(payPlate));
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid command.");
                }
            } catch (NoFreeSpotsException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("System Error: " + e.getMessage());
            }
        }
    }
}
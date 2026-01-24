import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.IDatabase;
import repository.*;
import service.*;
import java.util.Scanner;

public class SmartParkingSystem {
    private final IDatabase db = new DatabaseConnection();
    private final ParkingSpotRepository spotRepo = new ParkingSpotRepository(db);
    private final TariffRepository tariffRepo = new TariffRepository(db);
    private final VehicleRepository vehicleRepo = new VehicleRepository(db);
    private final ReservationRepository resRepo = new ReservationRepository(db);
    private final ReservationService resService = new ReservationService(spotRepo, vehicleRepo, tariffRepo, resRepo);
    private final PricingService pricingService = new PricingService(resRepo, tariffRepo, spotRepo, vehicleRepo);
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n1. Print all available spots\n2. Print tariffs\n3. Park vehicle\n4. Parking fee\n5. Quit");
            int choice = scanner.nextInt(); scanner.nextLine();
            try {
                switch (choice) {
                    case 1 -> spotRepo.printFreeSpots();
                    case 2 -> tariffRepo.printAllTariffs();
                    case 3 -> {
                        System.out.print("Plate: "); String p = scanner.nextLine();
                        System.out.print("Type: "); String t = scanner.nextLine();
                        System.out.println(resService.parkVehicle(p, t));
                    }
                    case 4 -> {
                        System.out.print("Plate: "); String p = scanner.nextLine();
                        System.out.println(pricingService.calculateAndPay(p));
                    }
                    case 5 -> System.exit(0);
                }
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        }
    }

    public static void main(String[] args) { new SmartParkingSystem().start(); }
}
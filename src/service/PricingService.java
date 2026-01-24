package service;

import entity.Reservation;
import entity.Vehicle;
import exception.InvalidVehiclePlateException;
import repository.*;

public class PricingService {
    private final ReservationRepository resRepo;
    private final TariffRepository tariffRepo;
    private final ParkingSpotRepository spotRepo;
    private final VehicleRepository vehicleRepo;

    public PricingService(ReservationRepository resRepo, TariffRepository tariffRepo, ParkingSpotRepository spotRepo, VehicleRepository vehicleRepo) {
        this.resRepo = resRepo;
        this.tariffRepo = tariffRepo;
        this.spotRepo = spotRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public String calculateAndPay(String plate) throws InvalidVehiclePlateException {
        Vehicle vehicle = vehicleRepo.findByPlate(plate);
        if (vehicle == null) throw new InvalidVehiclePlateException("Vehicle not found!");

        Reservation res = resRepo.findActiveReservation(vehicle.getId());
        if (res == null) return "No active parking session for this vehicle.";

        long durationMillis = System.currentTimeMillis() - res.getStartTime().getTime();
        double hours = Math.ceil(durationMillis / (1000.0 * 60 * 60));
        if (hours == 0) hours = 1;

        double price = tariffRepo.getPriceById(res.getTariffId());
        double totalFee = hours * price;

        resRepo.closeReservation(res.getId());
        spotRepo.updateStatus(res.getParkingSpotId(), true);

        return "Parking finished. Time: " + (int)hours + " hours. Total Fee: " + totalFee + " KZT.";
    }
}
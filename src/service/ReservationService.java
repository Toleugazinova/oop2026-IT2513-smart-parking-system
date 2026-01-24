package service;

import entity.*;
import exception.*;
import repository.*;

public class ReservationService {
    private final ParkingSpotRepository spotRepo;
    private final VehicleRepository vehicleRepo;
    private final TariffRepository tariffRepo;
    private final ReservationRepository resRepo;

    public ReservationService(ParkingSpotRepository spotRepo, VehicleRepository vehicleRepo, TariffRepository tariffRepo, ReservationRepository resRepo) {
        this.spotRepo = spotRepo;
        this.vehicleRepo = vehicleRepo;
        this.tariffRepo = tariffRepo;
        this.resRepo = resRepo;
    }

    public String parkVehicle(String plate, String vehicleType) throws NoFreeSpotsException {
        Vehicle vehicle = vehicleRepo.findByPlate(plate);
        int vehicleId;
        if (vehicle == null) {
            vehicleId = vehicleRepo.createVehicle(plate, vehicleType);
        } else {
            vehicleId = vehicle.getId();
        }

        ParkingSpot spot = spotRepo.findFirstAvailable();
        if (spot == null) {
            throw new NoFreeSpotsException("Sorry, no free spots available right now.");
        }

        Tariff tariff = tariffRepo.getTariffBySpotType(spot.getSpotType());
        if (tariff == null) return "Error: No tariff found for spot type " + spot.getSpotType();

        resRepo.createReservation(vehicleId, spot.getId(), tariff.getId());

        spotRepo.updateStatus(spot.getId(), false);

        return "Success! Vehicle " + plate + " parked at spot " + spot.getSpotNumber() + " (" + spot.getSpotType() + ")";
    }
}
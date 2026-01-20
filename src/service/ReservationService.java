package service;

import entity.ParkingSpot;
import entity.Reservation;
import entity.Tariff;
import entity.Vehicle;
import exception.InvalidVehiclePlateException;
import exception.NoFreeSpotsException;
import exception.ReservationStateException;
import repository.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {

    private final ParkingSpotRepository spotRepo;
    private final VehicleRepository vehicleRepo;
    private final TariffRepository tariffRepo;
    private final ReservationRepository reservationRepo;
    private final PricingService pricingService;

    public ReservationService(ParkingSpotRepository spotRepo,
                              VehicleRepository vehicleRepo,
                              TariffRepository tariffRepo,
                              ReservationRepository reservationRepo,
                              PricingService pricingService) {
        this.spotRepo = spotRepo;
        this.vehicleRepo = vehicleRepo;
        this.tariffRepo = tariffRepo;
        this.reservationRepo = reservationRepo;
        this.pricingService = pricingService;
    }

    public void reserveSpot(String plateNumber) {
        Vehicle vehicle = vehicleRepo.findByPlate(plateNumber);
        if (vehicle == null) {
            throw new InvalidVehiclePlateException();
        }

        if (reservationRepo.findActiveByVehicle(vehicle.getId()) != null) {
            throw new ReservationStateException("Reservation already active");
        }

        List<ParkingSpot> freeSpots = spotRepo.findFreeSpots();
        if (freeSpots.isEmpty()) {
            throw new NoFreeSpotsException();
        }

        ParkingSpot spot = freeSpots.get(0);
        Tariff tariff = tariffRepo.findBySpotType(spot.getSpotType());

        reservationRepo.createReservation(vehicle.getId(), spot.getId(), tariff.getId());
        spotRepo.setAvailability(spot.getId(), false);

        System.out.println("Spot reserved: " + spot.getSpotNumber());
    }

    public void releaseSpot(String plateNumber) {
        Vehicle vehicle = vehicleRepo.findByPlate(plateNumber);
        if (vehicle == null) {
            throw new InvalidVehiclePlateException();
        }

        Reservation reservation = reservationRepo.findActiveByVehicle(vehicle.getId());
        if (reservation == null) {
            throw new ReservationStateException("No active reservation");
        }

        Tariff tariff = tariffRepo.findBySpotType("standard"); // упрощение
        LocalDateTime end = LocalDateTime.now();

        reservationRepo.finishReservation(reservation.getId());
        spotRepo.setAvailability(reservation.getParkingSpotId(), true);

        var cost = pricingService.calculateCost(tariff,
                reservation.getStartTime(), end);

        System.out.println("Reservation finished. Cost = " + cost);
    }
}
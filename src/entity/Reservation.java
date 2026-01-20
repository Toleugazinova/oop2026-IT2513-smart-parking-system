package entity;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private int vehicleId;
    private int parkingSpotId;
    private int tariffId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public Reservation(int id, int vehicleId, int parkingSpotId, int tariffId,
                       LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.parkingSpotId = parkingSpotId;
        this.tariffId = tariffId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getId() { return id; }
    public int getVehicleId() { return vehicleId; }
    public int getParkingSpotId() { return parkingSpotId; }
    public int getTariffId() { return tariffId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getStatus() { return status; }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
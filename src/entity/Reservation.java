package entity;

import java.sql.Timestamp;

public class Reservation {
    private int id;
    private int vehicleId;
    private int parkingSpotId;
    private int tariffId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;

    public Reservation(int id, int vehicleId, int parkingSpotId, int tariffId, Timestamp startTime, Timestamp endTime, String status) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.parkingSpotId = parkingSpotId;
        this.tariffId = tariffId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getId() { return id; }
    public int getParkingSpotId() { return parkingSpotId; }
    public Timestamp getStartTime() { return startTime; }
    public int getTariffId() { return tariffId; }
}
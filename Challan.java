package com.ast.challan;

import java.time.LocalDateTime;

public class Challan {

    public enum Violation {
        OVER_SPEEDING,
        SIGNAL_VIOLATION,
        ILLEGAL_PARKING
    }

    public enum PaymentStatus {
        UNPAID,
        PAID
    }

    private final String id;
    private final String vehicleNumber;
    private final Violation violation;
    private final String location;
    private final LocalDateTime timestamp;
    private final double speed;
    private final double permittedSpeed;
    private final double fineAmount;
    private final String eventKey;

    private PaymentStatus paymentStatus =
            PaymentStatus.UNPAID;

    public Challan(
            String id,
            String vehicleNumber,
            Violation violation,
            String location,
            LocalDateTime timestamp,
            double speed,
            double permittedSpeed,
            double fineAmount,
            String eventKey) {

        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.violation = violation;
        this.location = location;
        this.timestamp = timestamp;
        this.speed = speed;
        this.permittedSpeed = permittedSpeed;
        this.fineAmount = fineAmount;
        this.eventKey = eventKey;
    }

    public String getId() {
        return id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public Violation getViolation() {
        return violation;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getSpeed() {
        return speed;
    }

    public double getPermittedSpeed() {
        return permittedSpeed;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void pay() {
        paymentStatus = PaymentStatus.PAID;
    }

    @Override
    public String toString() {

        return id +
                " | Vehicle: " +
                vehicleNumber +
                " | " +
                violation +
                " | Fine: ₹" +
                fineAmount +
                " | " +
                paymentStatus +
                " | " +
                timestamp;
    }
}

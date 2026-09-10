package com.ast.challan;

public class Vehicle {

    public enum Type {
        TWO_WHEELER,
        CAR,
        BUS,
        TRUCK,
        OTHER
    }

    private final String number;
    private final String owner;
    private final Type type;

    private int violationCount;

    public Vehicle(
            String number,
            String owner,
            Type type) {

        this.number = number;
        this.owner = owner;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public Type getType() {
        return type;
    }

    public int getViolationCount() {
        return violationCount;
    }

    public void incrementViolations() {
        violationCount++;
    }

    public String classification() {

        if (violationCount >= 5) {
            return "HIGH_RISK";
        }

        if (violationCount >= 3) {
            return "REPEAT_OFFENDER";
        }

        if (violationCount >= 1) {
            return "VIOLATOR";
        }

        return "CLEAN";
    }
}

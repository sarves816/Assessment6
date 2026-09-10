package com.ast.challan;

import java.time.LocalDateTime;
import java.util.*;

public class TrafficService {

    private final Map<String, Vehicle> vehicles =
            new HashMap<>();

    private final List<Challan> challans =
            new ArrayList<>();

    private final Set<String> events =
            new HashSet<>();

    public void registerVehicle(
            String number,
            String owner,
            Vehicle.Type type)
            throws InvalidVehicleException {

        if (number == null ||
                !number.matches(
                        "[A-Za-z]{2}[- ]?[0-9]{1,2}" +
                        "[- ]?[A-Za-z]{1,3}" +
                        "[- ]?[0-9]{1,4}")
                || owner == null
                || owner.isBlank()
                || type == null) {

            throw new InvalidVehicleException(
                    "Invalid vehicle information");
        }

        if (vehicles.containsKey(number)) {

            throw new InvalidVehicleException(
                    "Vehicle already registered");
        }

        vehicles.put(
                number,
                new Vehicle(
                        number,
                        owner,
                        type));
    }

    public Challan issueChallan(
            String vehicleNumber,
            Challan.Violation violation,
            String location,
            LocalDateTime timestamp,
            double speed,
            double permittedSpeed,
            String eventKey)
            throws Exception {

        Vehicle vehicle =
                vehicles.get(vehicleNumber);

        if (vehicle == null) {

            throw new InvalidVehicleException(
                    "Vehicle is not registered");
        }

        if (violation == null
                || location == null
                || location.isBlank()
                || timestamp == null
                || eventKey == null
                || eventKey.isBlank()
                || speed < 0
                || permittedSpeed <= 0) {

            throw new InvalidVehicleException(
                    "Invalid violation information");
        }

        if (events.contains(eventKey)) {

            throw new DuplicateChallanException(
                    "Duplicate challan for this violation event");
        }

        double fine =
                calculateFine(
                        violation,
                        speed,
                        permittedSpeed,
                        vehicle.getViolationCount());

        vehicle.incrementViolations();

        String challanId =
                "CH-" +
                String.format(
                        "%04d",
                        challans.size() + 1);

        Challan challan =
                new Challan(
                        challanId,
                        vehicleNumber,
                        violation,
                        location,
                        timestamp,
                        speed,
                        permittedSpeed,
                        fine,
                        eventKey);

        challans.add(challan);
        events.add(eventKey);

        return challan;
    }

    private double calculateFine(
            Challan.Violation violation,
            double speed,
            double limit,
            int previousViolations) {

        double baseFine;

        switch (violation) {

            case OVER_SPEEDING:

                baseFine =
                        Math.max(
                                500,
                                (speed - limit) * 50);

                break;

            case SIGNAL_VIOLATION:

                baseFine = 1000;

                break;

            case ILLEGAL_PARKING:

                baseFine = 500;

                break;

            default:

                baseFine = 0;
        }

        double multiplier;

        if (previousViolations >= 3) {

            multiplier = 2.0;

        } else if (previousViolations >= 1) {

            multiplier = 1.5;

        } else {

            multiplier = 1.0;
        }

        return baseFine * multiplier;
    }

    public void payChallan(String id)
            throws InvalidVehicleException {

        Challan challan = find(id);

        if (challan == null) {

            throw new InvalidVehicleException(
                    "Challan not found");
        }

        challan.pay();
    }

    private Challan find(String id) {

        return challans.stream()
                .filter(c ->
                        c.getId()
                                .equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public double outstanding(
            String vehicleNumber) {

        return challans.stream()
                .filter(c ->
                        c.getVehicleNumber()
                                .equals(vehicleNumber)
                        && c.getPaymentStatus()
                                == Challan.PaymentStatus.UNPAID)
                .mapToDouble(
                        Challan::getFineAmount)
                .sum();
    }

    public Vehicle getVehicle(
            String number) {

        return vehicles.get(number);
    }

    public List<Challan> getChallans() {

        return Collections.unmodifiableList(
                challans);
    }
}

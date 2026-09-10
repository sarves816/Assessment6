package com.ast.challan;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TrafficServiceTest {

    @Test
    void registerAndIssue()
            throws Exception {

        TrafficService service =
                new TrafficService();

        service.registerVehicle(
                "TN01AB1234",
                "Ravi",
                Vehicle.Type.CAR);

        Challan challan =
                service.issueChallan(
                        "TN01AB1234",
                        Challan.Violation.OVER_SPEEDING,
                        "Chennai",
                        LocalDateTime.now(),
                        80,
                        60,
                        "E1");

        assertTrue(
                challan.getFineAmount() > 0);
    }

    @Test
    void invalidVehicle() {

        TrafficService service =
                new TrafficService();

        assertThrows(
                InvalidVehicleException.class,
                () ->
                        service.registerVehicle(
                                "BAD",
                                "Ravi",
                                Vehicle.Type.CAR));
    }

    @Test
    void duplicateEvent()
            throws Exception {

        TrafficService service =
                new TrafficService();

        service.registerVehicle(
                "TN01AB1234",
                "Ravi",
                Vehicle.Type.CAR);

        service.issueChallan(
                "TN01AB1234",
                Challan.Violation.ILLEGAL_PARKING,
                "Chennai",
                LocalDateTime.now(),
                0,
                40,
                "E1");

        assertThrows(
                DuplicateChallanException.class,
                () ->
                        service.issueChallan(
                                "TN01AB1234",
                                Challan.Violation.ILLEGAL_PARKING,
                                "Chennai",
                                LocalDateTime.now(),
                                0,
                                40,
                                "E1"));
    }

    @Test
    void paymentAndOutstanding()
            throws Exception {

        TrafficService service =
                new TrafficService();

        service.registerVehicle(
                "TN01AB1234",
                "Ravi",
                Vehicle.Type.CAR);

        Challan challan =
                service.issueChallan(
                        "TN01AB1234",
                        Challan.Violation.SIGNAL_VIOLATION,
                        "Chennai",
                        LocalDateTime.now(),
                        30,
                        40,
                        "E1");

        assertEquals(
                challan.getFineAmount(),
                service.outstanding(
                        "TN01AB1234"));

        service.payChallan(
                challan.getId());

        assertEquals(
                0,
                service.outstanding(
                        "TN01AB1234"));
    }

    @Test
    void repeatedViolationHigherFine()
            throws Exception {

        TrafficService service =
                new TrafficService();

        service.registerVehicle(
                "TN01AB1234",
                "Ravi",
                Vehicle.Type.CAR);

        double firstFine =
                service.issueChallan(
                        "TN01AB1234",
                        Challan.Violation.SIGNAL_VIOLATION,
                        "Chennai",
                        LocalDateTime.now(),
                        30,
                        40,
                        "E1")
                        .getFineAmount();

        double secondFine =
                service.issueChallan(
                        "TN01AB1234",
                        Challan.Violation.SIGNAL_VIOLATION,
                        "Chennai",
                        LocalDateTime.now(),
                        30,
                        40,
                        "E2")
                        .getFineAmount();

        assertTrue(
                secondFine > firstFine);
    }
}

package com.ast.challan;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        TrafficService service =
                new TrafficService();

        while (true) {

            System.out.println(
                    "\n===== TRAFFIC E-CHALLAN SYSTEM =====");

            System.out.println("1. Register Vehicle");
            System.out.println("2. Issue Challan");
            System.out.println("3. Pay Challan");
            System.out.println("4. View Challans");
            System.out.println("5. Outstanding Fine");
            System.out.println("6. Vehicle Classification");
            System.out.println("7. Exit");

            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            try {

                switch (choice) {

                    case "1":

                        System.out.print(
                                "Vehicle Number: ");

                        String number =
                                sc.nextLine()
                                        .toUpperCase();

                        System.out.print(
                                "Owner Name: ");

                        String owner =
                                sc.nextLine();

                        System.out.print(
                                "Vehicle Type " +
                                "(TWO_WHEELER/CAR/BUS/TRUCK/OTHER): ");

                        Vehicle.Type type =
                                Vehicle.Type.valueOf(
                                        sc.nextLine()
                                                .toUpperCase());

                        service.registerVehicle(
                                number,
                                owner,
                                type);

                        System.out.println(
                                "Vehicle registered successfully.");

                        break;

                    case "2":

                        System.out.print(
                                "Vehicle Number: ");

                        number =
                                sc.nextLine()
                                        .toUpperCase();

                        System.out.print(
                                "Violation " +
                                "(OVER_SPEEDING/" +
                                "SIGNAL_VIOLATION/" +
                                "ILLEGAL_PARKING): ");

                        Challan.Violation violation =
                                Challan.Violation.valueOf(
                                        sc.nextLine()
                                                .toUpperCase());

                        System.out.print(
                                "Location: ");

                        String location =
                                sc.nextLine();

                        System.out.print(
                                "Speed: ");

                        double speed =
                                Double.parseDouble(
                                        sc.nextLine());

                        System.out.print(
                                "Permitted Speed: ");

                        double permittedSpeed =
                                Double.parseDouble(
                                        sc.nextLine());

                        System.out.print(
                                "Violation Event ID: ");

                        String eventId =
                                sc.nextLine();

                        Challan challan =
                                service.issueChallan(
                                        number,
                                        violation,
                                        location,
                                        LocalDateTime.now(),
                                        speed,
                                        permittedSpeed,
                                        eventId);

                        System.out.println(
                                "\nChallan Generated:");

                        System.out.println(challan);

                        break;

                    case "3":

                        System.out.print(
                                "Challan ID: ");

                        String challanId =
                                sc.nextLine();

                        service.payChallan(
                                challanId);

                        System.out.println(
                                "Payment successful.");

                        break;

                    case "4":

                        System.out.println(
                                "\n===== CHALLAN HISTORY =====");

                        for (Challan c :
                                service.getChallans()) {

                            System.out.println(c);
                        }

                        break;

                    case "5":

                        System.out.print(
                                "Vehicle Number: ");

                        number =
                                sc.nextLine()
                                        .toUpperCase();

                        System.out.println(
                                "Outstanding Fine: ₹" +
                                service.outstanding(number));

                        break;

                    case "6":

                        System.out.print(
                                "Vehicle Number: ");

                        number =
                                sc.nextLine()
                                        .toUpperCase();

                        Vehicle vehicle =
                                service.getVehicle(number);

                        if (vehicle == null) {

                            throw new InvalidVehicleException(
                                    "Vehicle not found");
                        }

                        System.out.println(
                                "Classification: " +
                                vehicle.classification());

                        break;

                    case "7":

                        System.out.println(
                                "Program terminated.");

                        sc.close();

                        return;

                    default:

                        System.out.println(
                                "Invalid option.");
                }

            } catch (Exception e) {

                System.out.println(
                        "ERROR: " + e.getMessage());
            }
        }
    }
}

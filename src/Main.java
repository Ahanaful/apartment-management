package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ApartmentManager manager = new ApartmentManager();
        manager.testDatabaseConnection();

        //if you want to add resident, do this steps
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add resident (FirstName, LastName, RoomNumber)");
        String FirstName = scanner.nextLine();
        String LastName = scanner.nextLine();
        int RoomNumber = scanner.nextInt();
        manager.addResident(FirstName, LastName, RoomNumber);
        manager.reassignResidentIDs();
        manager.displayResident();
        scanner.close();
    }
}
package cashcardmanagementsystem;

import java.util.Scanner;

public class CashCardManagementSystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nWELCOME TO CASH CARD MANAGEMENT SYSTEM");
            System.out.println("-----------------------------------------");
            System.out.println("1. USER INFORMATION");
            System.out.println("2. CARD INFORMATION");
            System.out.println("3. TRANSACTION MANAGEMENT");
            System.out.println("4. ACCOUNT INFORMATION");
            System.out.println("5. EXIT");

            int action = 0;
            boolean validInput = false;

            while (!validInput) {
                System.out.print("Enter action (1-5): ");
                if (sc.hasNextInt()) {
                    action = sc.nextInt();
                    if (action >= 1 && action <= 5) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    sc.next(); 
                }
            }

            switch (action) {
                case 1:
                    User user = new User();
                    user.user();
                    break;
                case 2:
                    Card card = new Card();
                    card.card();
                    break;
                case 3:
                    Transaction transaction = new Transaction();
                    transaction.transactionMenu();
                    break;
                case 4:
                    Accounts accounts = new Accounts();
                    accounts.viewUserTransactions();
                    break;
                case 5:
                    System.out.print("Exit selected. Type 'yes' to confirm or 'no' to cancel: ");
                    String resp = sc.next().trim().toLowerCase();
                    while (!resp.equals("yes") && !resp.equals("no")) {
                        System.out.print("Invalid response. Type 'yes' to confirm or 'no' to cancel: ");
                        resp = sc.next().trim().toLowerCase();
                    }
                    exit = resp.equals("yes");
                    if (!exit) {
                        System.out.println("Returning to the main menu...");
                    }
                    break;
                default:
                    System.out.println("Unexpected error. Please try again.");
                    break;
            }
        }

        System.out.println("Exiting Cash Card Management System. Thank you!");
        sc.close();
    }
}

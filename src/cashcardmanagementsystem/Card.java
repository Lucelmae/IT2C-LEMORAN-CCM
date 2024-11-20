package cashcardmanagementsystem;

import java.util.Scanner;

public class Card {
    private config conf = new config();

    public void card() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("-----------------------------------------");
            System.out.println("CARD PANEL");
            System.out.println("1. Add Card");
            System.out.println("2. View Card");
            System.out.println("3. Update Card");
            System.out.println("4. Delete Card");
            System.out.println("5. Exit");

            int act = 0;
            boolean validInput = false;


            while (!validInput) {
                System.out.print("Enter Action (1-5): ");
                if (sc.hasNextInt()) {
                    act = sc.nextInt();
                    sc.nextLine(); 
                    if (act >= 1 && act <= 5) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid option. Please choose between 1 and 5.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.next(); 
                }
            }

            switch (act) {
                case 1:
                    addCard();
                    break;
                case 2:
                    viewCard();
                    break;
                case 3:
                    viewCard();
                    updateCard();
                    break;
                case 4:
                    viewCard();
                    deleteCard();
                    break;
                case 5:
                    System.out.println("Exiting card panel.");
                    return;
                default:
                    System.out.println("Unexpected error. Please try again.");
            }

            System.out.print("Do you want to continue? (yes/no): ");
            response = sc.nextLine().trim().toLowerCase();
            while (!response.equals("yes") && !response.equals("no")) {
                System.out.print("Invalid response. Enter 'yes' to continue or 'no' to exit: ");
                response = sc.nextLine().trim().toLowerCase();
            }
        } while (response.equals("yes"));
    }

    public void addCard() {
        Scanner sc = new Scanner(System.in);
        String number, expire, type, balance;


        do {
            System.out.print("Enter Card Number (16 digits): ");
            number = sc.nextLine().trim();
        } while (!number.matches("\\d{16}"));


        do {
            System.out.print("Enter Expiry Date (MM/YYYY): ");
            expire = sc.nextLine().trim();
        } while (!expire.matches("^(0[1-9]|1[0-2])/\\d{4}$"));


        do {
            System.out.print("Enter Card Type (Debit/Prepaid): ");
            type = sc.nextLine().trim().toLowerCase();
        } while (!type.equals("debit") && !type.equals("prepaid"));


        do {
            System.out.print("Enter Initial Balance (positive number): ");
            balance = sc.nextLine().trim();
        } while (!balance.matches("\\d+(\\.\\d{1,2})?"));

        String sql = "INSERT INTO tbl_card (c_number, c_expiredate, c_type, c_balance) VALUES (?, ?, ?, ?)";
        conf.addRecord(sql, number, expire, type, balance);
    }

    public void viewCard() {
        String qry = "SELECT * FROM tbl_card";
        String[] headers = {"ID", "Card Number", "Expire Date", "Card Type", "Balance"};
        String[] columns = {"c_id", "c_number", "c_expiredate", "c_type", "c_balance"};

        conf.viewRecords(qry, headers, columns);
    }

    public void updateCard() {
        Scanner sc = new Scanner(System.in);
        String cardId;


        do {
            System.out.print("Enter Card ID to update: ");
            cardId = sc.nextLine().trim();
            if (conf.getSingleValue("SELECT c_id FROM tbl_card WHERE c_id = ?", cardId) == 0) {
                System.out.println("Selected Card ID doesn't exist. Try again.");
            } else {
                break;
            }
        } while (true);


        System.out.print("Updated Card Number (16 digits): ");
        String uNumber = sc.nextLine().trim();
        while (!uNumber.matches("\\d{16}")) {
            System.out.print("Invalid card number. Enter a 16-digit card number: ");
            uNumber = sc.nextLine().trim();
        }

        System.out.print("Updated Expiry Date (MM/YYYY): ");
        String uExpire = sc.nextLine().trim();
        while (!uExpire.matches("^(0[1-9]|1[0-2])/\\d{4}$")) {
            System.out.print("Invalid expiry date. Enter in MM/YYYY format: ");
            uExpire = sc.nextLine().trim();
        }

        System.out.print("Updated Card Type (Debit/Prepaid): ");
        String uType = sc.nextLine().trim().toLowerCase();
        while (!uType.equals("debit") && !uType.equals("prepaid")) {
            System.out.print("Invalid card type. Enter 'Debit' or 'Prepaid': ");
            uType = sc.nextLine().trim().toLowerCase();
        }

        System.out.print("Updated Balance (positive number): ");
        String uBalance = sc.nextLine().trim();
        while (!uBalance.matches("\\d+(\\.\\d{1,2})?")) {
            System.out.print("Invalid balance. Enter a positive number: ");
            uBalance = sc.nextLine().trim();
        }

        String qry = "UPDATE tbl_card SET c_number = ?, c_expiredate = ?, c_type = ?, c_balance = ? WHERE c_id = ?";
        conf.updateRecord(qry, uNumber, uExpire, uType, uBalance, cardId);
    }

    public void deleteCard() {
        Scanner sc = new Scanner(System.in);
        String cardId;


        do {
            System.out.print("Enter Card ID to delete: ");
            cardId = sc.nextLine().trim();
            if (conf.getSingleValue("SELECT c_id FROM tbl_card WHERE c_id = ?", cardId) == 0) {
                System.out.println("Selected Card ID doesn't exist. Try again.");
            } else {
                break;
            }
        } while (true);

        String sqlDelete = "DELETE FROM tbl_card WHERE c_id = ?";
        conf.deleteRecord(sqlDelete, cardId);
    }
}

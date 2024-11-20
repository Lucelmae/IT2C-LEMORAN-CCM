package cashcardmanagementsystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Transaction {
    private config conf = new config(); 

    public void transactionMenu() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("-----------------------------------------");
            System.out.println("TRANSACTION PANEL");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. Update Transaction");
            System.out.println("4. Delete Transaction");
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
                        System.out.println("Invalid option. Please choose a number between 1 and 5.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.next();
                }
            }

            switch (act) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    viewTransactions();
                    break;
                case 3:
                    viewTransactions();
                    updateTransaction();
                    break;
                case 4:
                    viewTransactions();
                    deleteTransaction();
                    break;
                case 5:
                    System.out.println("Exiting transaction panel.");
                    return;
                default:
                    System.out.println("Unexpected error occurred.");
            }

            System.out.print("Do you want to continue in the Transaction Panel? (yes/no): ");
            response = sc.nextLine().trim().toLowerCase();
            while (!response.equals("yes") && !response.equals("no")) {
                System.out.print("Invalid response. Please enter 'yes' or 'no': ");
                response = sc.nextLine().trim().toLowerCase();
            }
        } while (response.equals("yes"));
    }

    public void addTransaction() {
        Scanner sc = new Scanner(System.in);

        User us = new User();
        us.viewUser();
        String userId;
        do {
            System.out.print("Enter User ID: ");
            userId = sc.nextLine().trim();
        } while (!validateExistence("SELECT user_id FROM tbl_users WHERE user_id = ?", userId, "User ID does not exist!"));

        Card cd = new Card();
        cd.viewCard();
        String cardId;
        do {
            System.out.print("Enter Card ID: ");
            cardId = sc.nextLine().trim();
        } while (!validateExistence("SELECT c_id FROM tbl_card WHERE c_id = ?", cardId, "Card ID does not exist!"));

        String amount;
        do {
            System.out.print("Enter Amount (positive number): ");
            amount = sc.nextLine().trim();
        } while (!amount.matches("\\d+(\\.\\d{1,2})?") || Double.parseDouble(amount) <= 0);

        String type;
        do {
            System.out.print("Enter Transaction Type (Debit/Prepaid): ");
            type = sc.nextLine().trim().toLowerCase();
        } while (!type.equals("debit") && !type.equals("prepaid"));

        LocalDate currDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String transactionDate = currDate.format(format);

        String transactionStatus = "APPROVED";

        String insertQuery = "INSERT INTO tbl_transaction (user_id, c_id, amount, transaction_type, transaction_date, transaction_status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        conf.addRecord(insertQuery, userId, cardId, amount, type, transactionDate, transactionStatus);
        System.out.println("Transaction successfully added!");
    }

    public void viewTransactions() {
        String viewQuery = "SELECT t.transaction_id, u.user_name, c.c_id, t.amount, t.transaction_type, "
                + "t.transaction_date, t.transaction_status "
                + "FROM tbl_transaction t "
                + "INNER JOIN tbl_users u ON t.user_id = u.user_id "
                + "INNER JOIN tbl_card c ON t.c_id = c.c_id";

        String[] headers = {"Transaction ID", "User Name", "Card Number", "Amount", "Type", "Date", "Status"};
        String[] columns = {"transaction_id", "user_name", "c_id", "amount", "transaction_type", "transaction_date", "transaction_status"};

        System.out.println("\n--- Transaction Report ---");
        conf.viewRecords(viewQuery, headers, columns);
    }

    public void updateTransaction() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Transaction ID to update: ");
        String transactionId;
        do {
            transactionId = sc.nextLine().trim();
        } while (!validateExistence("SELECT transaction_id FROM tbl_transaction WHERE transaction_id = ?", transactionId, "Transaction ID does not exist!"));

        String updatedAmount;
        do {
            System.out.print("Enter Updated Amount (positive number): ");
            updatedAmount = sc.nextLine().trim();
        } while (!updatedAmount.matches("\\d+(\\.\\d{1,2})?") || Double.parseDouble(updatedAmount) <= 0);

        String updatedType;
        do {
            System.out.print("Enter Updated Transaction Type (Debit/Prepaid): ");
            updatedType = sc.nextLine().trim().toLowerCase();
        } while (!updatedType.equals("debit") && !updatedType.equals("prepaid"));

        LocalDate currDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String transactionDate = currDate.format(format);

        String updateQuery = "UPDATE tbl_transaction SET amount = ?, transaction_type = ?, transaction_date = ? WHERE transaction_id = ?";
        conf.updateRecord(updateQuery, updatedAmount, updatedType, transactionDate, transactionId);
        System.out.println("Transaction successfully updated!");
    }

    public void deleteTransaction() {
        Scanner sc = new Scanner(System.in);

        String transactionId;
        do {
            System.out.print("Enter Transaction ID to delete: ");
            transactionId = sc.nextLine().trim();
        } while (!validateExistence("SELECT transaction_id FROM tbl_transaction WHERE transaction_id = ?", transactionId, "Transaction ID does not exist!"));

        String deleteQuery = "DELETE FROM tbl_transaction WHERE transaction_id = ?";
        conf.deleteRecord(deleteQuery, transactionId);
        System.out.println("Transaction successfully deleted!");
    }

    private boolean validateExistence(String query, String param, String errorMessage) {
        if (conf.getSingleValue(query, param) == 0) {
            System.out.println(errorMessage);
            return false;
        }
        return true;
    }
}

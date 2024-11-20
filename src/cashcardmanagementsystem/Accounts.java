package cashcardmanagementsystem;

import java.util.Scanner;

public class Accounts {
    private config conf = new config(); 

    public void viewUserTransactions() {
        Scanner sc = new Scanner(System.in);

        String userId;
        do {
           User u = new User();
           u.viewUser();
            System.out.print("Enter User ID to view transactions: ");
            userId = sc.nextLine().trim();
        } while (!validateExistence("SELECT user_id FROM tbl_users WHERE user_id = ?", userId, "User ID does not exist!"));

        String query = "SELECT t.transaction_id, c.c_id, t.amount, t.transaction_type, "
                     + "t.transaction_date, t.transaction_status "
                     + "FROM tbl_transaction t "
                     + "INNER JOIN tbl_card c ON t.c_id = c.c_id "
                     + "WHERE t.user_id = ?";
                     
        String[] headers = {"Transaction ID", "Card Number", "Amount", "Type", "Date", "Status"};
        String[] columns = {"transaction_id", "c_id", "amount", "transaction_type", "transaction_date", "transaction_status"};

        System.out.println("\n--- Transactions for User ID: " + userId + " ---");
        conf.viewRecords(query, headers, columns, userId); 
    }

    private boolean validateExistence(String query, String param, String errorMessage) {
        if (conf.getSingleValue(query, param) == 0) {
            System.out.println(errorMessage);
            return false;
        }
        return true;
    }
}

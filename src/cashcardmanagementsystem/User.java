package cashcardmanagementsystem;

import java.util.Scanner;

public class User {
    private config conf = new config(); 

    public void user() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("-----------------------------------------");
            System.out.println("USER PANEL");
            System.out.println("1. Add User");
            System.out.println("2. View User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
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
                    System.out.println("Invalid input. Please enter a valid number.");
                    sc.next(); 
                }
            }

            switch (act) {
                case 1:
                    addUser();
                    break;
                case 2:
                    viewUser();
                    break;
                case 3:
                    viewUser();  
                    updateUser();
                    break;
                case 4:
                    viewUser();  
                    deleteUser();
                    break;
                case 5:
                    System.out.println("Exiting user panel.");
                    return;
                default:
                    System.out.println("Unexpected error occurred.");
            }

            System.out.print("Do you want to continue? (yes/no): ");
            response = sc.nextLine().trim().toLowerCase();
            while (!response.equals("yes") && !response.equals("no")) {
                System.out.print("Invalid response. Please enter 'yes' or 'no': ");
                response = sc.nextLine().trim().toLowerCase();
            }
        } while (response.equals("yes"));
    }

    public void addUser() {
        Scanner sc = new Scanner(System.in);

        String userName, address, contact;

        do {
            System.out.print("Enter User Name (only alphabets, max 50 characters): ");
            userName = sc.nextLine().trim();
        } while (!userName.matches("[A-Za-z ]{1,50}"));

        do {
            System.out.print("Enter Address (max 100 characters): ");
            address = sc.nextLine().trim();
        } while (address.isEmpty() || address.length() > 100);

        do {
            System.out.print("Enter Contact Number (10-15 digits): ");
            contact = sc.nextLine().trim();
        } while (!contact.matches("\\d{10,15}"));

        String sql = "INSERT INTO tbl_users (user_name, user_address, user_contact) VALUES (?, ?, ?)";
        conf.addRecord(sql, userName, address, contact);
    }

    public void viewUser() {
        String qry = "SELECT * FROM tbl_users";
        String[] headers = {"ID", "User Name", "Address", "Contact Number"};
        String[] columns = {"user_id", "user_name", "user_address", "user_contact"};

        conf.viewRecords(qry, headers, columns);
    }

    public void updateUser() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter User ID to update: ");
        String userId = sc.nextLine().trim();

        while (conf.getSingleValue("SELECT user_id FROM tbl_users WHERE user_id = ?", userId) == 0) {
            System.out.println("Selected User ID doesn't exist.");
            System.out.print("Enter another User ID: ");
            userId = sc.nextLine().trim();
        }

        String uuserName, uaddress, ucontact;

        do {
            System.out.print("Updated User Name (only alphabets, max 50 characters): ");
            uuserName = sc.nextLine().trim();
        } while (!uuserName.matches("[A-Za-z ]{1,50}"));

        do {
            System.out.print("Updated Address (max 100 characters): ");
            uaddress = sc.nextLine().trim();
        } while (uaddress.isEmpty() || uaddress.length() > 100);

        do {
            System.out.print("Updated Contact Number (10-15 digits): ");
            ucontact = sc.nextLine().trim();
        } while (!ucontact.matches("\\d{10,15}"));

        String qry = "UPDATE tbl_users SET user_name = ?, user_address = ?, user_contact = ? WHERE user_id = ?";
        conf.updateRecord(qry, uuserName, uaddress, ucontact, userId);
    }

    public void deleteUser() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter User ID to delete: ");
        String userId = sc.nextLine().trim();

        while (conf.getSingleValue("SELECT user_id FROM tbl_users WHERE user_id = ?", userId) == 0) {
            System.out.println("Selected User ID doesn't exist.");
            System.out.print("Enter another User ID: ");
            userId = sc.nextLine().trim();
        }

        String sqlDelete = "DELETE FROM tbl_users WHERE user_id = ?";
        conf.deleteRecord(sqlDelete, userId);
    }
}

package com.fhw.Project0;

import java.sql.SQLException;
import java.util.Scanner;

public class CustomerMenu {

    User user;

    public CustomerMenu(User user) throws SQLException{
        this.user = user;
        getMenu();
    }

    public void getMenu() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        boolean flag = true;
        while(flag) {
            ClearScreen.cls();
            System.out.println("\n\n\n\t\t------------------------");
            System.out.println("\t\tCustomer Account Options");
            System.out.println("\t\t------------------------");
            System.out.println("\t\t1: Apply for a new account");
            System.out.println("\t\t2: View account balance");
            System.out.println("\t\t3: Make a deposit");
            System.out.println("\t\t4: Make a withdrawal");
            System.out.println("\t\t5: Transfer funds to another account");
            System.out.println("\t\t6: Accept a funds transfer to your account");
            System.out.println("\t\t7: Return to main menu");

            System.out.print("\n\t\tOption No: ");
            int input = scanner.nextInt();
            Account account = new Account();
            Transfer transfer = new Transfer();

            switch (input) {

                case 1:
                    ClearScreen.cls();
                    Customer.applyForAccount(user);
                    break;

                case 2:
                    ClearScreen.cls();
                    Customer.getAccountDetails(user, account);
                    break;

                case 3:
                    ClearScreen.cls();
                    Customer.depositFunds(user, account);
                    break;

                case 4:
                    ClearScreen.cls();
                    Customer.withdrawFunds(user, account);
                    break;

                case 5:
                    ClearScreen.cls();
                    Customer.postFundsTransfer(user, account, transfer);
                    break;

                case 6:
                    ClearScreen.cls();
                    Customer.approveFundsTransfer(user, account, transfer);
                    break;

                case 7:
                    System.out.println("\n\t\tExiting customer account ...");
                    flag = false;
                    UserMenu.getInstance();
                    break;

                default:
                    System.out.println("\n\t\tYou must enter 1 - 7");

            }
        }
    }
}

package com.fhw.Project0;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.SQLException;


public class EmployeeMenu {

    public EmployeeMenu() throws SQLException {
        getMenu();
    }

    public void getMenu() throws SQLException, InputMismatchException {
        Scanner scanner = new Scanner(System.in);

        boolean flag = true;
        while(flag) {
            ClearScreen.cls();
            System.out.println("\n\n\n\t\t------------------------");
            System.out.println("\t\tEmployee Account Options");
            System.out.println("\t\t------------------------");
            System.out.println("\t\t1: View all database transactions");
            System.out.println("\t\t2: Approve/Reject account applications");
            System.out.println("\t\t3: View specific account details");
            System.out.println("\t\t4: Return to main menu");

            System.out.print("\n\t\tOption No: ");
            int input = scanner.nextInt();

            switch(input) {

                case 1:
                    ClearScreen.cls();
                    Employee.getDbaseTransactions();
                    break;

                case 2:
                    ClearScreen.cls();
                    Employee.approveApplications();
                    break;

                case 3:
                    ClearScreen.cls();
                    Employee.seeAccountDetails();
                    break;

                case 4:
                    System.out.println("\t\tExiting employee account ...");
                    flag = false;
                    UserMenu.getInstance();
                    break;

                default:
                    System.out.println("\n\t\tYou must enter 1 - 4");
            }
        }
    }
}

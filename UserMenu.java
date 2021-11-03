package com.fhw.Project0;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserMenu {

    private static UserMenu instance = null;

    // Top menu instantiated as singleton
    private UserMenu() throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        boolean flag = true;
        while(flag) {
            ClearScreen.cls();
            System.out.println("\n\n\n\t\t$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println("\t\t$  WELCOME TO BANK OF FRANK  $");
            System.out.println("\t\t$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println("\t\t-----------------");
            System.out.println("\t\tSelect an option:");
            System.out.println("\t\t-----------------");
            System.out.println("\t\t1: Register for a new account");
            System.out.println("\t\t2: Customer login");
            System.out.println("\t\t3: Employee login");
            System.out.println("\t\t4: Exit");

            System.out.print("\n\t\tOption No: ");

            try {
                int input = scanner.nextInt();

                switch (input) {
                    case 1:
                        ClearScreen.cls();
                        User.accountRegistration();
                        break;

                    case 2:
                        ClearScreen.cls();
                        User.userLogin();
                        break;

                    case 3:
                        ClearScreen.cls();
                        Employee.employeeLogin();
                        break;

                    case 4: {
                        flag = false;
                        System.exit(0);
                    }

                    default: {
                        ClearScreen.cls();
                        System.out.println("\n\n\n\t\t-> You must enter 1 - 4");
                        Prompt.prompt();
                    }
                }

            }catch(InputMismatchException e){
                System.out.println("\n\t\t-> You must enter a number");
                Prompt.prompt();
                ClearScreen.cls();
                getInstance();
            }




        }
    }



    public static UserMenu getInstance() throws SQLException{
        if (instance == null)
            instance = new UserMenu();
        return instance;

    }


    }



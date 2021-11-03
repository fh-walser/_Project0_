package com.fhw.Project0;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public User(){

    }

    public User(int id, String firstName, String lastName, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;

   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void accountRegistration() throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        System.out.println("\n\n\n\t\t------------------------");
        System.out.println("\t\tNew Account Registration");
        System.out.println("\t\t------------------------\n");

            System.out.print("\t\tFirst Name: ");
            String fname = scanner.next();
            System.out.print("\t\tLast Name: ");
            String lname = scanner.next();
            System.out.print("\t\tNew username: ");
            String uname = scanner.next();
            System.out.print("\t\tNew password (MAX 8 CHARACTERS): ");
            String passwd = scanner.next();

            user.setFirstName(fname);
            user.setLastName(lname);
            user.setUsername(uname);
            user.setPassword(passwd);

        if (dao.verifyUser(user)) {
            System.out.println("\n\t\t-> You are already registered as a user.\n\n" +
                    "\t\t-> Please choose Option 2 to log in");
            Prompt.prompt();
        } else {
            dao.addNewUser(user);
            Prompt.prompt();
        }
    }

    public static void userLogin() throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        boolean flag = true;

        while (flag) {
            ClearScreen.cls();
            System.out.println("\n\n\n\t\t--------------");
            System.out.println("\t\tCustomer Login");
            System.out.println("\t\t--------------");
            System.out.print("\n\t\tUsername: ");
            String uname = scanner.next();
            System.out.print("\t\tPassword: ");
            String passwd = scanner.next();

            user.setUsername(uname);
            user.setPassword(passwd);
            user.setId(dao.fetchUserId(user));

            if (dao.verifyUser(user)) {

                if (dao.verifyAccount(user)) {
                    flag = false;
                    CustomerMenu customerMenu = new CustomerMenu(user);

                } else {
                    System.out.print("\n\t\t-> You do not yet have an account...\n\n\t\t-> Create new account now? (Yes/No): ");
                    String ans = scanner.next().toUpperCase();
                    if (ans.equals("YES") || ans.equals("Y")) {

                        Customer.applyForAccount(user);

                        flag = false;
                        break;

                    } else {
                        System.out.println("\n\t\t-> You must have an account to access services");
                        Prompt.prompt();
                        break;
                    }
                }
            } else {
                System.out.println("\n\t\t-> Please login again");
                Prompt.prompt();

            }
        }
    }
}


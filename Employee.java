package com.fhw.Project0;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Employee {
    private int id;
    private int userId;
    private boolean isCustomer;

    public Employee(){

    }

    public Employee(int Id, boolean isCustomer) {
        this.id = id;
        this.userId = userId;
        this.isCustomer = isCustomer;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getCustStatus() {
        return isCustomer;
    }

    public void setName(boolean isCustomer) {
        this.isCustomer = isCustomer;
    }

    public static void employeeLogin() throws SQLException{
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        Employee employee = new Employee();

        boolean flag = true;
        while (flag) {

            System.out.println("\n\n\n\t\t--------------");
            System.out.println("\t\tEmployee Login");
            System.out.println("\t\t--------------\n");
            System.out.print("\t\tUsername: ");
            String uname = scanner.next();
            System.out.print("\t\tPassword: ");
            String passwd = scanner.next();
            System.out.print("\t\tEmployee No: ");

            try {
                int empno = scanner.nextInt();

                user.setUsername(uname);
                user.setPassword(passwd);
                user.setId(dao.fetchUserId(user));

                employee.setId(empno);
                employee.setUserId(user.getId());

            }catch(InputMismatchException e){
                System.out.println("\n\t\t-> Employee No. must be a number");
                Prompt.prompt();
                ClearScreen.cls();
                employeeLogin();
            }

                if (dao.isEmployee(employee)) {
                    flag = false;
                    EmployeeMenu employeeMenu = new EmployeeMenu();
                    break;
                } else {
                    System.out.println("\n\t\t-> Employee number not identified\n\t\t-> Please login again");
                    Prompt.prompt();
                    ClearScreen.cls();
                    employeeLogin();
                }

        }

    }

    public static void getDbaseTransactions() throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        dao.retrieveAllTransactions();
        Prompt.prompt();
    }

    public static void approveApplications() throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        dao.getAllApplications();
        System.out.print("\n\t\tEnter Application No. to approve or reject application: ");
        int applId = scanner.nextInt();
        System.out.print("\n\t\tEnter 'A' to accept application\n\t\tEnter 'R' to reject application\n\t\t'A' or 'R': ");
        String aorR = scanner.next().toUpperCase();
        Application application = new Application();
        application.setId(applId);

        boolean flag = true;
        while (flag) {
            if (aorR.equals("A")) {
                dao.addNewAccount(application);
                flag = false;

            } else if (aorR.equals("R")) {
                dao.rejectApplication(application);
                flag = false;

            }else{
                System.out.println("\n\t\t-> You must enter 'A' or 'R'");
                Prompt.prompt();
            }
        }
        Prompt.prompt();
    }

    public static void seeAccountDetails() throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\n\t\tAll active Bank of Frank Accounts:\n");
        dao.retrieveAllAccountDetails();
        System.out.print("\n\t\tEnter Account No. to view specific details: ");
        int acctId = scanner.nextInt();
        Account account = new Account();
        account.setId(acctId);
        dao.retrieveSpecificAccountDetails(account);
        Prompt.prompt();


    }

}
package com.fhw.Project0;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Customer {

    private int id;
    private String fname;
    private String lname;
    private User user;

    public Customer(User user){
        this.user = user;
    }

    public static void applyForAccount(User user) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);
        Application application = new Application();

        System.out.println("\n\n\n\t\tStart a new account at Bank of Frank");
        System.out.print("\n\t\tTypes of account to open:\n\t\t   Checking\n\t\t   Savings\n\t\t   IRA\n\t\t   CD\n\n\t\tInput account type: ");
        String accountType = scanner.next();

        if(AccountTypeCheck.applicationCheck(accountType)) {
            System.out.print("\t\tAmount to deposit: ");
            double deposit = scanner.nextDouble();

            application.setUserId(user.getId());
            application.setAcctType(accountType);
            application.setDeposit(deposit);
            dao.addApplication(application);
            Prompt.prompt();
        }else{
            System.out.println("\t\t-> You must enter a choice from the list provided");
            Prompt.prompt();
            ClearScreen.cls();
            Customer.applyForAccount(user);
        }

    }

    public static void getAccountDetails(User user, Account account) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n\n\t\tYour accounts at Bank of Frank\n");
        dao.retrieveAccountViewDetails(user);
        System.out.print("\n\t\tEnter Account No. to see details: ");
        int acctId = scanner.nextInt();

        account.setId(acctId);
        dao.retrieveSpecificAccountDetails(account);
        Prompt.prompt();

    }

    public static void depositFunds(User user, Account account) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n\n\t\tDeposit funds into your account\n");
        dao.retrieveAccountViewDetails(user);
        System.out.print("\n\t\tEnter Account No. for deposit: ");
        int acctNo = scanner.nextInt();
        System.out.print("\n\t\tEnter amount for deposit: ");
        double amount = scanner.nextDouble();

        account.setId(acctNo);
        try {
            dao.depositFunds(account, amount);
            Prompt.prompt();
        }catch(NegativeAmountException e){
            e.getMessage();
            Prompt.prompt();
        }
    }

    public static void withdrawFunds(User user, Account account) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n\n\t\tWithdraw funds from your account\n");
        dao.retrieveAccountViewDetails(user);
        System.out.print("\n\t\tEnter Account No. for withdrawal: ");
        int acctNo = scanner.nextInt();
        System.out.print("\n\t\tEnter amount for withdrawal: ");
        double amount = scanner.nextDouble();

        account.setId(acctNo);
        try {
            dao.withdrawFunds(account, amount);
            Prompt.prompt();
        }catch(InsufficientFundsException | NegativeAmountException e){
            e.getMessage();
            Prompt.prompt();
        }


    }

    public static void postFundsTransfer(User user, Account account, Transfer transfer) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n\n\t\tPost a funds transfer to another account\n");
        dao.retrieveAccountViewDetails(user);

        System.out.print("\n\t\tSelect Account No. to transfer money from: ");
        int acctid = scanner.nextInt();
        account.setId(acctid);
        transfer.setSendId(acctid);
        System.out.print("\n\t\tSelect Account No. to transfer money to: ");
        int recid = scanner.nextInt();
        transfer.setRecId(recid);
        System.out.print("\n\t\tAmount to transfer: ");
        double amount = scanner.nextDouble();
        transfer.setAmount(amount);

        dao.addNewTransfer(transfer);
        Prompt.prompt();

    }

    public static void approveFundsTransfer(User user, Account account, Transfer transfer) throws SQLException {
        BankDao dao = BankDaoFactory.getBankDao();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n\n\n\t\tApprove a funds transfer to your account\n");
        dao.retrieveAccountViewDetails(user);
        System.out.print("\n\t\tEnter Account No. to see awaiting transfers: ");
        int acctid = scanner.nextInt();
        account.setId(acctid);
        if(dao.retrieveTransferDetails(account)) {
            System.out.print("\n\t\tEnter Transfer No. to approve transfer to your account: ");
            int transId = scanner.nextInt();
            transfer.setId(transId);
            try {
                dao.postTransferToAccount(transfer);
                Prompt.prompt();
            }catch(InsufficientFundsException | NegativeAmountException e){
                e.getMessage();
                Prompt.prompt();
            }
        }else{
            Prompt.prompt();
        }
    }


}


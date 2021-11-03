package com.fhw.Project0;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class BankDaoImpl<T> implements BankDao {

    Connection connection;

    public BankDaoImpl() {this.connection = ConnectionFactory.getConnection();}

    // verify whether the user already has a system account
    @Override
    public boolean verifyUser(User user) throws SQLException {
        String sql = "SELECT userName, password FROM Users WHERE userName=? AND password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return true;
        else
            return false;

    }

    public int fetchUserId(User user) throws SQLException {
        String query = "{CALL sp_get_id(?, ?)}";
        //String sql = "SELECT userId FROM Users WHERE userName=? AND password=?";
        CallableStatement callableStatement = connection.prepareCall(query);
        callableStatement.setString(1, user.getUsername());
        callableStatement.setString(2, user.getPassword());
        ResultSet resultSet = callableStatement.executeQuery();
        if(resultSet.next()) {
            return resultSet.getInt(1);
        }
        else {
            System.out.println("\n\t\t-> User ID not found");
            return 00;
        }

    }

    // A new user has joined! Add details to the Users table
    @Override
    public void addNewUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (firstName, LastName, userName, password) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setString(3, user.getUsername());
        preparedStatement.setString(4, user.getPassword());
        int count = preparedStatement.executeUpdate();
        if(count > 0) {
            System.out.println("\n\t\t-> New user account created");

            // **** UPDATE TRANSACTIONS TABLE *******
            BankDao dao = BankDaoFactory.getBankDao();
            Transaction transaction = new Transaction();
            transaction.setTime(GetTime.time());
            String logString = "New user " +
                    user.getFirstName() + " " +
                    user.getLastName() + " added to system";
            transaction.setDescription(logString);
            dao.updateTransactionsLog(transaction);
        }
        else {
            System.out.println("\n\t\t-> ERROR: No account created");
        }
    }

    // Check whether the user has an account aka is a 'customer'
    @Override
    public boolean verifyAccount(User user) throws SQLException {
        String sql = "SELECT acctId FROM Users t1 INNER JOIN Accounts t2 ON t1.userId=? AND t1.userId = t2.userId";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return true;
        else
            return false;
    }

    // Customer can see all his accounts
    @Override
    public void retrieveAccountViewDetails(User user) throws SQLException {
        String sql = "SELECT acctId, acctType FROM Accounts t1 INNER JOIN Users t2 ON t2.userId=? AND t1.userId = t2.userId";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.format("%-21s%-12s\n","\t\tACCOUNT NO.","ACCOUNT TYPE");
        while(resultSet.next()) {
            System.out.format("%-21s%-12s\n", "\t\t" + resultSet.getInt(1), resultSet.getString(2));
        }
    }

    // New account added following employee approval and corresponding application removed from Applications table
    @Override
    public void addNewAccount(Application application) throws SQLException {
        String sql1 = "SELECT userId, acctType, amountToDep, applId FROM Applications WHERE applId=?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        preparedStatement1.setInt(1, application.getId());
        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();

        String sql2 = "INSERT INTO Accounts (userId, acctType, balance) VALUES (?, ? ,?)";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        preparedStatement2.setInt(1, resultSet.getInt(1));
        preparedStatement2.setString(2, resultSet.getString(2));
        preparedStatement2.setDouble(3, resultSet.getDouble(3));
        int count = preparedStatement2.executeUpdate();
        if(count > 0) {
            System.out.println("\n\t\t-> New account created");

            // **** UPDATE TRANSACTIONS TABLE *******
            BankDao dao = BankDaoFactory.getBankDao();
            Transaction transaction = new Transaction();
            transaction.setTime(GetTime.time());
            String logString = "New " + resultSet.getString(2) + " account create for user ID " + application.getUserId();
            transaction.setDescription(logString);
            dao.updateTransactionsLog(transaction);
        }

        else {
            System.out.println("\n\t\t-> Account creation failed");
        }

        String sql3 = "DELETE FROM Applications WHERE applId=?";
        PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
        preparedStatement3.setInt(1, resultSet.getInt(4));
        int count1 = preparedStatement3.executeUpdate();
        if(count1 > 0)
            System.out.println("\n\t\t-> Applications list updated");
        else
            System.out.println("\n\t\t-> Error updating Applications list");

        BankDao dao = BankDaoFactory.getBankDao();
        Transaction transaction = new Transaction();
        transaction.setTime(GetTime.time());
        String logString = "Application No." + application.getId() + " deleted from Applications table" ;
        transaction.setDescription(logString);
        dao.updateTransactionsLog(transaction);

    }

    // Customer can see specific account details
    @Override
    public void retrieveSpecificAccountDetails(Account account) throws SQLException {
        String sql = "SELECT acctId, acctType, balance FROM Accounts WHERE acctId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("\n\t\tAccount details:");
        System.out.format("%-21s%-18s%10s\n", "\t\tACCOUNT NO.","ACCOUNT TYPE", "BALANCE");
        while(resultSet.next()) {
            System.out.format("%-21s%-18s%10s\n", "\t\t" + resultSet.getInt(1), resultSet.getString(2), "$"+resultSet.getString(3));
        }

    }

    // Increment or decrement account balance
    @Override
    public void modifyAccountBalance(Account account) throws SQLException {
        String sql = "UPDATE Accounts SET balance=? WHERE acctId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDouble(1, account.getBalance());
        preparedStatement.setInt(2, account.getId());
        int count = preparedStatement.executeUpdate();
        if(count <= 0)
            System.out.println("\n\t\t-> Transaction failure Acct No: " + account.getId());

    }

    @Override
    public double retrieveAccountBalance(Account account) throws SQLException {
        String sql = "SELECT balance FROM Accounts WHERE acctId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getDouble(1);
    }

    @Override
    public void retrieveAllAccountDetails() throws SQLException {
        String sql = "SELECT firstName, lastName, acctId, acctType FROM Accounts t1 INNER JOIN Users t2 ON t1.userId = t2.userId";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.format("%-21s%-14s%-16S%-12s\n", "\t\tFIRST NAME", "LAST NAME", "ACCOUNT NO.", "ACCOUNT TYPE");
        while(resultSet.next()) {
            System.out.format("%-21s%-14s%-16S%-12s\n", "\t\t" + resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getString(4));
        }

    }

    @Override
    public void withdrawFunds(Account account, double amount) throws SQLException, NegativeAmountException {

        if(amount < 0){
            throw new NegativeAmountException();
        }else {

            BankDao dao = BankDaoFactory.getBankDao();

            // get balance
            double balance = dao.retrieveAccountBalance(account);

            // Test if valid
            if (amount <= balance) {
                balance -= amount;
                account.setBalance(balance);
                dao.modifyAccountBalance(account);
                System.out.println("\n\t\t-> Account No: " + account.getId() + " debited: $" + amount);

                // **** UPDATE TRANSACTIONS TABLE *******
                Transaction transaction = new Transaction();
                transaction.setTime(GetTime.time());
                String logString = "Account No. " + account.getId() + " debited $" + amount;
                transaction.setDescription(logString);
                dao.updateTransactionsLog(transaction);

            } else {
                throw new InsufficientFundsException();
            }
        }

    }

    @Override
    public void depositFunds(Account account, double amount) throws SQLException, NegativeAmountException {

        if(amount < 0){
            throw new NegativeAmountException();
        }else{

            BankDao dao = BankDaoFactory.getBankDao();

            // get balance
            account.setId(account.getId());
            double balance = dao.retrieveAccountBalance(account);

            // calculate new receiver balance
            balance += amount;
            account.setBalance(balance);

            // modify account
            dao.modifyAccountBalance(account);
            System.out.println("\n\t\t-> Account No: " + account.getId() + " credited $" + amount);

            // **** UPDATE TRANSACTIONS TABLE *******
            Transaction transaction = new Transaction();
            transaction.setTime(GetTime.time());
            String logString = "Account No: " + account.getId() + " credited $" + amount;
            transaction.setDescription(logString);
            dao.updateTransactionsLog(transaction);
        }

    }

    // Adds a new application by user or customer to the Applications table
    @Override
    public void addApplication(Application application) throws SQLException {
        String sql = "INSERT INTO Applications (userId, acctType, amountToDep) VALUES (?, ? ,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, application.getUserId());
        preparedStatement.setString(2, application.getAcctType());
        preparedStatement.setDouble(3, application.getDeposit());
        int count = preparedStatement.executeUpdate();
        if(count > 0) {
            System.out.println("\n\t\t-> Your application has been submitted");

            // **** UPDATE TRANSACTIONS TABLE *******
            BankDao dao = BankDaoFactory.getBankDao();
            Transaction transaction = new Transaction();
            transaction.setTime(GetTime.time());
            String logString = "User No. " + application.getUserId() + " applied for a " + application.getAcctType() + " account";
            transaction.setDescription(logString);
            dao.updateTransactionsLog(transaction);
        }

        else {
            System.out.println("\n\t\t-> No application submitted, please re-apply");
        }
    }


    // Shows all applications awaiting approval to and employee
    @Override
    public void getAllApplications() throws SQLException {
        String sql = "SELECT * FROM Applications";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("\n\n\n\t\tAccount applications for approval/rejection:\n");
        System.out.format("%-21s%-17s%-17s%10s\n", "\t\tAPPLICATION NO.", "CUSTOMER NO.", "ACCOUNT TYPE", "DEPOSIT");
        while(resultSet.next()) {
            System.out.format("%-21s%-17d%-17s%10s\n", "\t\t" + resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), "$"+resultSet.getString(4));
        }

    }

    @Override
    public void rejectApplication(Application application) throws SQLException {
        String sql2 = "DELETE FROM Applications WHERE applId=?";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        preparedStatement2.setInt(1, application.getId());
        int count = preparedStatement2.executeUpdate();
        if(count > 0)
            System.out.println("\n\t\t-> Applications list updated");
        else
            System.out.println("\n\t\t-> Error updating Applications list");

        BankDao dao = BankDaoFactory.getBankDao();
        Transaction transaction = new Transaction();
        transaction.setTime(GetTime.time());
        String logString = "Application No. " + application.getId() + " rejected and deleted from table" ;
        transaction.setDescription(logString);
        dao.updateTransactionsLog(transaction);

    }

    @Override
    public void addNewTransfer(Transfer transfer) throws SQLException {
        String sql = "INSERT INTO Transfers (sendId, recId, transAmt) VALUES (?, ? ,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, transfer.getSendId());
        preparedStatement.setInt(2, transfer.getRecId());
        preparedStatement.setDouble(3, transfer.getAmount());
        int count = preparedStatement.executeUpdate();
        if(count > 0) {
            System.out.println("\n\t\t-> Your transfer is posted awaiting approval");

            // **** UPDATE TRANSACTIONS TABLE *******
            BankDao dao = BankDaoFactory.getBankDao();
            Transaction transaction = new Transaction();
            transaction.setTime(GetTime.time());
            String logString = "Funds posted for transfer from Account No. " + transfer.getSendId() + " to Account No. " + transfer.getRecId();
            transaction.setDescription(logString);
            dao.updateTransactionsLog(transaction);
        }

        else {
            System.out.println("\n\t\t-> No transfer recorded, please re-submit");
        }
    }

    // Shows all transfers posted to customer account for acceptance
    @Override
    public boolean retrieveTransferDetails(Account account) throws SQLException {
        String sql = "SELECT * FROM Transfers WHERE recId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("\n\t\tTransfers awaiting approval:");

        if(resultSet.next()) {
            System.out.format("%-21s%-23s%-25s%10s\n", "\t\tTRANSFER NO.", "SENDER ACCOUNT NO.", "RECEIVER ACCOUNT NO.", "AMOUNT");
            System.out.format("%-21s%-23d%-25d%10s\n", "\t\t" + resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), "$" + resultSet.getString(4));

            while (resultSet.next()) {
                System.out.format("%-21s%-23d%-25d%10s\n", "\t\t" + resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), "$" + resultSet.getString(4));
            }
            return true;
        }else{
            System.out.println("\n\t\t-> There are no further transfers to be approved for this account");
            return false;
        }

    }

    @Override
    public void postTransferToAccount(Transfer transfer) throws SQLException, NegativeAmountException {

        // get key transfer details
        String sql1 = "SELECT sendId, recId, transAmt FROM Transfers WHERE transferId=?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        preparedStatement1.setInt(1, transfer.getId());
        ResultSet resultSet = preparedStatement1.executeQuery();
        if(resultSet.next()) {

            int transferId = transfer.getId();
            int senderAcct = resultSet.getInt(1);
            int receiverAcct = resultSet.getInt(2);
            double transamount = resultSet.getDouble(3);

            BankDao dao = BankDaoFactory.getBankDao();
            Account account = new Account();
            account.setId(senderAcct);

            dao.withdrawFunds(account, transamount);
            account.setId(receiverAcct);
            dao.depositFunds(account, transamount);

            String sql2 = "DELETE FROM Transfers WHERE transferId=" + Integer.toString(transferId);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            int count = preparedStatement2.executeUpdate();
            if (count > 0) {
                System.out.println("\n\t\t-> Transfer complete");

                // **** UPDATE TRANSACTIONS TABLE *******
                Transaction transaction = new Transaction();
                transaction.setTime(GetTime.time());
                String logString = "Funds transferred from Account No. " + senderAcct + " to Account No. " + receiverAcct;
                transaction.setDescription(logString);
                dao.updateTransactionsLog(transaction);
            }
     }

        else {
            System.out.println("\n\t\t-> ERROR: Transfer cannot be confirmed");
        }

    }

    @Override
    public void updateTransactionsLog (Transaction transaction) throws SQLException {
        String sql = "INSERT INTO Transactions_Log (time, transDescript) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, transaction.getTime());
        preparedStatement.setString(2, transaction.getDescription());
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("\n\t\t-> Transaction recorded");
        else
            System.out.println("\n\t\t-> Error: Failed to update Transactions Log");


    }

    @Override
    public void retrieveAllTransactions() throws SQLException {
        String sql = "SELECT * FROM Transactions_Log";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("\n\t\tTransactions Log:");
        System.out.format("%-21s%-23s%-60s\n", "\t\tTRANSACTION NO.", "DATE       TIME", "DESCRIPTION");

        // NOTE - TRY TO APPLY CONDITION TO TEST IF EMPTY
        while(resultSet.next()) {
            System.out.format("%-21s%-23s%-60s\n", "\t\t" + resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
        }

    }


    @Override
    public boolean isEmployee(Employee employee) throws SQLException, InputMismatchException {
        String sql = "SELECT isCust FROM Employees WHERE empId=? AND userId=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, employee.getId());
        preparedStatement.setInt(2, employee.getUserId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return true;
        else
            return false;
    }
}

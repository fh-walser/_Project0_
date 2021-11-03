package com.fhw.Project0;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface BankDao {

    // Actions on Users table
    boolean verifyUser(User user) throws SQLException;
    void addNewUser(User user) throws SQLException;
    int fetchUserId(User user) throws SQLException;

    // Actions on Accounts table
    boolean verifyAccount(User user) throws SQLException;
    void retrieveAccountViewDetails(User user) throws SQLException;
    void addNewAccount(Application application) throws SQLException;
    void retrieveSpecificAccountDetails(Account account) throws SQLException;
    void modifyAccountBalance(Account account) throws SQLException;
    double retrieveAccountBalance(Account account) throws SQLException;
    void retrieveAllAccountDetails() throws SQLException;
    void withdrawFunds(Account account, double amount) throws SQLException, NegativeAmountException;
    void depositFunds(Account account, double amount) throws SQLException, NegativeAmountException;

    // Actions on Applications table
    void addApplication(Application application) throws SQLException;
    void getAllApplications() throws SQLException;
    void rejectApplication(Application application) throws SQLException;

    //Actions on Transfers table
    void addNewTransfer(Transfer transfer)  throws SQLException;
    boolean retrieveTransferDetails(Account account) throws SQLException;
    void postTransferToAccount(Transfer transfer) throws SQLException, NegativeAmountException;

    // Actions on Transactions table
    void updateTransactionsLog (Transaction transaction) throws SQLException;
    void retrieveAllTransactions() throws SQLException;

    // Actions on Employees table
    boolean isEmployee(Employee employee) throws SQLException;



}

package com.fhw.Project0;

public class Account {

    private int id;
    private int userId;
    private String acctType;
    private double balance;


    public Account() {
        
    }

    public Account(int id, int userId, String acctType, double balance) {

        this.id = id;
        this.userId = userId;
        this.acctType = acctType;
        this.balance = balance;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

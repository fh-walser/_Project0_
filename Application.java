package com.fhw.Project0;

public class Application {
    private int id;
    private int userId;
    private String acctType;
    private double deposit;

    public Application(int id, int userId, String acctType, double deposit) {
        this.id = id;
        this.userId = userId;
        this.acctType = acctType;
        this.deposit = deposit;

    }

    public Application() {

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

    public String getAcctType() { return acctType; }

    public void setAcctType(String acctType) {this.acctType = acctType;}

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
}

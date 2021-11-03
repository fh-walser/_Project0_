package com.fhw.Project0;

public class Transaction {

    private int id;
    private String time;
    private String description;

    public Transaction() {

    }

    public Transaction(int id, String time, String description) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

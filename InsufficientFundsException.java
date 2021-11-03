package com.fhw.Project0;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        System.out.println("\n\t\t-> Insufficient funds in account to support this request");

    }
}

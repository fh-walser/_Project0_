package com.fhw.Project0;

public class NegativeAmountException extends Throwable {

    public NegativeAmountException(){
        System.out.println("\n\t\t-> You must enter a positive value for this transaction");
    }

}

package com.fhw.Project0;

import java.util.Arrays;

public class AccountTypeCheck {
    
    public static boolean applicationCheck(String s) {
        
        String[] arr = {"Checking", "Savings", "IRA", "CD"};
        return Arrays.asList(arr).contains(s);
    }
    
}

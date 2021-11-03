package com.fhw.Project0;

public class ClearScreen {

    public static void cls() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}

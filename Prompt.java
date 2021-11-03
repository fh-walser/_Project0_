package com.fhw.Project0;

import java.util.Scanner;

public class Prompt {

    // source https://stackoverflow.com/questions/26184409/java-console-prompt-for-enter-input-before-moving-on/26184535
    public static void prompt(){
        System.out.println("\n\t\t-> HIT \"ENTER\" TO CONTINUE...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}

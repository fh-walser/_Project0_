package com.fhw.Project0;

public class GetTime {
    public static String time() {
        // code to convert Java Date to MySql datetime
        // from https://stackoverflow.com/questions/2400955/how-to-store-java-date-to-mysql-datetime-with-jpa
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        return currentTime;
    }
}

package com.fhw.Project0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionFactory {

    private static Connection connection = null;

    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            //ResourceBundle bundle = ResourceBundle.getBundle("com/fhw/Project0/dbConfig");
            //String url = bundle.getString("jdbc:mysql://localhost:3306/BankOfFrank");
            //String username = bundle.getString("fhw");
            //String password = bundle.getString("12345");
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankOfFrank", "fhw", "12345");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return connection;
    }
}

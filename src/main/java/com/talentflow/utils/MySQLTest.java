package com.talentflow.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/talentflowhr_db?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

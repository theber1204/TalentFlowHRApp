package com.talentflow.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class DatabaseUtils {
    // Parámetros de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/talentflowhr_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Metodo para obtener una conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        try {
            // Primero intenta con JNDI
            try {
                javax.naming.InitialContext context = new javax.naming.InitialContext();
                javax.sql.DataSource dataSource = (javax.sql.DataSource) context.lookup("java:comp/env/jdbc/talentflowhrDB");
                return dataSource.getConnection();
            } catch (Exception e) {
                System.out.println("JNDI fallido, usando conexión directa: " + e.getMessage());
                // Si falla JNDI, usa conexión directa
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC", e);
        }
    }

    // Metodo para ejecutar consultas SELECT
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(statement, params);
        return statement.executeQuery();
    }

    // Metodo para ejecutar consultas INSERT, UPDATE o DELETE
    public static int executeUpdate(String query, Object... params) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params);
            return statement.executeUpdate();
        }
    }

    // Metodo para cerrar recursos
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Metodo auxiliar para establecer parámetros en un PreparedStatement
    private static void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
package com.ordersmanagement.Connection;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = ConnectionFactory.getConnection();
        if (conn != null) {
            System.out.println("Connected to PostgreSQL successfully!");
            ConnectionFactory.close(conn);
        } else {
            System.out.println("Failed to connect.");
        }
    }


}


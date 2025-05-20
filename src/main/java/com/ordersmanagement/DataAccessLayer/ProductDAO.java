package com.ordersmanagement.DataAccessLayer;

import com.ordersmanagement.Connection.ConnectionFactory;
import com.ordersmanagement.Model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO extends AbstractDAO<Product> {

    /**
     * Default constructor for ProductDAO.
     */
    public ProductDAO() {
        super();
    }

    /**
     * Retrieves the current stock quantity for a given product ID.
     *
     * @param productId the ID of the product
     * @return the stock quantity
     * @throws SQLException if the product is not found or a database access error occurs
     */
    public int getStock(int productId) throws SQLException {
        String sql = "SELECT stock FROM product WHERE id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
                } else {
                    throw new SQLException("Product not found.");
                }
            }
        }
    }

    /**
     * Decreases the stock of a product by a specified amount using the provided connection.
     * Ensures stock does not go below zero.
     *
     * @param productId the ID of the product
     * @param amount the amount to decrease
     * @param connection the active database connection
     * @throws SQLException if there is not enough stock or a database access error occurs
     */
    public void decreaseStock(int productId, int amount, Connection connection) throws SQLException {
        String sql = "UPDATE product SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, productId);
            stmt.setInt(3, amount);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Not enough stock to decrease.");
            }
        }
    }
}

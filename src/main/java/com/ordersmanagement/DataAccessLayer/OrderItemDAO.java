package com.ordersmanagement.DataAccessLayer;

import com.ordersmanagement.Model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemDAO extends AbstractDAO<OrderItem> {

    /**
     * Default constructor for OrderItemDAO.
     */
    public OrderItemDAO() {
        super();
    }

    /**
     * Inserts an OrderItem into the database using the provided connection.
     *
     * @param item the OrderItem to insert
     * @param connection the active database connection
     * @throws SQLException if a database access error occurs or the SQL statement fails
     */
    public void insertOrderItem(OrderItem item, Connection connection) throws SQLException {
        String sql = "INSERT INTO order_item(order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.executeUpdate();
        }
    }
}

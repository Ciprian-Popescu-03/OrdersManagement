package com.ordersmanagement.Business_Logic;

import com.ordersmanagement.DataAccessLayer.OrderDAO;
import com.ordersmanagement.DataAccessLayer.OrderItemDAO;
import com.ordersmanagement.DataAccessLayer.ProductDAO;
import com.ordersmanagement.Connection.ConnectionFactory;
import com.ordersmanagement.Model.Order;
import com.ordersmanagement.Model.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO = new OrderDAO();
    private OrderItemDAO orderItemDAO = new OrderItemDAO();
    private ProductDAO productDAO = new ProductDAO();

    /**
     * Creates an order with multiple order items and updates product stock in a single transaction.
     * If any operation fails, the entire transaction is rolled back.
     *
     * @param order The Order object to be created.
     * @param items A List of OrderItem objects to be associated with the order.
     * @throws SQLException If a database access error occurs or stock is insufficient.
     */
    public void createOrderWithItems(Order order, List<OrderItem> items) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            int orderId = orderDAO.insertOrder(order, connection);

            for (OrderItem item : items) {
                int stock = productDAO.getStock(item.getProductId());
                if (stock < item.getQuantity()) {
                    throw new SQLException("Not enough stock for product ID: " + item.getProductId());
                }

                item.setOrderId(orderId);

                orderItemDAO.insertOrderItem(item, connection);

                productDAO.decreaseStock(item.getProductId(), item.getQuantity(), connection);
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
}

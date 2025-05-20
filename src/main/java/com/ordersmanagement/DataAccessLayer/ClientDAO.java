package com.ordersmanagement.DataAccessLayer;

import com.ordersmanagement.Connection.ConnectionFactory;
import com.ordersmanagement.Model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO extends AbstractDAO<Client> {

    /**
     * Default constructor initializing the DAO for Client entity.
     */
    public ClientDAO() {
        super();
    }

    /**
     * Checks if a client has any associated orders.
     *
     * @param clientId the ID of the client to check
     * @return true if the client has one or more orders, false otherwise
     */
    public boolean hasOrders(int clientId) {
        String query = "SELECT 1 FROM orders WHERE client_id = ? LIMIT 1";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.ordersmanagement.Business_Logic;

import com.ordersmanagement.DataAccessLayer.ClientDAO;
import com.ordersmanagement.Model.Client;

import java.util.List;

public class ClientService {
    private final ClientDAO clientDAO = new ClientDAO();

    /**
     * Adds a new client to the database.
     *
     * @param client the Client object to add
     * @return true if insertion was successful, false otherwise
     */
    public boolean addClient(Client client) {
        return clientDAO.insert(client) != null;
    }

    /**
     * Edits an existing client in the database.
     *
     * @param client the Client object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean editClient(Client client) {
        return clientDAO.update(client) != null;
    }

    /**
     * Deletes a client from the database if the client has no existing orders.
     *
     * @param client the Client object to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteClient(Client client) {
        if (clientDAO.hasOrders(client.getId())) {
            System.out.println("Cannot delete client: Client has existing orders.");
            return false;
        }
        return clientDAO.delete(client) != null;
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a List of all Client objects
     */
    public List<Client> viewAllClients() {
        return clientDAO.findAll();
    }
}

package com.ordersmanagement.Presentation;

import com.ordersmanagement.Business_Logic.ClientService;
import com.ordersmanagement.Model.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientGUI extends JFrame {
    private final ClientService clientService = new ClientService();

    /**
     * Constructs the ClientGUI frame with input fields and buttons
     * for managing clients: add, update, delete, and view all.
     */
    public ClientGUI() {
        setTitle("Client Management");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField ageField = new JTextField();

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("View All");

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String address = addressField.getText();
                int age = Integer.parseInt(ageField.getText());
                Client newClient = new Client(name, address, age);
                if (clientService.addClient(newClient)) {
                    showMessage("Client added successfully.");
                } else {
                    showError("Failed to add client.");
                }
            } catch (Exception ex) {
                showError("Invalid input.");
            }
        });

        updateButton.addActionListener(e -> {
            try {
                Client c = getClientFromFields(idField, nameField, addressField, ageField);
                if (clientService.editClient(c)) {
                    showMessage("Client updated successfully.");
                } else {
                    showError("Failed to update client.");
                }
            } catch (Exception ex) {
                showError("Invalid input.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (clientService.deleteClient(new Client(id, "", "", 0))) {
                    showMessage("Client deleted successfully.");
                } else {
                    showError("Could not delete client. They may have existing orders.");
                }
            } catch (Exception ex) {
                showError("Invalid ID.");
            }
        });

        viewButton.addActionListener(e -> {
            List<Client> clients = clientService.viewAllClients();
            JTextArea area = new JTextArea(10, 40);
            area.setEditable(false);
            for (Client c : clients) {
                area.append("ID: " + c.getId() + ", Name: " + c.getName() +
                        ", Address: " + c.getAddress() + ", Age: " + c.getAge() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "All Clients", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("Age:")); panel.add(ageField);
        panel.add(addButton); panel.add(updateButton);
        panel.add(deleteButton); panel.add(viewButton);

        add(panel);
        setVisible(true);
    }

    /**
     * Constructs a Client object from the input fields.
     * @param idField JTextField containing the client ID
     * @param nameField JTextField containing the client name
     * @param addressField JTextField containing the client address
     * @param ageField JTextField containing the client age
     * @return Client object populated from input fields
     * @throws NumberFormatException if ID or age fields are invalid integers
     */
    private Client getClientFromFields(JTextField idField, JTextField nameField, JTextField addressField, JTextField ageField) {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String address = addressField.getText();
        int age = Integer.parseInt(ageField.getText());
        return new Client(id, name, address, age);
    }

    /**
     * Shows an information dialog with the given message.
     * @param message the message to display
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error dialog with the given message.
     * @param message the error message to display
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

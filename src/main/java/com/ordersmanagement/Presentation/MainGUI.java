package com.ordersmanagement.Presentation;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    /**
     * Constructs the main GUI window with buttons to manage products, clients, and orders.
     */
    public MainGUI() {
        setTitle("Order Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton productButton = new JButton("Manage Products");
        JButton clientButton = new JButton("Manage Clients");
        JButton orderButton = new JButton("Manage Orders");

        productButton.addActionListener(e -> new ProductGUI());
        clientButton.addActionListener(e -> new ClientGUI());
        orderButton.addActionListener(e -> new OrderGUI());

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(productButton);
        panel.add(clientButton);
        panel.add(orderButton);

        add(panel);
        setVisible(true);
    }

    /**
     * The entry point of the application.
     * Launches the main GUI in the Swing event dispatch thread.
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}

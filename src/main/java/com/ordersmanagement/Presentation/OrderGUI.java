package com.ordersmanagement.Presentation;

import com.ordersmanagement.Business_Logic.*;
import com.ordersmanagement.DataAccessLayer.OrderItemDAO;
import com.ordersmanagement.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class OrderGUI extends JFrame {
    private JComboBox<Client> clientComboBox;
    private JComboBox<Product> productComboBox;
    private JTextField quantityField;
    private JButton createOrderButton;
    private JTextArea resultArea;

    private ClientService clientService = new ClientService();
    private ProductService productService = new ProductService();
    private OrderService orderService = new OrderService();
    private OrderItemDAO orderItemDAO = new OrderItemDAO();

    /**
     * Constructs the Order GUI window for creating new orders.
     */
    public OrderGUI() {
        setTitle("Create Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLayout(new GridLayout(6, 2, 10, 10));

        clientComboBox = new JComboBox<>();
        productComboBox = new JComboBox<>();
        quantityField = new JTextField();
        createOrderButton = new JButton("Create Order");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        loadClients();
        loadProducts();

        add(new JLabel("Select Client:"));
        add(clientComboBox);
        add(new JLabel("Select Product:"));
        add(productComboBox);
        add(new JLabel("Quantity:"));
        add(quantityField);
        add(new JLabel(""));
        add(createOrderButton);
        add(new JLabel("Status:"));
        add(new JScrollPane(resultArea));

        createOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createOrder();
            }
        });

        setVisible(true);
    }

    /**
     * Loads all clients from the service and populates the client combo box.
     */
    private void loadClients() {
        List<Client> clients = clientService.viewAllClients();
        for (Client c : clients) {
            clientComboBox.addItem(c);
        }
    }

    /**
     * Loads all products from the service and populates the product combo box.
     */
    private void loadProducts() {
        List<Product> products = productService.viewAllProducts();
        for (Product p : products) {
            productComboBox.addItem(p);
        }
    }

    /**
     * Creates an order based on selected client, product, and quantity.
     * Displays success or error messages in the result area.
     */
    private void createOrder() {
        Client client = (Client) clientComboBox.getSelectedItem();
        Product product = (Product) productComboBox.getSelectedItem();
        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            resultArea.setText("Quantity must be a number.");
            return;
        }

        Order order = new Order();
        order.setClientId(client.getId());

        OrderItem item = new OrderItem();
        item.setProductId(product.getId());
        item.setQuantity(quantity);

        List<OrderItem> items = List.of(item);

        try {
            orderService.createOrderWithItems(order, items);
            resultArea.setText("Order created successfully!");
            loadProducts();
            quantityField.setText("");
        } catch (SQLException e) {
            resultArea.setText("Failed to create order: " + e.getMessage());
        }
    }
}

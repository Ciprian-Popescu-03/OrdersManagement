package com.ordersmanagement.Presentation;

import com.ordersmanagement.Business_Logic.ProductService;
import com.ordersmanagement.Model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductGUI extends JFrame {
    private final ProductService productService = new ProductService();

    /**
     * Constructs the Product Management GUI window.
     * Provides interface to add, update, delete, and view products.
     */
    public ProductGUI() {
        setTitle("Product Management");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField priceField = new JTextField();

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("View All");

        addButton.addActionListener(e -> {
            try {
                Product p = getProductFromFields(idField, nameField, stockField, priceField);
                if (productService.addProduct(p)) {
                    showMessage("Product added successfully.");
                } else {
                    showError("Failed to add product.");
                }
            } catch (Exception ex) {
                showError("Invalid input.");
            }
        });

        updateButton.addActionListener(e -> {
            try {
                Product p = getProductFromFields(idField, nameField, stockField, priceField);
                if (productService.editProduct(p)) {
                    showMessage("Product updated successfully.");
                } else {
                    showError("Failed to update product.");
                }
            } catch (Exception ex) {
                showError("Invalid input.");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Product p = new Product(id, "", 0, 0);
                if (productService.deleteProduct(p)) {
                    showMessage("Product deleted successfully.");
                } else {
                    showError("Could not delete product. It may be in use.");
                }
            } catch (Exception ex) {
                showError("Invalid ID.");
            }
        });

        viewButton.addActionListener(e -> {
            List<Product> products = productService.viewAllProducts();
            JTextArea area = new JTextArea(10, 40);
            area.setEditable(false);
            for (Product p : products) {
                area.append("ID: " + p.getId() + ", Name: " + p.getName()
                        + ", Stock: " + p.getStock() + ", Price: $" + p.getPrice() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "All Products", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Stock:")); panel.add(stockField);
        panel.add(new JLabel("Price:")); panel.add(priceField);
        panel.add(addButton); panel.add(updateButton);
        panel.add(deleteButton); panel.add(viewButton);

        add(panel);
        setVisible(true);
    }

    /**
     * Creates a Product object from the input fields.
     * @param idField JTextField containing product ID.
     * @param nameField JTextField containing product name.
     * @param stockField JTextField containing stock quantity.
     * @param priceField JTextField containing product price.
     * @return Product constructed from input values.
     */
    private Product getProductFromFields(JTextField idField, JTextField nameField, JTextField stockField, JTextField priceField) {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        int stock = Integer.parseInt(stockField.getText());
        int price = Integer.parseInt(priceField.getText());
        return new Product(id, name, stock, price);
    }

    /**
     * Shows an information message dialog.
     * @param message Message to display.
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error message dialog.
     * @param message Message to display.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

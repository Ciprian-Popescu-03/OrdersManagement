package com.ordersmanagement.Business_Logic;

import com.ordersmanagement.DataAccessLayer.ProductDAO;
import com.ordersmanagement.Model.Product;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Adds a new product to the database.
     *
     * @param product the Product object to add
     * @return true if insertion was successful, false otherwise
     */
    public boolean addProduct(Product product) {
        return productDAO.insert(product) != null;
    }

    /**
     * Edits an existing product in the database.
     *
     * @param product the Product object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean editProduct(Product product) {
        return productDAO.update(product) != null;
    }

    /**
     * Deletes a product from the database.
     *
     * @param product the Product object to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProduct(Product product) {
        return productDAO.delete(product) != null;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a List of all Product objects
     */
    public List<Product> viewAllProducts() {
        return productDAO.findAll();
    }
}

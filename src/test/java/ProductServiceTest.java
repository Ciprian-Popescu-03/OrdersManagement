import com.ordersmanagement.Business_Logic.ProductService;
import com.ordersmanagement.Model.Product;

import java.util.List;

public class ProductServiceTest {
    public static void main(String[] args) {
        ProductService productService = new ProductService();

       Product newProduct = new Product(1, "Laptop", 10, 1500);
        if (productService.addProduct(newProduct)) {
            System.out.println("New product added successfully!");
        } else {
            System.out.println("Failed to add new product.");
        }

        Product updatedProduct = new Product(1, "Gaming Laptop", 5, 2000);
        if (productService.editProduct(updatedProduct)) {
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Failed to update product.");
        }

        System.out.println("---- View All Products ----");
        List<Product> allProducts = productService.viewAllProducts();
        for (Product product : allProducts) {
            System.out.println("Product ID: " + product.getId() +
                    ", Name: " + product.getName() +
                    ", Stock: " + product.getStock() +
                    ", Price: $" + product.getPrice());
        }

       if (productService.deleteProduct(updatedProduct)) {
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Failed to delete product.");
        }
    }
}

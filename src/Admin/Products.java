/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
MARK REUBEN E. FORTUITO - DOCUMENT SPECIALIST
JOSHUA P. GAGARIN - BUSINESS ANALYST
LEI AIREL A. LOQUIS - SYSTEM ANALYST
JOHN CHRISTOFER LUCERO - ASSISTANT PROGRAMMER
JEREMY L. MALANA - LEAD PROGRAMMER
MIGUEL ROSE N. SORIANO - PROJECT MANAGER
*/
package Admin;

import DatabaseConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author Jeremy Malana
 */
public class Products {
    public void insertProduct(String productId, String category, String name, double price, int quantity, String status, byte[] productImage) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO products (product_id, categories, product_name, price, quantity, product_status, product_image) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, category);
            pstmt.setString(3, name);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, quantity);
            pstmt.setString(6, status);
            pstmt.setBytes(7, productImage);
            pstmt.executeUpdate();
        }
    }

    public void updateProduct(String productId, String category, String name, double price, int quantity, String status, byte[] productImage) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE products SET categories = ?, product_name = ?, price = ?, quantity = ?, product_status = ?, product_image = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, quantity);
            pstmt.setString(5, status);
            pstmt.setBytes(6, productImage);
            pstmt.setString(7, productId);
            pstmt.executeUpdate();
        }
    }

    public void deleteProduct(String productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.executeUpdate();
        }
    }

    public List<Product> getAllProducts() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String productId = rs.getString("product_id");
                String category = rs.getString("categories");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String createdAt = rs.getString("created_at");
                String updatedAt = rs.getString("updated_at");
                String status = rs.getString("product_status");
                byte[] productImage = rs.getBytes("product_image");

                products.add(new Product(id, productId, category, name, price, quantity, createdAt, updatedAt, status, productImage));
            }
        }

        return products;
    }
    
    public List<Product> getAvailableProducts() throws SQLException, ClassNotFoundException {
    String sql = "SELECT * FROM products WHERE product_status = 'Available'";
    List<Product> products = new ArrayList<>();

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            long id = rs.getLong("id");
            String productId = rs.getString("product_id");
            String category = rs.getString("categories");
            String name = rs.getString("product_name");
            double price = rs.getDouble("price");
            int quantity = rs.getInt("quantity");
            String createdAt = rs.getString("created_at");
            String updatedAt = rs.getString("updated_at");
            String status = rs.getString("product_status");
            byte[] productImage = rs.getBytes("product_image");

            products.add(new Product(id, productId, category, name, price, quantity, createdAt, updatedAt, status, productImage));
        }
    }

    return products;
}
    
public List<String> getAllCategories() {
    List<String> categories = new ArrayList<>();
    
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT DISTINCT categories FROM products WHERE categories IS NOT NULL AND categories != ''";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String category = rs.getString("categories");
                if (category != null && !category.trim().isEmpty()) {
                    categories.add(category);
                }
            }
        }
    } catch (SQLException | ClassNotFoundException e) {
        System.err.println("Error fetching categories: " + e.getMessage());
        e.printStackTrace();
    }
    
    return categories;
}

public boolean addCategory(String category) {
    if (category == null || category.trim().isEmpty()) {
        return false;
    }
    
    // First check if category already exists
    List<String> existingCategories = getAllCategories();
    if (existingCategories.contains(category)) {
        return true; // Category already exists
    }
    
    // If category doesn't exist, add a dummy product with this category
    // This ensures the category exists in the database
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "INSERT INTO products (product_id, categories, product_name, price, quantity, product_status) " +
                     "VALUES (?, ?, 'Category Placeholder', 0.0, 0, 'Not Available')";
        
        // Generate a temporary product ID (negative to avoid conflicts)
        // This is just a placeholder to store the category
        Random rand = new Random();
        int tempId = -1000000 - rand.nextInt(1000000);
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(tempId));  // Convert int to String
            stmt.setString(2, category);
            
            int affected = stmt.executeUpdate();
            return affected > 0;
        }
    } catch (SQLException | ClassNotFoundException e) {
        System.err.println("Error adding category: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    
    public static class Product {
        private long id;
        private String productId;
        private String category;
        private String productName;
        private double price;
        private int quantity;
        private String createdAt;
        private String updatedAt;
        private String status;
        private byte[] productImage;

        public Product(long id, String productId, String category, String productName, double price, int quantity, String createdAt, String updatedAt, String status, byte[] productImage) {
            this.id = id;
            this.productId = productId;
            this.category = category;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.status = status;
            this.productImage = productImage;
        }

        public long getId() {
            return id;
        }

        public String getProductId() {
            return productId;
        }

        public String getCategory() {
            return category;
        }

        public String getProductName() {
            return productName;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getStatus() {
            return status;
        }

        public byte[] getProductImage() {
            return productImage;
        }
    }

    public static class ProductTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "PROD ID", "CTG", "PROD NAME", "PRICE", "QUANTITY", "STATUS", "CRT AT", "UPD AT"};
        private List<Product> products;

        public ProductTableModel(List<Product> products) {
            this.products = products;
        }
        
        public List<Product> getProducts() {
            return products;
        }
        
        @Override
        public int getRowCount() {
            return products.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Product product = products.get(rowIndex);
            switch (columnIndex) {
                case 0: return product.getId();
                case 1: return product.getProductId();
                case 2: return product.getCategory();
                case 3: return product.getProductName();
                case 4: return product.getPrice();
                case 5: return product.getQuantity();
                case 6: return product.getStatus();
                case 7: return product.getCreatedAt();
                case 8: return product.getUpdatedAt();
                default: throw new IllegalArgumentException("Invalid column index");
            }
        }

        public void setProducts(List<Product> products) {
            this.products = products;
            fireTableDataChanged();
        }
    }
}
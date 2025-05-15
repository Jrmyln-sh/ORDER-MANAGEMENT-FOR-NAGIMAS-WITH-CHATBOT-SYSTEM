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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DatabaseConnection.DBConnection;
/**
 *
 * @author Jeremy Malana
 */
public class Dashboard {
    
    private static final String PROMO_FILE_PATH = "src/Promo/current_promo.jpg";
    private File selectedPromoFile;

    
    public File selectPromoImage() {
        JFileChooser fileChooser = new JFileChooser(new File("C:\\Users\\Jeremy Malana\\Documents\\NetBeansProjects\\OMS\\src\\Promo"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Select Promo Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
                       name.endsWith(".png") || name.endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });
        
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedPromoFile = fileChooser.getSelectedFile();
            return selectedPromoFile;
        }
        return null;
    }
    
    public void displayPromoImage(JLabel promoLabel, File file) {
        if (file != null && file.exists()) {
            ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(
                    promoLabel.getWidth(), 
                    promoLabel.getHeight(), 
                    Image.SCALE_SMOOTH);
            
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            promoLabel.setIcon(scaledIcon);
        }
    }
    
    public boolean uploadPromoImage() {
        if (selectedPromoFile == null || !selectedPromoFile.exists()) {
            return false;
        }
        
        try {
            Path directory = Paths.get("src/Promo");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Path destination = Paths.get(PROMO_FILE_PATH);
            Files.copy(selectedPromoFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving promo image: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean loadPromoImageForCustomer(JLabel displayLabel) {
        File promoFile = new File(PROMO_FILE_PATH);
        if (promoFile.exists()) {
            ImageIcon originalIcon = new ImageIcon(promoFile.getAbsolutePath());
            Image image = originalIcon.getImage();
            
            Image scaledImage = image.getScaledInstance(1170, 590, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            
            displayLabel.setIcon(scaledIcon);
            return true;
        }
        return false;
    }
    
    public int getTotalCustomers() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) as total FROM customers";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    public int getTotalGuests() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) as total FROM guest_customers";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    public int getTotalOrders() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT (SELECT COUNT(*) FROM customer_orders) + " +
                          "(SELECT COUNT(*) FROM guest_orders) as total";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    public int getTotalProducts() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) as total FROM products";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    public double getTotalSales() throws SQLException, ClassNotFoundException {
        double totalSales = 0;
        
        try (Connection conn = DBConnection.getConnection()) {
            String customerQuery = 
                "SELECT SUM(oi.price * oi.quantity) as total " +
                "FROM customer_orders o " +
                "JOIN customer_order_items oi ON o.customer_order_id = oi.order_id " +
                "WHERE o.order_status = 'Completed'";
            
            PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
            ResultSet customerRs = customerStmt.executeQuery();
            
            if (customerRs.next() && customerRs.getObject("total") != null) {
                totalSales += customerRs.getDouble("total");
            }
            
            String guestQuery = 
                "SELECT SUM(oi.price * oi.quantity) as total " +
                "FROM guest_orders o " +
                "JOIN guest_order_items oi ON o.guest_order_id = oi.order_id " +
                "WHERE o.order_status = 'Completed'";
            
            PreparedStatement guestStmt = conn.prepareStatement(guestQuery);
            ResultSet guestRs = guestStmt.executeQuery();
            
            if (guestRs.next() && guestRs.getObject("total") != null) {
                totalSales += guestRs.getDouble("total");
            }
        }
        
        return totalSales;
    }
    
    public int getTotalOrdersPending() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = 
                "SELECT (SELECT COUNT(*) FROM customer_orders WHERE order_status = 'Pending') + " +
                "(SELECT COUNT(*) FROM guest_orders WHERE order_status = 'Pending') as total";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    

    public String formatCurrency(double amount) {
        return String.format("%,.2f", amount);
    }
    
    public void updateDashboardStats(
            JLabel totalCustomersLabel,
            JLabel totalGuestLabel, 
            JLabel totalOrdersLabel,
            JLabel totalProductsLabel,
            JLabel totalSalesLabel,
            JLabel totalOrdersPendingLabel) {
        
        try {
            int totalCustomers = getTotalCustomers();
            int totalGuests = getTotalGuests();
            int totalOrders = getTotalOrders();
            int totalProducts = getTotalProducts();
            double totalSales = getTotalSales();
            int totalOrdersPending = getTotalOrdersPending();
            
            totalCustomersLabel.setText("TOTAL CUSTOMERS: " + totalCustomers);
            totalGuestLabel.setText("TOTAL GUESTS: " + totalGuests);
            totalOrdersLabel.setText("TOTAL ORDERS: " + totalOrders);
            totalProductsLabel.setText("TOTAL PRODUCTS: " + totalProducts);
            totalSalesLabel.setText("TOTAL SALES: " + formatCurrency(totalSales));
            totalOrdersPendingLabel.setText("PENDING ORDERS: " + totalOrdersPending);
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating dashboard stats: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error updating dashboard statistics: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
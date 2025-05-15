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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Jeremy Malana
 */
public class Orders {

public void displayOrderManage(JTable orderManageTable) {
    DefaultTableModel model = (DefaultTableModel) orderManageTable.getModel();
    model.setRowCount(0);

    if (model.getColumnCount() == 0) {
        model.setColumnIdentifiers(new Object[]{
            "Transaction Number", "Customer/Guest ID", "Customer Name", "Product Name", 
            "Quantity", "Total Price", "Order Status", "Order Type"
        });
    }

    Connection conn = null;
    try {
        conn = DBConnection.getConnection();
        
        // Fixed SQL query for customer orders - ensure correct join on customer_id
        String customerSql = 
            "SELECT o.transaction_number, o.customer_id AS id, c.customer_username AS username, 'Customer' AS user_type, " +
            "p.product_name, oi.quantity, oi.price AS total_price, o.order_status, o.order_type " +
            "FROM customer_orders o " +
            "JOIN customer_order_items oi ON o.customer_order_id = oi.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "JOIN customers c ON o.customer_id = c.customer_id";
        
        PreparedStatement customerStmt = conn.prepareStatement(customerSql);
        ResultSet customerRs = customerStmt.executeQuery();
        addOrdersToTable(model, customerRs, "Customer");
        
        // Keep guest orders query as is
        String guestSql = 
            "SELECT o.transaction_number, o.guest_id AS id, gc.guest_name_customer AS username, 'Guest' AS user_type, " +
            "p.product_name, oi.quantity, oi.price AS total_price, o.order_status, o.order_type " +
            "FROM guest_orders o " +
            "JOIN guest_order_items oi ON o.guest_order_id = oi.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "JOIN guest_customers gc ON o.guest_id = gc.guest_id";
        
        PreparedStatement guestStmt = conn.prepareStatement(guestSql);
        ResultSet guestRs = guestStmt.executeQuery();
        addOrdersToTable(model, guestRs, "Guest");
        
    } catch (SQLException | ClassNotFoundException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            "Database error: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
    
    private void addOrdersToTable(DefaultTableModel model, ResultSet rs, String userType) throws SQLException {
        while (rs.next()) {
            String transactionNumber = rs.getString("transaction_number");
            long id = rs.getLong("id");
            String username = rs.getString("username");
            String productName = rs.getString("product_name");
            int quantity = rs.getInt("quantity");
            double totalPrice = rs.getDouble("total_price");
            String orderStatus = rs.getString("order_status");
            String orderType = rs.getString("order_type");
            String displayId = userType + " #" + id;
            
            model.addRow(new Object[]{
                transactionNumber, 
                displayId,
                username,
                productName, 
                quantity, 
                totalPrice, 
                orderStatus,
                orderType
            });
        }
    }

    public void displayOrderProcess(JTable orderProcessTable) {
        DefaultTableModel model = (DefaultTableModel) orderProcessTable.getModel();
        model.setRowCount(0);

        if (model.getColumnCount() == 0) {
            model.setColumnIdentifiers(new Object[]{
                "Transaction Number", "Order Status", "Order Type"
            });
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            
            String customerSql = 
                "SELECT transaction_number, order_status, order_type " +
                "FROM customer_orders " +
                "ORDER BY customer_order_id DESC";
            
            PreparedStatement customerStmt = conn.prepareStatement(customerSql);
            ResultSet customerRs = customerStmt.executeQuery();
            
            while (customerRs.next()) {
                String transactionNumber = customerRs.getString("transaction_number");
                String orderStatus = customerRs.getString("order_status");
                String orderType = customerRs.getString("order_type");
                
                model.addRow(new Object[]{
                    transactionNumber,
                    orderStatus,
                    orderType
                });
            }
            
            String guestSql = 
                "SELECT transaction_number, order_status, order_type " +
                "FROM guest_orders " +
                "ORDER BY guest_order_id DESC";
            
            PreparedStatement guestStmt = conn.prepareStatement(guestSql);
            ResultSet guestRs = guestStmt.executeQuery();
            
            while (guestRs.next()) {
                String transactionNumber = guestRs.getString("transaction_number");
                String orderStatus = guestRs.getString("order_status");
                String orderType = guestRs.getString("order_type");
                
                model.addRow(new Object[]{
                    transactionNumber,
                    orderStatus,
                    orderType
                });
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Database error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public double calculateTotalPrice(String transactionNumber) throws SQLException, ClassNotFoundException {
        double totalPrice = 0.0;
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            
            String customerSql = 
                "SELECT SUM(oi.price * oi.quantity) AS total_price " +
                "FROM customer_orders o " +
                "JOIN customer_order_items oi ON o.customer_order_id = oi.order_id " +
                "WHERE o.transaction_number = ?";
            
            PreparedStatement customerStmt = conn.prepareStatement(customerSql);
            customerStmt.setString(1, transactionNumber);
            ResultSet customerRs = customerStmt.executeQuery();
            
            if (customerRs.next() && customerRs.getObject("total_price") != null) {
                totalPrice = customerRs.getDouble("total_price");
            }
            
            if (totalPrice == 0.0) {
                String guestSql = 
                    "SELECT SUM(oi.price * oi.quantity) AS total_price " +
                    "FROM guest_orders o " +
                    "JOIN guest_order_items oi ON o.guest_order_id = oi.order_id " +
                    "WHERE o.transaction_number = ?";
                
                PreparedStatement guestStmt = conn.prepareStatement(guestSql);
                guestStmt.setString(1, transactionNumber);
                ResultSet guestRs = guestStmt.executeQuery();
                
                if (guestRs.next() && guestRs.getObject("total_price") != null) {
                    totalPrice = guestRs.getDouble("total_price");
                }
            }
            
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return totalPrice;
    }
    
    public String getCurrentOrderStatus(String transactionNumber) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            
            String customerSql = "SELECT order_status FROM customer_orders WHERE transaction_number = ?";
            PreparedStatement customerStmt = conn.prepareStatement(customerSql);
            customerStmt.setString(1, transactionNumber);
            ResultSet customerRs = customerStmt.executeQuery();
            
            if (customerRs.next()) {
                return customerRs.getString("order_status");
            }
            
            String guestSql = "SELECT order_status FROM guest_orders WHERE transaction_number = ?";
            PreparedStatement guestStmt = conn.prepareStatement(guestSql);
            guestStmt.setString(1, transactionNumber);
            ResultSet guestRs = guestStmt.executeQuery();
            
            if (guestRs.next()) {
                return guestRs.getString("order_status");
            }
            
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return null;
    }
    
    public boolean updateOrderStatus(String transactionNumber, String newStatus) 
            throws SQLException, ClassNotFoundException {
        
        String currentStatus = getCurrentOrderStatus(transactionNumber);
        
        if (currentStatus != null && (currentStatus.equals("Completed") || currentStatus.equals("Cancelled"))) {
            JOptionPane.showMessageDialog(null, 
                "Cannot change status of orders that are already " + currentStatus + ".", 
                "Status Change Not Allowed", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            String customerSql = "UPDATE customer_orders SET order_status = ? WHERE transaction_number = ?";
            PreparedStatement customerStmt = conn.prepareStatement(customerSql);
            customerStmt.setString(1, newStatus);
            customerStmt.setString(2, transactionNumber);
            int customerUpdated = customerStmt.executeUpdate();
            
            if (customerUpdated == 0) {
                String guestSql = "UPDATE guest_orders SET order_status = ? WHERE transaction_number = ?";
                PreparedStatement guestStmt = conn.prepareStatement(guestSql);
                guestStmt.setString(1, newStatus);
                guestStmt.setString(2, transactionNumber);
                guestStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
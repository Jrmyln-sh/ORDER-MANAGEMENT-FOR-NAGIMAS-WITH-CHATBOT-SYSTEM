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
import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author Jeremy Malana
 */
public class Accounts {

    public void insertAccount(long customerId, String name, String username, String password, String email) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO customers (customer_id, customer_name, customer_username, customer_password, email) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            pstmt.setLong(1, customerId);
            pstmt.setString(2, name);
            pstmt.setString(3, username);
            pstmt.setString(4, hashedPassword);
            pstmt.setString(5, email);
            pstmt.executeUpdate();
        }
    }

    public void updateAccount(long customerId, String name, String username, String password, String email) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE customers SET customer_name = ?, customer_username = ?, customer_password = ?, email = ? WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, email);
            pstmt.setLong(5, customerId);
            pstmt.executeUpdate();
        }
    }

    public void deleteAccount(long customerId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            pstmt.executeUpdate();
        }
    }

    public List<Customer> getAllAccounts() throws SQLException, ClassNotFoundException {
        String sql = "SELECT id, customer_id, customer_name, customer_username, email, created_at, updated_at FROM customers";
        List<Customer> customers = new ArrayList<>();
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                long id = rs.getLong("id");
                long customerId = rs.getLong("customer_id");
                String name = rs.getString("customer_name");
                String username = rs.getString("customer_username");
                String email = rs.getString("email");
                String createdAt = rs.getString("created_at");
                String updatedAt = rs.getString("updated_at");
    
                customers.add(new Customer(id, customerId, name, username, email, createdAt, updatedAt));
            }
        }
        return customers;
    }
    
    public int getTotalCustomers() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS total FROM customers";
        int total = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }
        }

        return total;
    }

public static class Customer {
    private long id;
    private long customerId;
    private String customerName;
    private String customerUsername;
    private String email;
    private String createdAt;
    private String updatedAt;

    public Customer(long id, long customerId, String customerName, String customerUsername, String email, String createdAt, String updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerUsername = customerUsername;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}

    public static class CustomerTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "CST ID", "CST NAME", "CST USERNAME", "EMAIL", "CRT AT", "UPD AT"};
        private List<Customer> customers;
    
        public CustomerTableModel(List<Customer> customers) {
            this.customers = customers;
        }
    
        @Override
        public int getRowCount() {
            return customers.size();
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
            Customer customer = customers.get(rowIndex);
            switch (columnIndex) {
                case 0: return customer.getId();
                case 1: return customer.getCustomerId();
                case 2: return customer.getCustomerName();
                case 3: return customer.getCustomerUsername();
                case 4: return customer.getEmail();
                case 5: return customer.getCreatedAt();
                case 6: return customer.getUpdatedAt();
                default: throw new IllegalArgumentException("Invalid column index");
            }
        }
    
        public void setCustomers(List<Customer> customers) {
            this.customers = customers;
            fireTableDataChanged();
        }
    }
    
    public void insertAdmin(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO admin_users (username_admin, password_admin) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
        }
    }

    public void updateAdmin(long id, String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE admin_users SET username_admin = ?, password_admin = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteAdmin(long id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM admin_users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Admin> getAllAdmins() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM admin_users";
        List<Admin> admins = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String username = rs.getString("username_admin");
                String createdAt = rs.getString("created_at");
                String updatedAt = rs.getString("updated_at");

                admins.add(new Admin(id, username, createdAt, updatedAt));
            }
        }

        return admins;
    }

    public static class Admin {
        private long id;
        private String username;
        private String createdAt;
        private String updatedAt;

        public Admin(long id, String username, String createdAt, String updatedAt) {
            this.id = id;
            this.username = username;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }

    public static class AdminTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "USERNAME", "CRT AT", "UPD AT"};
        private List<Admin> admins;

        public AdminTableModel(List<Admin> admins) {
            this.admins = admins;
        }

        @Override
        public int getRowCount() {
            return admins.size();
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
            Admin admin = admins.get(rowIndex);
            switch (columnIndex) {
                case 0: return admin.getId();
                case 1: return admin.getUsername();
                case 2: return admin.getCreatedAt();
                case 3: return admin.getUpdatedAt();
                default: throw new IllegalArgumentException("Invalid column index");
            }
        }

        public void setAdmins(List<Admin> admins) {
            this.admins = admins;
            fireTableDataChanged();
        }
    }
}
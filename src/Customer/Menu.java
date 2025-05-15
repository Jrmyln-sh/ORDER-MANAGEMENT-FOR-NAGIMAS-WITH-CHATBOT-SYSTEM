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
package Customer;

import Admin.Products;
import Authenticate.Main_Authenticate;
import DatabaseConnection.DBConnection;
import java.awt.Image;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.table.JTableHeader;
import java.awt.Font;
import java.awt.Color;
import javax.swing.table.TableCellRenderer;
import java.awt.GridBagLayout;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;
/**
 *
 * @author Jeremy Malana
 */
public class Menu {

    private List<Products.Product> products;

    public Menu() {
        loadProducts();
    }
    
    private class SpinnerCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JSpinner spinner;
        private final SpinnerNumberModel spinnerModel;
        
        public SpinnerCellEditor(int min, int max, int step) {
            // Create spinner model with constraints
            spinnerModel = new SpinnerNumberModel(1, min, max, step);
            spinner = new JSpinner(spinnerModel);
            JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
            editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
            spinner.setUI(new CustomSpinnerUI());
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setEditable(true);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            if (value instanceof Integer) {
                spinner.setValue(value);
            } else {
                try {
                    spinner.setValue(Integer.parseInt(value.toString()));
                } catch (NumberFormatException e) {
                    spinner.setValue(1);
                }
            }
            
            return spinner;
        }
        
        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
        
        @Override
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                return true;
            }
            return super.isCellEditable(e);
        }
        
        private class CustomSpinnerUI extends BasicSpinnerUI {
            @Override
            protected Component createNextButton() {
                JButton button = new JButton("+");
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
                installNextButtonListeners(button);
                return button;
            }
            
            @Override
            protected Component createPreviousButton() {
                JButton button = new JButton("-");
                button.setFont(new Font("Arial", Font.BOLD, 16));
                button.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
                installPreviousButtonListeners(button);
                return button;
            }
        }
    }

    private void loadProducts() {
        try {
            Products productsClass = new Products();
            products = productsClass.getAvailableProducts();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private class LargeCheckBoxRenderer implements TableCellRenderer {
        private final JPanel panel;
        private final JCheckBox checkbox;
        
        public LargeCheckBoxRenderer() {
            panel = new JPanel(new GridBagLayout());
            checkbox = new JCheckBox();
            
            checkbox.setIcon(new LargeCheckBoxIcon(18, false));
            checkbox.setSelectedIcon(new LargeCheckBoxIcon(18, true));
            
            panel.add(checkbox);
            panel.setBorder(BorderFactory.createEmptyBorder());
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            checkbox.setSelected(Boolean.TRUE.equals(value));
            
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            
            return panel;
        }
        
    
        private class LargeCheckBoxIcon implements Icon {
            private final int size;
            private final boolean selected;
            
            public LargeCheckBoxIcon(int size, boolean selected) {
                this.size = size;
                this.selected = selected;
            }
            
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                
    
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, size, size);
    
                g2d.setColor(new Color(216, 207, 188));
                g2d.fillRect(x + 1, y + 1, size - 1, size - 1);
                
                if (selected) {
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2));
                    
                    g2d.drawLine(x + 3, y + size/2, x + size/2 - 2, y + size - 4);
                    g2d.drawLine(x + size/2 - 2, y + size - 4, x + size - 3, y + 3);
                }
                g2d.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return size;
            }
            
            @Override
            public int getIconHeight() {
                return size;
            }
        }
    }
    
    public void fillAllProductsTable(JTable table) {
        loadProducts();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"", "PRODUCT NAME", "PRICE", "QUANTITY", "SELECT"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return ImageIcon.class;
                    case 3:
                        return Integer.class;
                    case 4:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

        for (Products.Product product : products) {
            if (product.getQuantity() > 0 && "Available".equalsIgnoreCase(product.getStatus())) {
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(product.getProductImage()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                model.addRow(new Object[]{imageIcon, product.getProductName(), product.getPrice(), 1, false});
            }
        }

        table.setModel(model);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        
        table.getColumnModel().getColumn(3).setCellEditor(new SpinnerCellEditor(1, 99, 1));
        setBoldTableHeaders(table);
        table.getColumnModel().getColumn(4).setCellRenderer(new LargeCheckBoxRenderer());
        table.setRowHeight(100);
    }
    
    private void setBoldTableHeaders(JTable table) {
        JTableHeader header = table.getTableHeader();
        Font headerFont = header.getFont();
    
        Font boldHeaderFont = new Font(headerFont.getName(), Font.BOLD, headerFont.getSize());
        header.setFont(boldHeaderFont);
        
        header.setForeground(Color.BLACK);
    }

    public boolean filterProductsByName(JTable table, String searchText) {
        loadProducts();
        
        DefaultTableModel model = new DefaultTableModel(new Object[]{"", "PRODUCT NAME", "PRICE", "QUANTITY", "SELECT"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return ImageIcon.class;
                    case 3:
                        return Integer.class;
                    case 4:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

        boolean foundMatches = false;
        String searchLower = searchText.toLowerCase().trim();
        
        for (Products.Product product : products) {
            if (product.getQuantity() > 0 && 
                "Available".equalsIgnoreCase(product.getStatus()) &&
                product.getProductName().toLowerCase().contains(searchLower)) {
                
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(product.getProductImage()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                model.addRow(new Object[]{imageIcon, product.getProductName(), product.getPrice(), 1, false});
                foundMatches = true;
            }
        }

        table.setModel(model);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        
        table.getColumnModel().getColumn(3).setCellEditor(new SpinnerCellEditor(1, 99, 1));
        setBoldTableHeaders(table);
        table.getColumnModel().getColumn(4).setCellRenderer(new LargeCheckBoxRenderer());
        table.setRowHeight(100);
        
        return foundMatches;
    }

    public void checkout(JTable menuTable, JTable checkoutTable, JLabel totalLabel) {
        DefaultTableModel menuModel = (DefaultTableModel) menuTable.getModel();
        DefaultTableModel checkoutModel = (DefaultTableModel) checkoutTable.getModel();

        if (checkoutModel.getColumnCount() == 0) {
            checkoutModel.setColumnIdentifiers(new Object[]{"PRODUCT NAME", "PRICE", "QUANTITY", "TOTAL", "REMOVE"});
        }

        double totalAmount = 0.0;

        for (int i = 0; i < menuModel.getRowCount(); i++) {
            Boolean isSelected = (Boolean) menuModel.getValueAt(i, 4);
            if (isSelected != null && isSelected) {
                String productName = (String) menuModel.getValueAt(i, 1);
                double price = Double.parseDouble(menuModel.getValueAt(i, 2).toString());
                int quantity = Integer.parseInt(menuModel.getValueAt(i, 3).toString());

                int existingRow = -1;
                for (int j = 0; j < checkoutModel.getRowCount(); j++) {
                    if (checkoutModel.getValueAt(j, 0).equals(productName)) {
                        existingRow = j;
                        break;
                    }
                }
                if (existingRow >= 0) {
                    int currentQty = Integer.parseInt(checkoutModel.getValueAt(existingRow, 2).toString());
                    int newQty = currentQty + quantity;
                    double total = price * newQty;

                    checkoutModel.setValueAt(newQty, existingRow, 2);
                    checkoutModel.setValueAt(total, existingRow, 3);
                } else {
                    double total = price * quantity;
                    checkoutModel.addRow(new Object[]{
                        productName,
                        price,
                        quantity,
                        total,
                        Boolean.FALSE
                    });
                }
                menuModel.setValueAt(false, i, 4);
            }
        }

        totalAmount = 0.0;
        for (int i = 0; i < checkoutModel.getRowCount(); i++) {
            totalAmount += Double.parseDouble(checkoutModel.getValueAt(i, 3).toString());
        }

        totalLabel.setText(String.format("TOTAL PRICE: %.2f", totalAmount));
    }

    public void removeSelectedItems(JTable checkoutTable, JLabel totalLabel) {
        DefaultTableModel model = (DefaultTableModel) checkoutTable.getModel();

        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            Boolean isRemove = (Boolean) model.getValueAt(i, 4);
            if (isRemove != null && isRemove) {
                model.removeRow(i);
            }
        }
        double totalAmount = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            double rowTotal = Double.parseDouble(model.getValueAt(i, 3).toString());
            totalAmount += rowTotal;
        }
        totalLabel.setText(String.format("TOTAL PRICE: %.2f", totalAmount));
        if (model.getRowCount() == 0) {
            model.setRowCount(0);
            totalLabel.setText("TOTAL PRICE: 0.00");
        }
    }

    public void confirmAndProcessOrder(JTable checkoutTable, JLabel totalLabel, String customerUsername, boolean isGuest, String orderType) {
        DefaultTableModel checkoutModel = (DefaultTableModel) checkoutTable.getModel();
        if (checkoutModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String totalText = totalLabel.getText().replaceAll("[^0-9.]", "");
        double totalPrice = Double.parseDouble(totalText);
        
        int response = JOptionPane.showConfirmDialog(
            null, 
            "Are you sure you want to order?\n" +
            "Total Amount: ₱" + String.format("%.2f", totalPrice) + "\n" + 
            "Order Type: " + orderType,
            "Confirm Order", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            Connection conn = null;
            try {
                conn = DBConnection.getConnection();
                conn.setAutoCommit(false);
                

                long customerId = Main_Authenticate.getLoggedInCustomerId();
                long guestId = Main_Authenticate.getLoggedInGuestId();
                
                boolean isCustomerLoggedIn = customerId > 0;
                boolean isGuestLoggedIn = guestId > 0;
                
                System.out.println("Customer ID: " + customerId);
                System.out.println("Guest ID: " + guestId);
                System.out.println("Is Customer Logged In: " + isCustomerLoggedIn);
                System.out.println("Is Guest Logged In: " + isGuestLoggedIn);
                System.out.println("Order Type: " + orderType);
                
                if (!isCustomerLoggedIn && !isGuestLoggedIn) {
                    throw new SQLException("No valid user ID found for order");
                }

                String transactionNumber;
                if (isCustomerLoggedIn) {
                    transactionNumber = generateTransactionNumber(customerId);
                } else {
                    transactionNumber = generateTransactionNumber(guestId);
                }
                
                long orderId;

                if (isCustomerLoggedIn) {
                    orderId = saveCustomerOrder(conn, customerId, transactionNumber, orderType);
                } else {
                    orderId = saveGuestOrder(conn, guestId, transactionNumber, orderType);
                }

                saveOrderItems(conn, checkoutModel, orderId, isCustomerLoggedIn);

                conn.commit();

                JOptionPane.showMessageDialog(null, 
                    "Order successfully placed! You can see your order process in your account.\n" +
                    "Transaction Number: " + transactionNumber, 
                    "Order Successful", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                checkoutModel.setRowCount(0);
                totalLabel.setText("TOTAL PRICE: 0.00");

            } catch (SQLException | ClassNotFoundException ex) {
                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
                JOptionPane.showMessageDialog(null, "Error processing order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException closeEx) {
                    System.err.println("Error closing connection: " + closeEx.getMessage());
                }
            }
        }
    }
    
    public void confirmAndProcessOrder(JTable checkoutTable, JLabel totalLabel, String customerUsername, boolean isGuest) {
        confirmAndProcessOrder(checkoutTable, totalLabel, customerUsername, isGuest, "Dine-In");
    }
    
    private long saveCustomerOrder(Connection conn, long customerId, String transactionNumber, String orderType) 
            throws SQLException {
        
        String insertOrderSQL = 
            "INSERT INTO customer_orders (customer_id, transaction_number, order_status, order_type) " +
            "VALUES (?, ?, 'Pending', ?)";
        
        PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS);
        orderStmt.setLong(1, customerId);
        orderStmt.setString(2, transactionNumber);
        orderStmt.setString(3, orderType);
        
        System.out.println("Executing SQL for customer: " + insertOrderSQL);
        System.out.println("Parameters: customer_id=" + customerId + 
                          ", transaction_number=" + transactionNumber + 
                          ", order_type=" + orderType);
        
        orderStmt.executeUpdate();
        
        ResultSet generatedKeys = orderStmt.getGeneratedKeys();
        if (!generatedKeys.next()) {
            throw new SQLException("Creating customer order failed, no ID obtained.");
        }
        return generatedKeys.getLong(1);
    }
    
    private long saveGuestOrder(Connection conn, long guestId, String transactionNumber, String orderType) 
            throws SQLException {
        
        String insertOrderSQL = 
            "INSERT INTO guest_orders (guest_id, transaction_number, order_status, order_type) " +
            "VALUES (?, ?, 'Pending', ?)";
        
        PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS);
        orderStmt.setLong(1, guestId);
        orderStmt.setString(2, transactionNumber);
        orderStmt.setString(3, orderType);
        
        System.out.println("Executing SQL for guest: " + insertOrderSQL);
        System.out.println("Parameters: guest_id=" + guestId + 
                          ", transaction_number=" + transactionNumber + 
                          ", order_type=" + orderType);
        
        orderStmt.executeUpdate();
        
        ResultSet generatedKeys = orderStmt.getGeneratedKeys();
        if (!generatedKeys.next()) {
            throw new SQLException("Creating guest order failed, no ID obtained.");
        }
        return generatedKeys.getLong(1);
    }
    
    private void saveOrderItems(Connection conn, DefaultTableModel checkoutModel, long orderId, boolean isCustomer) throws SQLException {
        String insertOrderItemsSQL;
        if (isCustomer) {
            insertOrderItemsSQL = "INSERT INTO customer_order_items (order_id, product_id, price, quantity) VALUES (?, ?, ?, ?)";
        } else {
            insertOrderItemsSQL = "INSERT INTO guest_order_items (order_id, product_id, price, quantity) VALUES (?, ?, ?, ?)";
        }
        
        PreparedStatement orderItemsStmt = conn.prepareStatement(insertOrderItemsSQL);
        
        for (int i = 0; i < checkoutModel.getRowCount(); i++) {
            String productName = checkoutModel.getValueAt(i, 0).toString();
            double price = Double.parseDouble(checkoutModel.getValueAt(i, 1).toString());
            int quantity = Integer.parseInt(checkoutModel.getValueAt(i, 2).toString());
            
            String productId = getProductIdByName(productName, conn);
            
            orderItemsStmt.setLong(1, orderId);
            orderItemsStmt.setString(2, productId);
            orderItemsStmt.setDouble(3, price);
            orderItemsStmt.setInt(4, quantity);
            orderItemsStmt.addBatch();
            updateProductQuantity(productId, quantity, conn);
        }
        
        orderItemsStmt.executeBatch();
    }

    private void updateProductQuantity(String productId, int quantityOrdered, Connection conn) throws SQLException {
        String updateQuantitySQL = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuantitySQL)) {
            updateStmt.setInt(1, quantityOrdered);
            updateStmt.setString(2, productId);
            updateStmt.executeUpdate();
        }
    }

    private String getProductIdByName(String name, Connection conn) throws SQLException {
        String sql = "SELECT product_id FROM products WHERE product_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("product_id");
            } else {
                throw new SQLException("Product not found: " + name);
            }
        }
    }

    private String generateTransactionNumber(long id) throws SQLException, ClassNotFoundException {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int orderCount = getOrderCountForCustomerOrGuest(id);
        String orderSuffix = String.format("%03d", orderCount + 1);
        return timestamp + "-" + orderSuffix;
    }

    private int getOrderCountForCustomerOrGuest(long id) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT COUNT(*) AS order_count FROM customer_orders WHERE customer_id = ? "
                   + "UNION "
                   + "SELECT COUNT(*) AS order_count FROM guest_orders WHERE guest_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setLong(1, id);
        pstmt.setLong(2, id);
        ResultSet rs = pstmt.executeQuery();
        int orderCount = 0;
        while (rs.next()) {
            orderCount += rs.getInt("order_count");
        }
        rs.close();
        pstmt.close();
        conn.close();
        return orderCount;
    }

    public void displayOrderProcess(JTable orderTable) {
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        model.setRowCount(0);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT transaction_number, order_status, order_type FROM customer_orders WHERE customer_id = ? "
                       + "UNION "
                       + "SELECT transaction_number, order_status, order_type FROM guest_orders WHERE guest_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Main_Authenticate.getLoggedInCustomerId());
            pstmt.setLong(2, Main_Authenticate.getLoggedInGuestId());

            System.out.println("Customer ID: " + Main_Authenticate.getLoggedInCustomerId());
            System.out.println("Guest ID: " + Main_Authenticate.getLoggedInGuestId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String transactionNumber = rs.getString("transaction_number");
                String orderStatus = rs.getString("order_status");
                String orderType = rs.getString("order_type");

                System.out.println("Transaction Number: " + transactionNumber);
                System.out.println("Order Status: " + orderStatus);
                System.out.println("Order Type: " + orderType);

                model.addRow(new Object[]{transactionNumber, orderStatus, orderType});
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
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
    
    public void removeSelectedItemsWithConfirmation(java.awt.Component parent, JTable checkoutTable, JLabel totalLabel) {
        DefaultTableModel model = (DefaultTableModel) checkoutTable.getModel();
        int selectedCount = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isRemove = (Boolean) model.getValueAt(i, 4);
            if (isRemove != null && isRemove) {
                selectedCount++;
            }
        }
        

        if (selectedCount == 0) {
            JOptionPane.showMessageDialog(parent, 
                "Please select items to remove by checking the boxes in the REMOVE column.", 
                "No Items Selected", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        

        String message = selectedCount == 1 
            ? "Are you sure want to remove this?" 
            : "Are you sure want to remove these " + selectedCount + " items?";
            
        int response = JOptionPane.showConfirmDialog(
            parent, 
            message, 
            "Confirm Removal", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {

            removeSelectedItems(checkoutTable, totalLabel);
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import Authenticate.LoginPageForm;

/**
 *
 * @author Jeremy Malana
 */
public class AdminDashboard extends javax.swing.JFrame {
    private Accounts accounts;
    private Products products;
    private Orders orders;
    private Accounts.CustomerTableModel customerTableModel;
    private Accounts.AdminTableModel adminTableModel;
    private Products.ProductTableModel productTableModel;
    private String username;
    private File selectedImageFile;
    private File selectedPromoFile;
    private Dashboard dashboard;
    private Reports reports;
    /**
     * Creates new form AdminDashboard
     */
    public AdminDashboard() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        
        accounts = new Accounts();
        products = new Products();
        orders = new Orders();
        dashboard = new Dashboard();
        updateDashboardStatistics();
        reports = new Reports();
        
        orders.displayOrderManage(jTable1_DisplayOrderManage);

        loadCategoriesIntoComboBox();
        
        jComboBox3_ReportTypeDateInventory.removeAllItems();
        jComboBox3_ReportTypeDateInventory.addItem("DAILY");
        jComboBox3_ReportTypeDateInventory.addItem("WEEKLY");
        jComboBox3_ReportTypeDateInventory.addItem("MONTHLY");
        jComboBox3_ReportTypeDateInventory.addItem("YEARLY");
        jComboBox3_ReportTypeDateInventory.setSelectedIndex(0);
        
        jComboBox2_ReportTypeDateCustomers.removeAllItems();
        jComboBox2_ReportTypeDateCustomers.addItem("DAILY");
        jComboBox2_ReportTypeDateCustomers.addItem("WEEKLY");
        jComboBox2_ReportTypeDateCustomers.addItem("MONTHLY");
        jComboBox2_ReportTypeDateCustomers.addItem("YEARLY");
        jComboBox2_ReportTypeDateCustomers.setSelectedIndex(0);
        
        jComboBox1_ReportTypeDateSales.removeAllItems();
        jComboBox1_ReportTypeDateSales.addItem("DAILY");
        jComboBox1_ReportTypeDateSales.addItem("WEEKLY");
        jComboBox1_ReportTypeDateSales.addItem("MONTHLY");
        jComboBox1_ReportTypeDateSales.addItem("YEARLY");
        jComboBox1_ReportTypeDateSales.setSelectedIndex(0);

        setupOrderTableSelection();
        setupProductTableSelection();
        reports.displayInventoryReports(jTable3_DisplayInventoryReports);
        reports.displayCustomerReports(jTable2_DisplayCustomersReports);
        reports.displaySalesReports(jTable1_DisplaySalesReports);
        
        jTabbedPane2_Manage_Acc.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/Icons/customer.png")));
        jTabbedPane2_Manage_Acc.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/Icons/admin.png")));
        
        jButton1_AceeptOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    acceptOrder();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        jTextField1_TransacNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    calculateTotalPrice();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        jButton1_AddCtg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });
        jButton1_SelectPromo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPromoImage();
            }
        });
        
        jButton1_UploadPromo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadPromoImage();
            }
        });
        jButton3_ExportInventoryReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateRangeType = (String) jComboBox3_ReportTypeDateInventory.getSelectedItem();
                if (dateRangeType == null || dateRangeType.isEmpty()) {
                    dateRangeType = "Daily";
                }
                reports.exportInventoryReportToPdf(jTable3_DisplayInventoryReports, dateRangeType);
            }
        });
        
        jButton2_ExportCustomersReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateRangeType = (String) jComboBox2_ReportTypeDateCustomers.getSelectedItem();
                if (dateRangeType == null || dateRangeType.isEmpty()) {
                    dateRangeType = "Daily";
                }
                reports.exportCustomerReportToPdf(jTable2_DisplayCustomersReports, dateRangeType);
            }
        });
        
        jButton1_ExportSalesReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateRangeType = (String) jComboBox1_ReportTypeDateSales.getSelectedItem();
                if (dateRangeType == null || dateRangeType.isEmpty()) {
                    dateRangeType = "Daily";
                }
                reports.exportSalesReportToPdf(jTable1_DisplaySalesReports, dateRangeType);
            }
        });
    }
    
    private void setupProductTableSelection() {
        jTable1_products_data.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable1_products_data.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
    
                        String productId = jTable1_products_data.getValueAt(selectedRow, 1).toString();
                        String category = jTable1_products_data.getValueAt(selectedRow, 2).toString();
                        String productName = jTable1_products_data.getValueAt(selectedRow, 3).toString();
                        double price = Double.parseDouble(jTable1_products_data.getValueAt(selectedRow, 4).toString());
                        int quantity = Integer.parseInt(jTable1_products_data.getValueAt(selectedRow, 5).toString());
                        String status = jTable1_products_data.getValueAt(selectedRow, 6).toString();
                        
     
                        jTextField1_product_id.setText(productId);
                        jTextField2_product_name.setText(productName);
                        jTextField3_product_price.setText(String.valueOf(price));
                        jTextField4_product_quantity.setText(String.valueOf(quantity));
                        jTextField5_product_status.setText(status);
                        
                        jComboBox1_product_category.setSelectedItem(category);
    
                        Products.Product product = productTableModel.getProducts().get(selectedRow);
                        byte[] imageData = product.getProductImage();
                        if (imageData != null && imageData.length > 0) {
                            ImageIcon imageIcon = new ImageIcon(imageData);
                            Image img = imageIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                            jLabel21_DisplayImage.setIcon(new ImageIcon(img));
    
                            try {
                                File tempFile = File.createTempFile("product_image", ".jpg");
                                Files.write(tempFile.toPath(), imageData);
                                selectedImageFile = tempFile;
                            } catch (IOException ex) {
                                System.err.println("Error creating temp file for image: " + ex.getMessage());
                            }
                        } else {
                            jLabel21_DisplayImage.setIcon(null);
                        }
                        
                    } catch (Exception ex) {
                        System.err.println("Error loading product details: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void setupOrderTableSelection() {
        jTable1_DisplayOrderManage.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable1_DisplayOrderManage.getSelectedRow();
                if (selectedRow >= 0) {
                    String transactionNumber = jTable1_DisplayOrderManage.getValueAt(selectedRow, 0).toString();
                    jTextField1_TransacNum.setText(transactionNumber);
                } else {
                    jTextField1_TransacNum.setText("");
                }
            }
        });
    }
    
    private void updateDashboardStatistics() {
        dashboard.updateDashboardStats(
            jLabel_TotalCustomers,
            jLabel_TotalGuest,
            jLabel_TotalOrders,
            jLabel_TotalProducts,
            jLabel_TotalSales,
            jLabel_TotalOrdersPending
        );
    }
        
    private void calculateTotalPrice() throws SQLException, ClassNotFoundException {
        String transactionNumber = jTextField1_TransacNum.getText();
        if (transactionNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Transaction Number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalPrice = orders.calculateTotalPrice(transactionNumber);
        jLabel25_TotalPrice.setText("" + totalPrice);
    }

    private void acceptOrder() throws SQLException, ClassNotFoundException {
        String transactionNumber = jTextField1_TransacNum.getText();
        if (transactionNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Transaction Number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        calculateTotalPrice();
    
        String status = (String) jComboBox1_Status.getSelectedItem();
        boolean updated = orders.updateOrderStatus(transactionNumber, status);
        
        if (updated) {
            JOptionPane.showMessageDialog(this, "Successfully Updated The Status of Order", "Success", JOptionPane.INFORMATION_MESSAGE);
            orders.displayOrderManage(jTable1_DisplayOrderManage);
        }
    }
    
    private void addCategory() {
        String newCategory = jTextField1_NewCtg.getText().trim();
        if (newCategory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = products.addCategory(newCategory);
        
        if (success) {
            loadCategoriesIntoComboBox();
            
            jComboBox1_product_category.setSelectedItem(newCategory);
            
            jTextField1_NewCtg.setText("");
            JOptionPane.showMessageDialog(this, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add category to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadCategoriesIntoComboBox() {
        try {
            jComboBox1_product_category.removeAllItems();
            
            List<String> categories = products.getAllCategories();
            
            for (String category : categories) {
                jComboBox1_product_category.addItem(category);
            }
        } catch (Exception e) {
            System.err.println("Error loading categories: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public AdminDashboard(String username) {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        
        accounts = new Accounts();
        products = new Products();
        orders = new Orders();
        dashboard = new Dashboard();
        updateDashboardStatistics();
        reports = new Reports();
       
        setupOrderTableSelection();
        setupProductTableSelection();
        
        jComboBox3_ReportTypeDateInventory.removeAllItems();
        jComboBox3_ReportTypeDateInventory.addItem("DAILY");
        jComboBox3_ReportTypeDateInventory.addItem("WEEKLY");
        jComboBox3_ReportTypeDateInventory.addItem("MONTHLY");
        jComboBox3_ReportTypeDateInventory.addItem("YEARLY");
        jComboBox3_ReportTypeDateInventory.setSelectedIndex(0);
        
        jComboBox2_ReportTypeDateCustomers.removeAllItems();
        jComboBox2_ReportTypeDateCustomers.addItem("DAILY");
        jComboBox2_ReportTypeDateCustomers.addItem("WEEKLY");
        jComboBox2_ReportTypeDateCustomers.addItem("MONTHLY");
        jComboBox2_ReportTypeDateCustomers.addItem("YEARLY");
        jComboBox2_ReportTypeDateCustomers.setSelectedIndex(0);
        
        jComboBox1_ReportTypeDateSales.removeAllItems();
        jComboBox1_ReportTypeDateSales.addItem("DAILY");
        jComboBox1_ReportTypeDateSales.addItem("WEEKLY");
        jComboBox1_ReportTypeDateSales.addItem("MONTHLY");
        jComboBox1_ReportTypeDateSales.addItem("YEARLY");
        jComboBox1_ReportTypeDateSales.setSelectedIndex(0);

        loadCategoriesIntoComboBox();
        orders.displayOrderManage(jTable1_DisplayOrderManage);
        reports.displayInventoryReports(jTable3_DisplayInventoryReports);
        reports.displayCustomerReports(jTable2_DisplayCustomersReports);
        reports.displaySalesReports(jTable1_DisplaySalesReports);


        jLabel2_welcome.setText("WELCOME " + username.toUpperCase());
        
        jButton1_AceeptOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    acceptOrder();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        jTextField1_TransacNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    calculateTotalPrice();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        jButton1_AddCtg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });
        jButton1_SelectPromo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPromoImage();
            }
        });
        
        jButton1_UploadPromo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadPromoImage();
            }
        });
        
        jButton3_ExportInventoryReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateRangeType = (String) jComboBox3_ReportTypeDateInventory.getSelectedItem();
                if (dateRangeType == null || dateRangeType.isEmpty()) {
                    dateRangeType = "Daily";
                }
                reports.exportInventoryReportToPdf(jTable3_DisplayInventoryReports, dateRangeType);
            }
        });
        
        jButton2_ExportCustomersReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateRangeType = (String) jComboBox2_ReportTypeDateCustomers.getSelectedItem();
                if (dateRangeType == null || dateRangeType.isEmpty()) {
                    dateRangeType = "Daily";
                }
                reports.exportCustomerReportToPdf(jTable2_DisplayCustomersReports, dateRangeType);
            }
        });
        
        jButton1_ExportSalesReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateRangeType = (String) jComboBox1_ReportTypeDateSales.getSelectedItem();
                if (dateRangeType == null || dateRangeType.isEmpty()) {
                    dateRangeType = "Daily";
                }
                reports.exportSalesReportToPdf(jTable1_DisplaySalesReports, dateRangeType);
            }
        });
    }
    
    private void selectPromoImage() {
        File promoFile = dashboard.selectPromoImage();
        if (promoFile != null) {
            selectedPromoFile = promoFile;
            dashboard.displayPromoImage(jLabel20_Promo, selectedPromoFile);
        }
    }
    
    private void uploadPromoImage() {
        if (selectedPromoFile == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a promo image first", 
                "No Image Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (dashboard.uploadPromoImage()) {
            JOptionPane.showMessageDialog(this, 
                "Promo image uploaded successfully!", 
                "Upload Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to upload promo image", 
                "Upload Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }   

    private void refreshCustomerTable() {
        try {
            List<Accounts.Customer> customers = accounts.getAllAccounts();
            if (customerTableModel == null) {
                customerTableModel = new Accounts.CustomerTableModel(customers);
                jTable1_customer_data.setModel(customerTableModel);
            } else {
                customerTableModel.setCustomers(customers);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error refreshing table: " + e.getMessage());
        }
    }

    private void refreshAdminTable() {
        try {
            List<Accounts.Admin> admins = accounts.getAllAdmins();
            if (adminTableModel == null) {
                adminTableModel = new Accounts.AdminTableModel(admins);
                jTable1_admin_data.setModel(adminTableModel);
            } else {
                adminTableModel.setAdmins(admins);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error refreshing admin table: " + e.getMessage());
        }
    }

private void refreshProductTable() {
    try {
        String selectedProductId = null;
        int selectedRow = jTable1_products_data.getSelectedRow();
        if (selectedRow >= 0) {
            selectedProductId = jTable1_products_data.getValueAt(selectedRow, 1).toString();
        }
        
        List<Products.Product> productsList = products.getAllProducts();
        if (productTableModel == null) {
            productTableModel = new Products.ProductTableModel(productsList);
            jTable1_products_data.setModel(productTableModel);
        } else {
            productTableModel.setProducts(productsList);
        }
        
        if (selectedProductId != null) {
            for (int i = 0; i < jTable1_products_data.getRowCount(); i++) {
                if (selectedProductId.equals(jTable1_products_data.getValueAt(i, 1).toString())) {
                    jTable1_products_data.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
        loadCategoriesIntoComboBox();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error refreshing product table: " + e.getMessage());
    }
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        NavBarPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        DashboardBTN = new javax.swing.JButton();
        OrdersBTN = new javax.swing.JButton();
        ProducsBTN = new javax.swing.JButton();
        AccountsBTN = new javax.swing.JButton();
        ReportsBTN = new javax.swing.JButton();
        LogoutBTN = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2_welcome = new javax.swing.JLabel();
        MainPanel = new javax.swing.JTabbedPane();
        DashboardPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel_TotalGuest = new javax.swing.JLabel();
        jLabel20_Promo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel_TotalProducts = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel_TotalCustomers = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel_TotalOrders = new javax.swing.JLabel();
        jButton1_SelectPromo = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel_TotalSales = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel_TotalOrdersPending = new javax.swing.JLabel();
        jButton1_UploadPromo = new javax.swing.JButton();
        OrdersPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1_DisplayOrderManage = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jComboBox1_Status = new javax.swing.JComboBox<>();
        jButton1_AceeptOrder = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jTextField1_TransacNum = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25_TotalPrice = new javax.swing.JLabel();
        ProductsPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1_products_data = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField1_product_id = new javax.swing.JTextField();
        jTextField2_product_name = new javax.swing.JTextField();
        jTextField3_product_price = new javax.swing.JTextField();
        jTextField4_product_quantity = new javax.swing.JTextField();
        jTextField5_product_status = new javax.swing.JTextField();
        jButton1_product_insert = new javax.swing.JButton();
        jButton2_product_update = new javax.swing.JButton();
        jButton3_product_delete = new javax.swing.JButton();
        jButton4_product_refresh = new javax.swing.JButton();
        CATEGORIES = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jButton1_SelectImage = new javax.swing.JButton();
        jLabel21_DisplayImage = new javax.swing.JLabel();
        jComboBox1_product_category = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jTextField1_NewCtg = new javax.swing.JTextField();
        jButton1_AddCtg = new javax.swing.JButton();
        AcccountsPanel = new javax.swing.JPanel();
        jTabbedPane2_Manage_Acc = new javax.swing.JTabbedPane();
        jPanel2_Customers = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1_customer_data = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1_customer_id = new javax.swing.JTextField();
        jTextField2_customer_name = new javax.swing.JTextField();
        jTextField3_customer_username = new javax.swing.JTextField();
        jTextField4_email = new javax.swing.JTextField();
        jPasswordField1_customer_pass = new javax.swing.JPasswordField();
        jButton1_customer_insert = new javax.swing.JButton();
        jButton2_customer_update = new javax.swing.JButton();
        jButton3_customer_delete = new javax.swing.JButton();
        jButton4_customer_refresh = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel3_Admins = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1_admin_data = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jTextField1_admin_id = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField2_admin_user = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPasswordField1_admin_pass = new javax.swing.JPasswordField();
        jButton1_admin_insert = new javax.swing.JButton();
        jButton2_admin_update = new javax.swing.JButton();
        jButton3_admin_delete = new javax.swing.JButton();
        jButton4_admin_refresh = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        ReportsPanel = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        ReportsContainerPanel = new javax.swing.JTabbedPane();
        SalesReports = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable1_DisplaySalesReports = new javax.swing.JTable();
        jButton1_ExportSalesReports = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jComboBox1_ReportTypeDateSales = new javax.swing.JComboBox<>();
        CustomersReports = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable2_DisplayCustomersReports = new javax.swing.JTable();
        jButton2_ExportCustomersReports = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jComboBox2_ReportTypeDateCustomers = new javax.swing.JComboBox<>();
        inventoryReports = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3_DisplayInventoryReports = new javax.swing.JTable();
        jButton3_ExportInventoryReports = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jComboBox3_ReportTypeDateInventory = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusCycleRoot(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NavBarPanel.setBackground(new java.awt.Color(28, 28, 28));
        NavBarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(233, 190, 95));
        jLabel1.setFont(new java.awt.Font("Luckiest Guy", 0, 40)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(233, 190, 95));
        jLabel1.setText("NAGIMAS");
        NavBarPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 57, -1, -1));

        DashboardBTN.setBackground(new java.awt.Color(86, 84, 73));
        DashboardBTN.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        DashboardBTN.setForeground(new java.awt.Color(255, 251, 244));
        DashboardBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/dashboard.png"))); // NOI18N
        DashboardBTN.setText("DASHBOARD");
        DashboardBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 2, true));
        DashboardBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DashboardBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardBTNActionPerformed(evt);
            }
        });
        NavBarPanel.add(DashboardBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 240, 40));

        OrdersBTN.setBackground(new java.awt.Color(86, 84, 73));
        OrdersBTN.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        OrdersBTN.setForeground(new java.awt.Color(255, 251, 244));
        OrdersBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/order.png"))); // NOI18N
        OrdersBTN.setText("ORDERS");
        OrdersBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 2, true));
        OrdersBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        OrdersBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrdersBTNActionPerformed(evt);
            }
        });
        NavBarPanel.add(OrdersBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 240, 40));

        ProducsBTN.setBackground(new java.awt.Color(86, 84, 73));
        ProducsBTN.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        ProducsBTN.setForeground(new java.awt.Color(255, 251, 244));
        ProducsBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/products.png"))); // NOI18N
        ProducsBTN.setText("PRODUCTS");
        ProducsBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 2, true));
        ProducsBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProducsBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProducsBTNActionPerformed(evt);
            }
        });
        NavBarPanel.add(ProducsBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 240, 40));

        AccountsBTN.setBackground(new java.awt.Color(86, 84, 73));
        AccountsBTN.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        AccountsBTN.setForeground(new java.awt.Color(255, 251, 244));
        AccountsBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/customer.png"))); // NOI18N
        AccountsBTN.setText("ACCOUNTS");
        AccountsBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 2, true));
        AccountsBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AccountsBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccountsBTNActionPerformed(evt);
            }
        });
        NavBarPanel.add(AccountsBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 240, 40));

        ReportsBTN.setBackground(new java.awt.Color(86, 84, 73));
        ReportsBTN.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        ReportsBTN.setForeground(new java.awt.Color(255, 251, 244));
        ReportsBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/reports.png"))); // NOI18N
        ReportsBTN.setText("REPORTS");
        ReportsBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 2, true));
        ReportsBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReportsBTNActionPerformed(evt);
            }
        });
        NavBarPanel.add(ReportsBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 240, 40));

        LogoutBTN.setBackground(new java.awt.Color(86, 84, 73));
        LogoutBTN.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        LogoutBTN.setForeground(new java.awt.Color(255, 251, 244));
        LogoutBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/logout.png"))); // NOI18N
        LogoutBTN.setText("LOGOUT");
        LogoutBTN.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 2, true));
        LogoutBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LogoutBTN.setDefaultCapable(false);
        LogoutBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutBTNActionPerformed(evt);
            }
        });
        NavBarPanel.add(LogoutBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, 240, 40));

        jPanel2.setBackground(new java.awt.Color(233, 190, 95));
        jPanel2.setForeground(new java.awt.Color(233, 190, 95));

        jLabel2_welcome.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel2_welcome.setForeground(new java.awt.Color(17, 18, 13));
        jLabel2_welcome.setText("WELCOME");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 52, Short.MAX_VALUE)
                .addComponent(jLabel2_welcome, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel2_welcome)
                .addGap(15, 15, 15))
        );

        NavBarPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 132, 248, -1));

        getContentPane().add(NavBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 960));

        DashboardPanel.setBackground(new java.awt.Color(215, 215, 215));
        DashboardPanel.setForeground(new java.awt.Color(17, 18, 13));
        DashboardPanel.setFont(new java.awt.Font("AvenirNext LT Pro HeavyCn", 1, 55)); // NOI18N

        jLabel17.setFont(new java.awt.Font("AvenirNext LT Pro HeavyCn", 1, 55)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(17, 18, 13));
        jLabel17.setText("ORDER MANAGEMENT DASHBOARD");

        jPanel3.setBackground(new java.awt.Color(233, 190, 95));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel_TotalGuest.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel_TotalGuest.setForeground(new java.awt.Color(30, 30, 30));
        jLabel_TotalGuest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/total-guest.png"))); // NOI18N
        jLabel_TotalGuest.setText("TOTAL  GUEST: ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel_TotalGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 31, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_TotalGuest, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
        );

        jLabel20_Promo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 3, true));

        jPanel4.setBackground(new java.awt.Color(233, 190, 95));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel_TotalProducts.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel_TotalProducts.setForeground(new java.awt.Color(30, 30, 30));
        jLabel_TotalProducts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/total-products.png"))); // NOI18N
        jLabel_TotalProducts.setText("TOTAL PRODUCTS:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel_TotalProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 44, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_TotalProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(233, 190, 95));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel_TotalCustomers.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel_TotalCustomers.setForeground(new java.awt.Color(17, 18, 13));
        jLabel_TotalCustomers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/customer.png"))); // NOI18N
        jLabel_TotalCustomers.setText("TOTAL CUSTOMERS:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel_TotalCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_TotalCustomers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(233, 190, 95));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel_TotalOrders.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel_TotalOrders.setForeground(new java.awt.Color(30, 30, 30));
        jLabel_TotalOrders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/total-orders.png"))); // NOI18N
        jLabel_TotalOrders.setText("TOTAL ORDERS:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel_TotalOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_TotalOrders, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jButton1_SelectPromo.setBackground(new java.awt.Color(233, 190, 95));
        jButton1_SelectPromo.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jButton1_SelectPromo.setForeground(new java.awt.Color(30, 30, 30));
        jButton1_SelectPromo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/select.png"))); // NOI18N
        jButton1_SelectPromo.setText("SELECT IMAGE");
        jButton1_SelectPromo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel7.setBackground(new java.awt.Color(233, 190, 95));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel_TotalSales.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel_TotalSales.setForeground(new java.awt.Color(30, 30, 30));
        jLabel_TotalSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/total-sales.png"))); // NOI18N
        jLabel_TotalSales.setText("TOTAL SALES: ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel_TotalSales, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_TotalSales, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(233, 190, 95));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel_TotalOrdersPending.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel_TotalOrdersPending.setForeground(new java.awt.Color(30, 30, 30));
        jLabel_TotalOrdersPending.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/orders-pending.png"))); // NOI18N
        jLabel_TotalOrdersPending.setText("ORDERS PENDING:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel_TotalOrdersPending, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 36, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_TotalOrdersPending, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
        );

        jButton1_UploadPromo.setBackground(new java.awt.Color(233, 190, 95));
        jButton1_UploadPromo.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jButton1_UploadPromo.setForeground(new java.awt.Color(30, 30, 30));
        jButton1_UploadPromo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/upload.png"))); // NOI18N
        jButton1_UploadPromo.setText("UPLOAD");
        jButton1_UploadPromo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20_Promo, javax.swing.GroupLayout.PREFERRED_SIZE, 1170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(DashboardPanelLayout.createSequentialGroup()
                                .addComponent(jButton1_SelectPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1_UploadPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(39, Short.MAX_VALUE))
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(DashboardPanelLayout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)))
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(23, 23, 23))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addGap(175, 175, 175))
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20_Promo, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1_SelectPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1_UploadPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        MainPanel.addTab("tab1", DashboardPanel);

        OrdersPanel.setBackground(new java.awt.Color(215, 215, 215));

        jLabel21.setFont(new java.awt.Font("AvenirNext LT Pro HeavyCn", 1, 55)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(17, 18, 13));
        jLabel21.setText("MANAGE ORDERS");

        jTable1_DisplayOrderManage.setBackground(new java.awt.Color(216, 207, 188));
        jTable1_DisplayOrderManage.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTable1_DisplayOrderManage.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jTable1_DisplayOrderManage.setForeground(new java.awt.Color(30, 30, 30));
        jTable1_DisplayOrderManage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "TRANSACTION NUMBER", "CUSTOMER ID", "CUSTOMER NAME", "PRODUCT NAME", "QUANTITY", "TOTAL  PRICE", "STATUS", "ORDER TYPE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1_DisplayOrderManage.setRowHeight(30);
        jTable1_DisplayOrderManage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1_DisplayOrderManageMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable1_DisplayOrderManage);
        if (jTable1_DisplayOrderManage.getColumnModel().getColumnCount() > 0) {
            jTable1_DisplayOrderManage.getColumnModel().getColumn(0).setPreferredWidth(150);
        }

        jLabel22.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(30, 30, 30));
        jLabel22.setText("STATUS:");
        jLabel22.setToolTipText("");

        jComboBox1_Status.setBackground(new java.awt.Color(244, 248, 255));
        jComboBox1_Status.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jComboBox1_Status.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox1_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Processing", "Completed", "Cancelled" }));
        jComboBox1_Status.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jButton1_AceeptOrder.setBackground(new java.awt.Color(17, 18, 13));
        jButton1_AceeptOrder.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton1_AceeptOrder.setForeground(new java.awt.Color(255, 251, 244));
        jButton1_AceeptOrder.setText("ACCEPT ORDER");

        jLabel23.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("TRANSACT NO. :");
        jLabel23.setToolTipText("");

        jTextField1_TransacNum.setBackground(new java.awt.Color(244, 248, 255));
        jTextField1_TransacNum.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jTextField1_TransacNum.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1_TransacNum.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel24.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(30, 30, 30));
        jLabel24.setText("TOTAL PRICE:");

        jLabel25_TotalPrice.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel25_TotalPrice.setForeground(new java.awt.Color(30, 30, 30));
        jLabel25_TotalPrice.setToolTipText("");

        javax.swing.GroupLayout OrdersPanelLayout = new javax.swing.GroupLayout(OrdersPanel);
        OrdersPanel.setLayout(OrdersPanelLayout);
        OrdersPanelLayout.setHorizontalGroup(
            OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrdersPanelLayout.createSequentialGroup()
                .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OrdersPanelLayout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(jLabel21))
                    .addGroup(OrdersPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(OrdersPanelLayout.createSequentialGroup()
                                .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox1_Status, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1_TransacNum, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(OrdersPanelLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jButton1_AceeptOrder))
                                    .addGroup(OrdersPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel25_TotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        OrdersPanelLayout.setVerticalGroup(
            OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OrdersPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OrdersPanelLayout.createSequentialGroup()
                        .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1_TransacNum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1_AceeptOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(OrdersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox1_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29))
                    .addGroup(OrdersPanelLayout.createSequentialGroup()
                        .addComponent(jLabel25_TotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(136, 136, 136))))
        );

        MainPanel.addTab("tab2", OrdersPanel);

        ProductsPanel.setBackground(new java.awt.Color(215, 215, 215));
        ProductsPanel.setForeground(new java.awt.Color(17, 18, 13));
        ProductsPanel.setFont(new java.awt.Font("Montserrat Black", 1, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("AvenirNext LT Pro HeavyCn", 1, 55)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(17, 18, 13));
        jLabel11.setText("MANAGE PRODUCTS");

        jTable1_products_data.setBackground(new java.awt.Color(216, 207, 188));
        jTable1_products_data.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTable1_products_data.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTable1_products_data.setForeground(new java.awt.Color(17, 18, 13));
        jTable1_products_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PROD ID", "CTG", "PROD NAME", "PRICE", "QUANTITY", "STATUS", "CRT AT", "UPD AT"
            }
        ));
        jTable1_products_data.setRowHeight(40);
        jScrollPane3.setViewportView(jTable1_products_data);

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(17, 18, 13));
        jLabel12.setText("PRODUCT ID");

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(17, 18, 13));
        jLabel13.setText("PRODUCT NAME");

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(17, 18, 13));
        jLabel14.setText("PRICE");

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(17, 18, 13));
        jLabel15.setText("QUANTITY");

        jLabel16.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(17, 18, 13));
        jLabel16.setText("STATUS");

        jTextField1_product_id.setBackground(new java.awt.Color(244, 248, 255));
        jTextField1_product_id.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField1_product_id.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1_product_id.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTextField2_product_name.setBackground(new java.awt.Color(244, 248, 255));
        jTextField2_product_name.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField2_product_name.setForeground(new java.awt.Color(0, 0, 0));
        jTextField2_product_name.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTextField3_product_price.setBackground(new java.awt.Color(244, 248, 255));
        jTextField3_product_price.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField3_product_price.setForeground(new java.awt.Color(0, 0, 0));
        jTextField3_product_price.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTextField4_product_quantity.setBackground(new java.awt.Color(244, 248, 255));
        jTextField4_product_quantity.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField4_product_quantity.setForeground(new java.awt.Color(0, 0, 0));
        jTextField4_product_quantity.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTextField5_product_status.setBackground(new java.awt.Color(244, 248, 255));
        jTextField5_product_status.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField5_product_status.setForeground(new java.awt.Color(0, 0, 0));
        jTextField5_product_status.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jButton1_product_insert.setBackground(new java.awt.Color(17, 18, 13));
        jButton1_product_insert.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton1_product_insert.setForeground(new java.awt.Color(255, 251, 244));
        jButton1_product_insert.setText("INSERT");
        jButton1_product_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_product_insertActionPerformed(evt);
            }
        });

        jButton2_product_update.setBackground(new java.awt.Color(17, 18, 13));
        jButton2_product_update.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton2_product_update.setForeground(new java.awt.Color(255, 251, 244));
        jButton2_product_update.setText("UPDATE");
        jButton2_product_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2_product_updateActionPerformed(evt);
            }
        });

        jButton3_product_delete.setBackground(new java.awt.Color(17, 18, 13));
        jButton3_product_delete.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton3_product_delete.setForeground(new java.awt.Color(255, 251, 244));
        jButton3_product_delete.setText("DELETE");
        jButton3_product_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3_product_deleteActionPerformed(evt);
            }
        });

        jButton4_product_refresh.setBackground(new java.awt.Color(17, 18, 13));
        jButton4_product_refresh.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton4_product_refresh.setForeground(new java.awt.Color(255, 251, 244));
        jButton4_product_refresh.setText("REFRESH");
        jButton4_product_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4_product_refreshActionPerformed(evt);
            }
        });

        CATEGORIES.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        CATEGORIES.setForeground(new java.awt.Color(17, 18, 13));
        CATEGORIES.setText("CATEGORIES");

        jLabel19.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(17, 18, 13));
        jLabel19.setText("PRODUCT IMAGE");

        jButton1_SelectImage.setBackground(new java.awt.Color(255, 102, 102));
        jButton1_SelectImage.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton1_SelectImage.setForeground(new java.awt.Color(0, 0, 0));
        jButton1_SelectImage.setText("SELECT IMAGE");
        jButton1_SelectImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_SelectImageActionPerformed(evt);
            }
        });

        jLabel21_DisplayImage.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jComboBox1_product_category.setBackground(new java.awt.Color(244, 248, 255));
        jComboBox1_product_category.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jComboBox1_product_category.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox1_product_category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sizzling", "Silog", "Drinks" }));
        jComboBox1_product_category.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel20.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(30, 30, 30));
        jLabel20.setText("<html>ADD NEW<br>CATEGORIES</br></html>");

        jTextField1_NewCtg.setBackground(new java.awt.Color(244, 248, 255));
        jTextField1_NewCtg.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField1_NewCtg.setForeground(new java.awt.Color(30, 30, 30));
        jTextField1_NewCtg.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jButton1_AddCtg.setBackground(new java.awt.Color(255, 102, 102));
        jButton1_AddCtg.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jButton1_AddCtg.setForeground(new java.awt.Color(30, 30, 30));
        jButton1_AddCtg.setText("ADD");

        javax.swing.GroupLayout ProductsPanelLayout = new javax.swing.GroupLayout(ProductsPanel);
        ProductsPanel.setLayout(ProductsPanelLayout);
        ProductsPanelLayout.setHorizontalGroup(
            ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsPanelLayout.createSequentialGroup()
                .addGap(340, 340, 340)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductsPanelLayout.createSequentialGroup()
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1_product_insert, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(jButton3_product_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2_product_update, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4_product_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addGap(215, 215, 215)
                        .addComponent(jLabel21_DisplayImage, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel18)
                            .addComponent(jLabel16)
                            .addComponent(jLabel12)
                            .addComponent(CATEGORIES)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1_product_id)
                            .addComponent(jTextField2_product_name)
                            .addComponent(jTextField3_product_price)
                            .addComponent(jTextField5_product_status)
                            .addComponent(jTextField4_product_quantity)
                            .addComponent(jButton1_SelectImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1_product_category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1_NewCtg)
                            .addComponent(jButton1_AddCtg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(62, 62, 62)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        ProductsPanelLayout.setVerticalGroup(
            ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductsPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProductsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel21_DisplayImage, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jButton1_SelectImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jTextField1_product_id, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CATEGORIES)
                            .addComponent(jComboBox1_product_category, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ProductsPanelLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel18))
                            .addComponent(jTextField2_product_name, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jTextField3_product_price, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jTextField4_product_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jTextField5_product_status, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1_NewCtg, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1_AddCtg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2_product_update)
                            .addComponent(jButton1_product_insert))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3_product_delete)
                            .addComponent(jButton4_product_refresh)))
                    .addComponent(jScrollPane3))
                .addGap(30, 30, 30))
        );

        MainPanel.addTab("tab3", ProductsPanel);

        AcccountsPanel.setBackground(new java.awt.Color(215, 215, 215));

        jTabbedPane2_Manage_Acc.setBackground(new java.awt.Color(233, 190, 95));
        jTabbedPane2_Manage_Acc.setForeground(new java.awt.Color(17, 18, 13));
        jTabbedPane2_Manage_Acc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabbedPane2_Manage_Acc.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N

        jPanel2_Customers.setBackground(new java.awt.Color(215, 215, 215));
        jPanel2_Customers.setForeground(new java.awt.Color(17, 18, 13));
        jPanel2_Customers.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        jTable1_customer_data.setBackground(new java.awt.Color(216, 207, 188));
        jTable1_customer_data.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTable1_customer_data.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTable1_customer_data.setForeground(new java.awt.Color(17, 18, 13));
        jTable1_customer_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "CST ID", "CST NAME", "CST USERNAME", "EMAIL", "CRT AT", "UPD AT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1_customer_data.setRowHeight(40);
        jScrollPane1.setViewportView(jTable1_customer_data);

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(17, 18, 13));
        jLabel2.setText("CUSTOMER ID");

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(17, 18, 13));
        jLabel3.setText("<html>CUSTOMER<br>NAME</html>");

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(17, 18, 13));
        jLabel4.setText("<html>CUSTOMER<br>USERNMAME</html>");

        jLabel5.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(17, 18, 13));
        jLabel5.setText("EMAIL");

        jTextField1_customer_id.setBackground(new java.awt.Color(244, 248, 255));
        jTextField1_customer_id.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField1_customer_id.setForeground(new java.awt.Color(30, 30, 30));
        jTextField1_customer_id.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTextField2_customer_name.setBackground(new java.awt.Color(244, 248, 255));
        jTextField2_customer_name.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField2_customer_name.setForeground(new java.awt.Color(30, 30, 30));
        jTextField2_customer_name.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTextField3_customer_username.setBackground(new java.awt.Color(244, 248, 255));
        jTextField3_customer_username.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField3_customer_username.setForeground(new java.awt.Color(30, 30, 30));
        jTextField3_customer_username.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jTextField4_email.setBackground(new java.awt.Color(244, 248, 255));
        jTextField4_email.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField4_email.setForeground(new java.awt.Color(30, 30, 30));
        jTextField4_email.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPasswordField1_customer_pass.setBackground(new java.awt.Color(244, 248, 255));
        jPasswordField1_customer_pass.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jPasswordField1_customer_pass.setForeground(new java.awt.Color(30, 30, 30));
        jPasswordField1_customer_pass.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jButton1_customer_insert.setBackground(new java.awt.Color(17, 18, 13));
        jButton1_customer_insert.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton1_customer_insert.setForeground(new java.awt.Color(255, 251, 244));
        jButton1_customer_insert.setText("INSERT");
        jButton1_customer_insert.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1_customer_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_customer_insertActionPerformed(evt);
            }
        });

        jButton2_customer_update.setBackground(new java.awt.Color(17, 18, 13));
        jButton2_customer_update.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton2_customer_update.setForeground(new java.awt.Color(255, 251, 244));
        jButton2_customer_update.setText("UPDATE");
        jButton2_customer_update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2_customer_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2_customer_updateActionPerformed(evt);
            }
        });

        jButton3_customer_delete.setBackground(new java.awt.Color(17, 18, 13));
        jButton3_customer_delete.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton3_customer_delete.setForeground(new java.awt.Color(255, 251, 244));
        jButton3_customer_delete.setText("DELETE");
        jButton3_customer_delete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3_customer_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3_customer_deleteActionPerformed(evt);
            }
        });

        jButton4_customer_refresh.setBackground(new java.awt.Color(17, 18, 13));
        jButton4_customer_refresh.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton4_customer_refresh.setForeground(new java.awt.Color(255, 251, 244));
        jButton4_customer_refresh.setText("REFRESH");
        jButton4_customer_refresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4_customer_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4_customer_refreshActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(17, 18, 13));
        jLabel6.setText("PASSWORD");

        javax.swing.GroupLayout jPanel2_CustomersLayout = new javax.swing.GroupLayout(jPanel2_Customers);
        jPanel2_Customers.setLayout(jPanel2_CustomersLayout);
        jPanel2_CustomersLayout.setHorizontalGroup(
            jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2_CustomersLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2_CustomersLayout.createSequentialGroup()
                        .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField3_customer_username, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1_customer_id, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2_customer_name, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPasswordField1_customer_pass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addComponent(jTextField4_email, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addGroup(jPanel2_CustomersLayout.createSequentialGroup()
                        .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1_customer_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3_customer_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton4_customer_refresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(jButton2_customer_update, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel2_CustomersLayout.setVerticalGroup(
            jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2_CustomersLayout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1_customer_id, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2_CustomersLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2_CustomersLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jTextField2_customer_name, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2_CustomersLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2_CustomersLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jTextField3_customer_username, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4_email, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jPasswordField1_customer_pass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1_customer_insert)
                    .addComponent(jButton2_customer_update))
                .addGap(18, 18, 18)
                .addGroup(jPanel2_CustomersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3_customer_delete)
                    .addComponent(jButton4_customer_refresh))
                .addContainerGap(219, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2_CustomersLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        jTabbedPane2_Manage_Acc.addTab("CUSTOMERS", jPanel2_Customers);

        jPanel3_Admins.setBackground(new java.awt.Color(215, 215, 215));

        jTable1_admin_data.setBackground(new java.awt.Color(216, 207, 188));
        jTable1_admin_data.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTable1_admin_data.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTable1_admin_data.setForeground(new java.awt.Color(17, 18, 13));
        jTable1_admin_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "USERNAME", "CRT AT", "UPD AT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1_admin_data.setRowHeight(40);
        jScrollPane2.setViewportView(jTable1_admin_data);

        jLabel8.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(17, 18, 13));
        jLabel8.setText("ID");

        jTextField1_admin_id.setBackground(new java.awt.Color(244, 248, 255));
        jTextField1_admin_id.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField1_admin_id.setForeground(new java.awt.Color(30, 30, 30));
        jTextField1_admin_id.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTextField1_admin_id.setCaretColor(new java.awt.Color(255, 251, 244));

        jLabel9.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(17, 18, 13));
        jLabel9.setText("<html>USERNAME<br>ADMIN</html>");

        jTextField2_admin_user.setBackground(new java.awt.Color(244, 248, 255));
        jTextField2_admin_user.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jTextField2_admin_user.setForeground(new java.awt.Color(30, 30, 30));
        jTextField2_admin_user.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel10.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(17, 18, 13));
        jLabel10.setText("<html>PASSWORD<br>ADMIN</html>");

        jPasswordField1_admin_pass.setBackground(new java.awt.Color(244, 248, 255));
        jPasswordField1_admin_pass.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jPasswordField1_admin_pass.setForeground(new java.awt.Color(30, 30, 30));
        jPasswordField1_admin_pass.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jButton1_admin_insert.setBackground(new java.awt.Color(17, 18, 13));
        jButton1_admin_insert.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton1_admin_insert.setForeground(new java.awt.Color(255, 251, 244));
        jButton1_admin_insert.setText("INSERT");
        jButton1_admin_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_admin_insertActionPerformed(evt);
            }
        });

        jButton2_admin_update.setBackground(new java.awt.Color(17, 18, 13));
        jButton2_admin_update.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton2_admin_update.setForeground(new java.awt.Color(255, 251, 244));
        jButton2_admin_update.setText("UPDATE");
        jButton2_admin_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2_admin_updateActionPerformed(evt);
            }
        });

        jButton3_admin_delete.setBackground(new java.awt.Color(17, 18, 13));
        jButton3_admin_delete.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton3_admin_delete.setForeground(new java.awt.Color(255, 251, 244));
        jButton3_admin_delete.setText("DELETE");
        jButton3_admin_delete.setToolTipText("");
        jButton3_admin_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3_admin_deleteActionPerformed(evt);
            }
        });

        jButton4_admin_refresh.setBackground(new java.awt.Color(17, 18, 13));
        jButton4_admin_refresh.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton4_admin_refresh.setForeground(new java.awt.Color(255, 251, 244));
        jButton4_admin_refresh.setText("REFRESH");
        jButton4_admin_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4_admin_refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3_AdminsLayout = new javax.swing.GroupLayout(jPanel3_Admins);
        jPanel3_Admins.setLayout(jPanel3_AdminsLayout);
        jPanel3_AdminsLayout.setHorizontalGroup(
            jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1_admin_id, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(jTextField2_admin_user)
                            .addComponent(jPasswordField1_admin_pass))
                        .addGap(114, 114, 114))
                    .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1_admin_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3_admin_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2_admin_update, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(jButton4_admin_refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel3_AdminsLayout.setVerticalGroup(
            jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField1_admin_id, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jTextField2_admin_user, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPasswordField1_admin_pass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3_AdminsLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(78, 78, 78)
                .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2_admin_update)
                    .addComponent(jButton1_admin_insert))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3_AdminsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3_admin_delete)
                    .addComponent(jButton4_admin_refresh))
                .addContainerGap(325, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3_AdminsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        jTabbedPane2_Manage_Acc.addTab("ADMINS", jPanel3_Admins);

        jLabel7.setFont(new java.awt.Font("AvenirNext LT Pro HeavyCn", 1, 55)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(17, 18, 13));
        jLabel7.setText("MANAGE ACCOUNTS");

        javax.swing.GroupLayout AcccountsPanelLayout = new javax.swing.GroupLayout(AcccountsPanel);
        AcccountsPanel.setLayout(AcccountsPanelLayout);
        AcccountsPanelLayout.setHorizontalGroup(
            AcccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2_Manage_Acc)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AcccountsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(367, 367, 367))
        );
        AcccountsPanelLayout.setVerticalGroup(
            AcccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AcccountsPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2_Manage_Acc))
        );

        MainPanel.addTab("tab4", AcccountsPanel);

        ReportsPanel.setBackground(new java.awt.Color(215, 215, 215));

        jLabel25.setFont(new java.awt.Font("AvenirNext LT Pro HeavyCn", 1, 55)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("REPORTS MANAGEMENT");

        ReportsContainerPanel.setBackground(new java.awt.Color(233, 190, 95));
        ReportsContainerPanel.setForeground(new java.awt.Color(0, 0, 0));
        ReportsContainerPanel.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N

        SalesReports.setBackground(new java.awt.Color(215, 215, 215));

        jTable1_DisplaySalesReports.setBackground(new java.awt.Color(255, 250, 236));
        jTable1_DisplaySalesReports.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jTable1_DisplaySalesReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "DATE", "TRANSACTION NUMBER", "CUSTOMER NAME", "CUSTOMER TYPE", "PRODUCTS", "CATEGORY", "QTY", "UNIT PRICE", "TOTAL PRICE", "ORDER STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1_DisplaySalesReports.setRowHeight(35);
        jScrollPane5.setViewportView(jTable1_DisplaySalesReports);

        jButton1_ExportSalesReports.setBackground(new java.awt.Color(233, 190, 95));
        jButton1_ExportSalesReports.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton1_ExportSalesReports.setForeground(new java.awt.Color(0, 0, 0));
        jButton1_ExportSalesReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/export.png"))); // NOI18N
        jButton1_ExportSalesReports.setText("EXPORT");

        jLabel26.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("REPORT TYPE DATE:");

        jComboBox1_ReportTypeDateSales.setBackground(new java.awt.Color(233, 190, 95));
        jComboBox1_ReportTypeDateSales.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N

        javax.swing.GroupLayout SalesReportsLayout = new javax.swing.GroupLayout(SalesReports);
        SalesReports.setLayout(SalesReportsLayout);
        SalesReportsLayout.setHorizontalGroup(
            SalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SalesReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1_ReportTypeDateSales, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1_ExportSalesReports, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        SalesReportsLayout.setVerticalGroup(
            SalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesReportsLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SalesReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1_ExportSalesReports, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(jComboBox1_ReportTypeDateSales)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        ReportsContainerPanel.addTab("SALES REPORTS", SalesReports);

        CustomersReports.setBackground(new java.awt.Color(215, 215, 215));

        jTable2_DisplayCustomersReports.setBackground(new java.awt.Color(255, 250, 236));
        jTable2_DisplayCustomersReports.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jTable2_DisplayCustomersReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "DATE REGISTRATION", "CUSTOMER ID", "CUSTOMER NAME", "CUSTOMER TYPE", "TOTAL ORDERS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2_DisplayCustomersReports.setRowHeight(35);
        jScrollPane6.setViewportView(jTable2_DisplayCustomersReports);

        jButton2_ExportCustomersReports.setBackground(new java.awt.Color(233, 190, 95));
        jButton2_ExportCustomersReports.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton2_ExportCustomersReports.setForeground(new java.awt.Color(0, 0, 0));
        jButton2_ExportCustomersReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/export.png"))); // NOI18N
        jButton2_ExportCustomersReports.setText("EXPORT");

        jLabel27.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("REPORT TYPE DATE:");

        jComboBox2_ReportTypeDateCustomers.setBackground(new java.awt.Color(233, 190, 95));
        jComboBox2_ReportTypeDateCustomers.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jComboBox2_ReportTypeDateCustomers.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout CustomersReportsLayout = new javax.swing.GroupLayout(CustomersReports);
        CustomersReports.setLayout(CustomersReportsLayout);
        CustomersReportsLayout.setHorizontalGroup(
            CustomersReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CustomersReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addGap(19, 19, 19)
                .addComponent(jComboBox2_ReportTypeDateCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2_ExportCustomersReports, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        CustomersReportsLayout.setVerticalGroup(
            CustomersReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomersReportsLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CustomersReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2_ExportCustomersReports, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2_ReportTypeDateCustomers))
                .addContainerGap())
        );

        ReportsContainerPanel.addTab("CUSTOMERS REPORTS", CustomersReports);

        inventoryReports.setBackground(new java.awt.Color(215, 215, 215));

        jTable3_DisplayInventoryReports.setBackground(new java.awt.Color(255, 250, 236));
        jTable3_DisplayInventoryReports.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jTable3_DisplayInventoryReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PRODUCT ID", "PPODUCT NAME", "STOCK", "STOCK STATUS", "PRODUCT STATUS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3_DisplayInventoryReports.setRowHeight(35);
        jScrollPane7.setViewportView(jTable3_DisplayInventoryReports);

        jButton3_ExportInventoryReports.setBackground(new java.awt.Color(233, 190, 95));
        jButton3_ExportInventoryReports.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton3_ExportInventoryReports.setForeground(new java.awt.Color(0, 0, 0));
        jButton3_ExportInventoryReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/export.png"))); // NOI18N
        jButton3_ExportInventoryReports.setText("EXPORT");

        jLabel28.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("REPORT TYPE DATE:");

        jComboBox3_ReportTypeDateInventory.setBackground(new java.awt.Color(233, 190, 95));
        jComboBox3_ReportTypeDateInventory.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jComboBox3_ReportTypeDateInventory.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout inventoryReportsLayout = new javax.swing.GroupLayout(inventoryReports);
        inventoryReports.setLayout(inventoryReportsLayout);
        inventoryReportsLayout.setHorizontalGroup(
            inventoryReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addComponent(jComboBox3_ReportTypeDateInventory, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3_ExportInventoryReports, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        inventoryReportsLayout.setVerticalGroup(
            inventoryReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryReportsLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inventoryReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3_ExportInventoryReports, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3_ReportTypeDateInventory))
                .addContainerGap())
        );

        ReportsContainerPanel.addTab("INVENTORY REPORTS", inventoryReports);

        javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
        ReportsPanel.setLayout(ReportsPanelLayout);
        ReportsPanelLayout.setHorizontalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addGap(312, 312, 312))
            .addComponent(ReportsContainerPanel)
        );
        ReportsPanelLayout.setVerticalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(ReportsContainerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 854, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        MainPanel.addTab("tab5", ReportsPanel);

        getContentPane().add(MainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, -40, 1220, 1000));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DashboardBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(0);
    }//GEN-LAST:event_DashboardBTNActionPerformed

    private void OrdersBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrdersBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(1);
    }//GEN-LAST:event_OrdersBTNActionPerformed

    private void ProducsBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProducsBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(2);
    }//GEN-LAST:event_ProducsBTNActionPerformed

    private void AccountsBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AccountsBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(3);
    }//GEN-LAST:event_AccountsBTNActionPerformed

    private void LogoutBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutBTNActionPerformed
        // TODO add your handling code here:
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            dispose();
            new LoginPageForm().setVisible(true);
        }
    }//GEN-LAST:event_LogoutBTNActionPerformed

    private void jButton1_customer_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_customer_insertActionPerformed
        // TODO add your handling code here:
           String customerIdText = jTextField1_customer_id.getText();
        if (customerIdText.isEmpty()) {
           JOptionPane.showMessageDialog(this, "CHOOSE A VALID CUSTOMER ID", "Error", JOptionPane.ERROR_MESSAGE);
           return;
        }

        String name = jTextField2_customer_name.getText();
        String username = jTextField3_customer_username.getText();
        String email = jTextField4_email.getText();
        String password = new String(jPasswordField1_customer_pass.getPassword());

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long customerId = Long.parseLong(customerIdText);
            accounts.insertAccount(customerId, name, username, password, email);
            JOptionPane.showMessageDialog(this, "Account inserted successfully!");
            refreshCustomerTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID CUSTOMER ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inserting account: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1_customer_insertActionPerformed

    private void jButton2_customer_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2_customer_updateActionPerformed
        // TODO add your handling code here:
        String customerIdText = jTextField1_customer_id.getText();
        if (customerIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID CUSTOMER ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = jTextField2_customer_name.getText();
        String username = jTextField3_customer_username.getText();
        String email = jTextField4_email.getText();
        String password = new String(jPasswordField1_customer_pass.getPassword());

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long customerId = Long.parseLong(customerIdText);
            accounts.updateAccount(customerId, name, username, password, email);
            JOptionPane.showMessageDialog(this, "Account updated successfully!");
            refreshCustomerTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID CUSTOMER ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating account: " + e.getMessage());
        }    
    }//GEN-LAST:event_jButton2_customer_updateActionPerformed

    private void jButton3_customer_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3_customer_deleteActionPerformed
        // TODO add your handling code here:
        String customerIdText = jTextField1_customer_id.getText();
        if (customerIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID CUSTOMER ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long customerId = Long.parseLong(customerIdText);
            accounts.deleteAccount(customerId);
            JOptionPane.showMessageDialog(this, "Account deleted successfully!");
            refreshCustomerTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID CUSTOMER ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting account: " + e.getMessage());
        }    
    }//GEN-LAST:event_jButton3_customer_deleteActionPerformed

    private void jButton4_customer_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4_customer_refreshActionPerformed
        // TODO add your handling code here:
        refreshCustomerTable();
    }//GEN-LAST:event_jButton4_customer_refreshActionPerformed
 
    private void jButton1_admin_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_admin_insertActionPerformed
        // TODO add your handling code here:
        String username = jTextField2_admin_user.getText();
        String password = new String(jPasswordField1_admin_pass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            accounts.insertAdmin(username, password);
            JOptionPane.showMessageDialog(this, "Admin inserted successfully!");
            refreshAdminTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inserting admin: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1_admin_insertActionPerformed

    private void jButton2_admin_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2_admin_updateActionPerformed
        // TODO add your handling code here:
        String idText = jTextField1_admin_id.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = jTextField2_admin_user.getText();
        String password = new String(jPasswordField1_admin_pass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long id = Long.parseLong(idText);
            accounts.updateAdmin(id, username, password);
            JOptionPane.showMessageDialog(this, "Admin updated successfully!");
            refreshAdminTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating admin: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton2_admin_updateActionPerformed

    private void jButton3_admin_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3_admin_deleteActionPerformed
        // TODO add your handling code here:
        String idText = jTextField1_admin_id.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long id = Long.parseLong(idText);

            accounts.deleteAdmin(id);
            JOptionPane.showMessageDialog(this, "Admin deleted successfully!");
            refreshAdminTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "CHOOSE A VALID ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting admin: " + e.getMessage());
        }    
    }//GEN-LAST:event_jButton3_admin_deleteActionPerformed

    private void jButton4_admin_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4_admin_refreshActionPerformed
        // TODO add your handling code here:
        refreshAdminTable();
    }//GEN-LAST:event_jButton4_admin_refreshActionPerformed

    private void jButton1_product_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_product_insertActionPerformed
        // TODO add your handling code here:
    String productId = jTextField1_product_id.getText().trim();
    if (productId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "CHOOSE A VALID PRODUCT ID", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (productId.length() > 20) {
        JOptionPane.showMessageDialog(this, "Product ID must be 20 characters or less", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String name = jTextField2_product_name.getText();
    double price = Double.parseDouble(jTextField3_product_price.getText());
    int quantity = Integer.parseInt(jTextField4_product_quantity.getText());
    String status = jTextField5_product_status.getText();
    String category = (String) jComboBox1_product_category.getSelectedItem();

    if (productId.isEmpty() || name.isEmpty() || jTextField3_product_price.getText().isEmpty() || jTextField4_product_quantity.getText().isEmpty() || status.isEmpty() || category.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        byte[] productImageBytes = Files.readAllBytes(selectedImageFile.toPath());
        products.insertProduct(productId, category, name, price, quantity, status, productImageBytes);
        JOptionPane.showMessageDialog(this, "Product inserted successfully!");
        refreshProductTable();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading product image file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error inserting product: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton1_product_insertActionPerformed

    private void jButton2_product_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2_product_updateActionPerformed
        // TODO add your handling code here:
    String productId = jTextField1_product_id.getText().trim();
    if (productId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "CHOOSE A VALID PRODUCT ID", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    if (productId.length() > 20) {
        JOptionPane.showMessageDialog(this, "Product ID must be 20 characters or less", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String name = jTextField2_product_name.getText();
    double price = Double.parseDouble(jTextField3_product_price.getText());
    int quantity = Integer.parseInt(jTextField4_product_quantity.getText());
    String status = jTextField5_product_status.getText();
    String category = (String) jComboBox1_product_category.getSelectedItem();

    if (productId.isEmpty() || name.isEmpty() || jTextField3_product_price.getText().isEmpty() || jTextField4_product_quantity.getText().isEmpty() || status.isEmpty() || category.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled out", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        byte[] productImageBytes = Files.readAllBytes(selectedImageFile.toPath());
        products.updateProduct(productId, category, name, price, quantity, status, productImageBytes);
        JOptionPane.showMessageDialog(this, "Product updated successfully!");
        refreshProductTable();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading product image file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton2_product_updateActionPerformed

    private void jButton3_product_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3_product_deleteActionPerformed
        // TODO add your handling code here:
    String productId = jTextField1_product_id.getText().trim();
    if (productId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "CHOOSE A VALID PRODUCT ID", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        products.deleteProduct(productId);
        JOptionPane.showMessageDialog(this, "Product deleted successfully!");
        refreshProductTable();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
    }     
    }//GEN-LAST:event_jButton3_product_deleteActionPerformed

    private void jButton4_product_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4_product_refreshActionPerformed
        // TODO add your handling code here:
        refreshProductTable();  
    }//GEN-LAST:event_jButton4_product_refreshActionPerformed

    private void jButton1_SelectImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_SelectImageActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser(new File("C:\\Users\\Jeremy Malana\\Documents\\NetBeansProjects\\OMS\\src\\Food"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedImageFile.getAbsolutePath()).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
            jLabel21_DisplayImage.setIcon(imageIcon);
        }
    }//GEN-LAST:event_jButton1_SelectImageActionPerformed

    private void jTable1_DisplayOrderManageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1_DisplayOrderManageMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1_DisplayOrderManageMouseClicked

    private void ReportsBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReportsBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(4);
    }//GEN-LAST:event_ReportsBTNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AcccountsPanel;
    private javax.swing.JButton AccountsBTN;
    private javax.swing.JLabel CATEGORIES;
    private javax.swing.JPanel CustomersReports;
    private javax.swing.JButton DashboardBTN;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JButton LogoutBTN;
    private javax.swing.JTabbedPane MainPanel;
    private javax.swing.JPanel NavBarPanel;
    private javax.swing.JButton OrdersBTN;
    private javax.swing.JPanel OrdersPanel;
    private javax.swing.JButton ProducsBTN;
    private javax.swing.JPanel ProductsPanel;
    private javax.swing.JButton ReportsBTN;
    private javax.swing.JTabbedPane ReportsContainerPanel;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JPanel SalesReports;
    private javax.swing.JPanel inventoryReports;
    private javax.swing.JButton jButton1_AceeptOrder;
    private javax.swing.JButton jButton1_AddCtg;
    private javax.swing.JButton jButton1_ExportSalesReports;
    private javax.swing.JButton jButton1_SelectImage;
    private javax.swing.JButton jButton1_SelectPromo;
    private javax.swing.JButton jButton1_UploadPromo;
    private javax.swing.JButton jButton1_admin_insert;
    private javax.swing.JButton jButton1_customer_insert;
    private javax.swing.JButton jButton1_product_insert;
    private javax.swing.JButton jButton2_ExportCustomersReports;
    private javax.swing.JButton jButton2_admin_update;
    private javax.swing.JButton jButton2_customer_update;
    private javax.swing.JButton jButton2_product_update;
    private javax.swing.JButton jButton3_ExportInventoryReports;
    private javax.swing.JButton jButton3_admin_delete;
    private javax.swing.JButton jButton3_customer_delete;
    private javax.swing.JButton jButton3_product_delete;
    private javax.swing.JButton jButton4_admin_refresh;
    private javax.swing.JButton jButton4_customer_refresh;
    private javax.swing.JButton jButton4_product_refresh;
    private javax.swing.JComboBox<String> jComboBox1_ReportTypeDateSales;
    private javax.swing.JComboBox<String> jComboBox1_Status;
    private javax.swing.JComboBox<String> jComboBox1_product_category;
    private javax.swing.JComboBox<String> jComboBox2_ReportTypeDateCustomers;
    private javax.swing.JComboBox<String> jComboBox3_ReportTypeDateInventory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel20_Promo;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel21_DisplayImage;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel25_TotalPrice;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel2_welcome;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_TotalCustomers;
    private javax.swing.JLabel jLabel_TotalGuest;
    private javax.swing.JLabel jLabel_TotalOrders;
    private javax.swing.JLabel jLabel_TotalOrdersPending;
    private javax.swing.JLabel jLabel_TotalProducts;
    private javax.swing.JLabel jLabel_TotalSales;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel2_Customers;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel3_Admins;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPasswordField jPasswordField1_admin_pass;
    private javax.swing.JPasswordField jPasswordField1_customer_pass;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane2_Manage_Acc;
    private javax.swing.JTable jTable1_DisplayOrderManage;
    private javax.swing.JTable jTable1_DisplaySalesReports;
    private javax.swing.JTable jTable1_admin_data;
    private javax.swing.JTable jTable1_customer_data;
    private javax.swing.JTable jTable1_products_data;
    private javax.swing.JTable jTable2_DisplayCustomersReports;
    private javax.swing.JTable jTable3_DisplayInventoryReports;
    private javax.swing.JTextField jTextField1_NewCtg;
    private javax.swing.JTextField jTextField1_TransacNum;
    private javax.swing.JTextField jTextField1_admin_id;
    private javax.swing.JTextField jTextField1_customer_id;
    private javax.swing.JTextField jTextField1_product_id;
    private javax.swing.JTextField jTextField2_admin_user;
    private javax.swing.JTextField jTextField2_customer_name;
    private javax.swing.JTextField jTextField2_product_name;
    private javax.swing.JTextField jTextField3_customer_username;
    private javax.swing.JTextField jTextField3_product_price;
    private javax.swing.JTextField jTextField4_email;
    private javax.swing.JTextField jTextField4_product_quantity;
    private javax.swing.JTextField jTextField5_product_status;
    // End of variables declaration//GEN-END:variables

}
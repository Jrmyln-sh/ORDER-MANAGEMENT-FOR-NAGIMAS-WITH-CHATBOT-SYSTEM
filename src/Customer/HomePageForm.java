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
package Customer;
import Authenticate.LoginPageForm;
import javax.swing.JOptionPane;
import Admin.Dashboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Jeremy Malana
 */
public class HomePageForm extends javax.swing.JFrame {
    
    private String loggedInUsername;
    private String displayName;
    private Menu menu;
    
    /**
     * Creates new form HomePageForm
     */
    public HomePageForm(String username, String displayName) {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        this.loggedInUsername = username;
        this.displayName = displayName;
        displayCustomerName();
        displayCustomerName();
        loadPromoImage();
        
        OrderType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dine-In", "Take-Out" }));
        
        menu = new Menu();
        
        DefaultTableModel cartModel = new DefaultTableModel(
            new Object[]{"PRODUCT NAME", "PRICE", "QUANTITY", "TOTAL", "REMOVE"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return Boolean.class;
                }
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 4;
            }
        };
        
        jTable_DisplayCart.setModel(cartModel);
        jLabel_DisplayTotalPrice.setText("TOTAL PRICE: 0.00");
        menu.fillAllProductsTable(DisplayMenu);
        setupButtonActions();
        setupSearch();
        
    }
    
    
    private void setupSearch() {
        jTextField_SearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
            
            private void filterTable() {
                String searchText = jTextField_SearchField.getText();
                if (searchText.isEmpty()) {
                    menu.fillAllProductsTable(DisplayMenu);
                } else {
                    menu.filterProductsByName(DisplayMenu, searchText);
                }
            }
        });
        
        jButton_SearchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = jTextField_SearchField.getText().trim();
                if (!searchText.isEmpty()) {
                    boolean foundMatches = menu.filterProductsByName(DisplayMenu, searchText);
                    if (!foundMatches) {
                        JOptionPane.showMessageDialog(
                            HomePageForm.this,
                            "Sorry we couldn't find that Product, Try searching for another term.",
                            "No Results Found",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });
    }
    
private void setupButtonActions() {
    jButton_AddToCart.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            menu.checkout(DisplayMenu, jTable_DisplayCart, jLabel_DisplayTotalPrice);
        }
    });
    
    jButton_Remove.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            menu.removeSelectedItemsWithConfirmation(HomePageForm.this, jTable_DisplayCart, jLabel_DisplayTotalPrice);
        }
    });

    jButton_Order.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the selected order type from the JComboBox
            String selectedOrderType = (String) OrderType.getSelectedItem();
            // Pass the order type to the order processing method
            menu.confirmAndProcessOrder(jTable_DisplayCart, jLabel_DisplayTotalPrice, loggedInUsername, false, selectedOrderType);
        }
    });
}
    
    private void displayCustomerName() {
        jLabel2_CustomerName.setText("WELCOME, " + displayName);
    }
    
    private void loadPromoImage() {
        Dashboard.loadPromoImageForCustomer(DisplayPromo);
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
        HomeBTN = new javax.swing.JButton();
        MenuBTN = new javax.swing.JButton();
        AccountsBTN = new javax.swing.JButton();
        MainPanel = new javax.swing.JTabbedPane();
        HomePanel = new javax.swing.JPanel();
        DisplayPromo = new javax.swing.JLabel();
        Label_1 = new javax.swing.JLabel();
        MenuPanel = new javax.swing.JPanel();
        Label_2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DisplayMenu = new javax.swing.JTable();
        jButton_AddToCart = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_DisplayCart = new javax.swing.JTable();
        jButton_Remove = new javax.swing.JButton();
        jButton_Order = new javax.swing.JButton();
        jLabel_DisplayTotalPrice = new javax.swing.JLabel();
        jTextField_SearchField = new javax.swing.JTextField();
        jButton_SearchBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        OrderType = new javax.swing.JComboBox<>();
        AccountsPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2_CustomerName = new javax.swing.JLabel();
        jButton1_Logout = new javax.swing.JButton();
        jButton1_OrderProcess = new javax.swing.JButton();
        jButton1_ChatBot = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NavBarPanel.setBackground(new java.awt.Color(28, 28, 28));

        HomeBTN.setBackground(new java.awt.Color(233, 190, 95));
        HomeBTN.setFont(new java.awt.Font("Montserrat", 1, 28)); // NOI18N
        HomeBTN.setForeground(new java.awt.Color(0, 0, 0));
        HomeBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/home.png"))); // NOI18N
        HomeBTN.setText("HOME");
        HomeBTN.setToolTipText("");
        HomeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeBTNActionPerformed(evt);
            }
        });

        MenuBTN.setBackground(new java.awt.Color(233, 190, 95));
        MenuBTN.setFont(new java.awt.Font("Montserrat", 1, 28)); // NOI18N
        MenuBTN.setForeground(new java.awt.Color(0, 0, 0));
        MenuBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu.png"))); // NOI18N
        MenuBTN.setText("MENU");
        MenuBTN.setToolTipText("");
        MenuBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuBTNActionPerformed(evt);
            }
        });

        AccountsBTN.setBackground(new java.awt.Color(233, 190, 95));
        AccountsBTN.setFont(new java.awt.Font("Montserrat", 1, 28)); // NOI18N
        AccountsBTN.setForeground(new java.awt.Color(0, 0, 0));
        AccountsBTN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/user.png"))); // NOI18N
        AccountsBTN.setText("ACCOUNT");
        AccountsBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccountsBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NavBarPanelLayout = new javax.swing.GroupLayout(NavBarPanel);
        NavBarPanel.setLayout(NavBarPanelLayout);
        NavBarPanelLayout.setHorizontalGroup(
            NavBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NavBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HomeBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MenuBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AccountsBTN, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        NavBarPanelLayout.setVerticalGroup(
            NavBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavBarPanelLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(HomeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(MenuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AccountsBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
        );

        getContentPane().add(NavBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 950));

        HomePanel.setBackground(new java.awt.Color(215, 215, 215));

        DisplayPromo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(233, 190, 95), 3, true));

        Label_1.setFont(new java.awt.Font("Montserrat", 1, 48)); // NOI18N
        Label_1.setForeground(new java.awt.Color(30, 30, 30));
        Label_1.setText("WELCOME TO NAGIMAS, ENJOY ORDERING!!!!");

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomePanelLayout.createSequentialGroup()
                .addContainerGap(187, Short.MAX_VALUE)
                .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DisplayPromo, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
                    .addComponent(Label_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(203, 203, 203))
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomePanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(Label_1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(DisplayPromo, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(220, Short.MAX_VALUE))
        );

        MainPanel.addTab("tab2", HomePanel);

        MenuPanel.setBackground(new java.awt.Color(215, 215, 215));

        Label_2.setFont(new java.awt.Font("Montserrat", 1, 48)); // NOI18N
        Label_2.setForeground(new java.awt.Color(17, 18, 13));
        Label_2.setText("OUR MENU");

        DisplayMenu.setBackground(new java.awt.Color(216, 207, 188));
        DisplayMenu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        DisplayMenu.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        DisplayMenu.setForeground(new java.awt.Color(30, 30, 30));
        DisplayMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PRODUCT IMAGE", "NAME", "PRICE", "QUANTITY", "SELECT"
            }
        ));
        jScrollPane1.setViewportView(DisplayMenu);

        jButton_AddToCart.setBackground(new java.awt.Color(233, 190, 95));
        jButton_AddToCart.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jButton_AddToCart.setForeground(new java.awt.Color(30, 30, 30));
        jButton_AddToCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/check-out.png"))); // NOI18N
        jButton_AddToCart.setText("ADD TO CART");

        jTable_DisplayCart.setBackground(new java.awt.Color(216, 207, 188));
        jTable_DisplayCart.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTable_DisplayCart.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jTable_DisplayCart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PRODUCT NAME", "PRICE", "QUANTITY", "TOTAL PRICE", "REMOVE"
            }
        ));
        jTable_DisplayCart.setRowHeight(30);
        jScrollPane2.setViewportView(jTable_DisplayCart);

        jButton_Remove.setBackground(new java.awt.Color(233, 190, 95));
        jButton_Remove.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        jButton_Remove.setForeground(new java.awt.Color(30, 30, 30));
        jButton_Remove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/remove.png"))); // NOI18N
        jButton_Remove.setText("REMOVE");
        jButton_Remove.setToolTipText("");

        jButton_Order.setBackground(new java.awt.Color(233, 190, 95));
        jButton_Order.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton_Order.setForeground(new java.awt.Color(30, 30, 30));
        jButton_Order.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/order1.png"))); // NOI18N
        jButton_Order.setText("ORDER");

        jLabel_DisplayTotalPrice.setFont(new java.awt.Font("Poppins", 1, 30)); // NOI18N
        jLabel_DisplayTotalPrice.setForeground(new java.awt.Color(30, 30, 30));
        jLabel_DisplayTotalPrice.setText("TOTAL PRICE:");

        jTextField_SearchField.setBackground(new java.awt.Color(239, 239, 239));
        jTextField_SearchField.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jTextField_SearchField.setForeground(new java.awt.Color(30, 30, 30));

        jButton_SearchBtn.setBackground(new java.awt.Color(233, 190, 95));
        jButton_SearchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("ORDER TYPE:");

        OrderType.setBackground(new java.awt.Color(233, 190, 95));
        OrderType.setFont(new java.awt.Font("Montserrat", 1, 24)); // NOI18N
        OrderType.setForeground(new java.awt.Color(0, 0, 0));
        OrderType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DINE IN", "TAKE OUT" }));

        javax.swing.GroupLayout MenuPanelLayout = new javax.swing.GroupLayout(MenuPanel);
        MenuPanel.setLayout(MenuPanelLayout);
        MenuPanelLayout.setHorizontalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                    .addComponent(jButton_AddToCart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuPanelLayout.createSequentialGroup()
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Label_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField_SearchField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_SearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MenuPanelLayout.createSequentialGroup()
                                .addComponent(jLabel_DisplayTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton_Remove, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(MenuPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(OrderType, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton_Order, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        MenuPanelLayout.setVerticalGroup(
            MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(Label_2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_SearchField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_SearchBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(MenuPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_Remove, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_DisplayTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(MenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(OrderType)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_AddToCart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Order, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                .addContainerGap())
        );

        MainPanel.addTab("tab3", MenuPanel);

        AccountsPanel.setBackground(new java.awt.Color(215, 215, 215));

        jPanel1.setBackground(new java.awt.Color(89, 92, 86));

        jLabel2_CustomerName.setFont(new java.awt.Font("Montserrat", 1, 45)); // NOI18N
        jLabel2_CustomerName.setForeground(new java.awt.Color(255, 102, 102));
        jLabel2_CustomerName.setText(" WELCOME ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2_CustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, 1476, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2_CustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
        );

        jButton1_Logout.setBackground(new java.awt.Color(233, 190, 95));
        jButton1_Logout.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton1_Logout.setForeground(new java.awt.Color(17, 18, 13));
        jButton1_Logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/logout-1.png"))); // NOI18N
        jButton1_Logout.setText("LOGOUT");
        jButton1_Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_LogoutActionPerformed(evt);
            }
        });

        jButton1_OrderProcess.setBackground(new java.awt.Color(233, 190, 95));
        jButton1_OrderProcess.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton1_OrderProcess.setForeground(new java.awt.Color(30, 30, 30));
        jButton1_OrderProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/order-process.png"))); // NOI18N
        jButton1_OrderProcess.setText("ORDERS");
        jButton1_OrderProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_OrderProcessActionPerformed(evt);
            }
        });

        jButton1_ChatBot.setBackground(new java.awt.Color(233, 190, 95));
        jButton1_ChatBot.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jButton1_ChatBot.setForeground(new java.awt.Color(30, 30, 30));
        jButton1_ChatBot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/chatbot.png"))); // NOI18N
        jButton1_ChatBot.setText("CHATBOT");
        jButton1_ChatBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_ChatBotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AccountsPanelLayout = new javax.swing.GroupLayout(AccountsPanel);
        AccountsPanel.setLayout(AccountsPanelLayout);
        AccountsPanelLayout.setHorizontalGroup(
            AccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AccountsPanelLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(AccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1_OrderProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1_ChatBot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1_Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        AccountsPanelLayout.setVerticalGroup(
            AccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AccountsPanelLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1_OrderProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1_ChatBot, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 410, Short.MAX_VALUE)
                .addComponent(jButton1_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        MainPanel.addTab("tab3", AccountsPanel);

        getContentPane().add(MainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, -57, 1560, 1010));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HomeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(0);
    }//GEN-LAST:event_HomeBTNActionPerformed

    private void MenuBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(1);
    }//GEN-LAST:event_MenuBTNActionPerformed

    private void AccountsBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AccountsBTNActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(2);
    }//GEN-LAST:event_AccountsBTNActionPerformed

    private void jButton1_LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_LogoutActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            dispose();
            new LoginPageForm().setVisible(true);
        }
    }//GEN-LAST:event_jButton1_LogoutActionPerformed

    private void jButton1_OrderProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_OrderProcessActionPerformed
        // TODO add your handling code here:
        OrderProcessPage orderPage = new OrderProcessPage();
        orderPage.setVisible(true);
    }//GEN-LAST:event_jButton1_OrderProcessActionPerformed

    private void jButton1_ChatBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_ChatBotActionPerformed
        // TODO add your handling code here:
        ChatBotPageForm chatPage = new ChatBotPageForm();
        chatPage.setVisible(true);
    }//GEN-LAST:event_jButton1_ChatBotActionPerformed

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
            java.util.logging.Logger.getLogger(HomePageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePageForm("username", "Customer Name").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AccountsBTN;
    private javax.swing.JPanel AccountsPanel;
    private javax.swing.JTable DisplayMenu;
    private javax.swing.JLabel DisplayPromo;
    private javax.swing.JButton HomeBTN;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel Label_1;
    private javax.swing.JLabel Label_2;
    private javax.swing.JTabbedPane MainPanel;
    private javax.swing.JButton MenuBTN;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JPanel NavBarPanel;
    private javax.swing.JComboBox<String> OrderType;
    private javax.swing.JButton jButton1_ChatBot;
    private javax.swing.JButton jButton1_Logout;
    private javax.swing.JButton jButton1_OrderProcess;
    private javax.swing.JButton jButton_AddToCart;
    private javax.swing.JButton jButton_Order;
    private javax.swing.JButton jButton_Remove;
    private javax.swing.JButton jButton_SearchBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2_CustomerName;
    private javax.swing.JLabel jLabel_DisplayTotalPrice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_DisplayCart;
    private javax.swing.JTextField jTextField_SearchField;
    // End of variables declaration//GEN-END:variables
}

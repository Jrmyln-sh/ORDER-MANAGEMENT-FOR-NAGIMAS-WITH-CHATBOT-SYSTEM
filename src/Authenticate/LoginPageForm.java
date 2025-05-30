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
package Authenticate;
import DatabaseConnection.DBHash;
import Customer.HomePageForm;
import DatabaseConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
/**
 *
 * @author Jeremy Malana
 */
public class LoginPageForm extends javax.swing.JFrame {

    /**
     * Creates new form LoginPage
     */
    public LoginPageForm() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        
        DBHash.hashAdminPasswords();
        DBHash.hashCustomerPasswords();
        
        jLabel6_Forgot_Password.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6_Forgot_PasswordMouseClicked(evt);
            }
        });
    }
    
    public String getUsernameInput() {
        return jTextField1_username_input.getText();
    }

    public String getPasswordInput() {
        return new String(jPasswordField1_password_input.getPassword());
    }
    private void jLabel6_Forgot_PasswordMouseClicked(java.awt.event.MouseEvent evt) {
        new ForgotPasswordPageForm().setVisible(true);
        this.dispose();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3_login = new javax.swing.JLabel();
        jTextField1_username_input = new javax.swing.JTextField();
        jPasswordField1_password_input = new javax.swing.JPasswordField();
        jButton1_login = new javax.swing.JButton();
        jCheckBox1_Show_Password = new javax.swing.JCheckBox();
        jLabel6_Forgot_Password = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton2_SignUp = new javax.swing.JButton();
        jButton3_Guest = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(215, 215, 215));
        jPanel1.setForeground(new java.awt.Color(244, 248, 255));

        jPanel2.setBackground(new java.awt.Color(245, 232, 182));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setToolTipText("");

        jLabel3_login.setBackground(new java.awt.Color(17, 18, 11));
        jLabel3_login.setFont(new java.awt.Font("Montserrat", 1, 48)); // NOI18N
        jLabel3_login.setForeground(new java.awt.Color(17, 18, 11));
        jLabel3_login.setText("Login");

        jTextField1_username_input.setBackground(new java.awt.Color(245, 232, 182));
        jTextField1_username_input.setFont(new java.awt.Font("Poppins", 1, 20)); // NOI18N
        jTextField1_username_input.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1_username_input.setText("Username");
        jTextField1_username_input.setToolTipText("");
        jTextField1_username_input.setBorder(null);
        jTextField1_username_input.setCaretColor(new java.awt.Color(0, 0, 0));
        jTextField1_username_input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1_username_inputFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1_username_inputFocusLost(evt);
            }
        });

        jPasswordField1_password_input.setBackground(new java.awt.Color(245, 232, 182));
        jPasswordField1_password_input.setFont(new java.awt.Font("Poppins", 1, 20)); // NOI18N
        jPasswordField1_password_input.setForeground(new java.awt.Color(0, 0, 0));
        jPasswordField1_password_input.setText("Password");
        jPasswordField1_password_input.setToolTipText("");
        jPasswordField1_password_input.setBorder(null);
        jPasswordField1_password_input.setCaretColor(new java.awt.Color(0, 0, 0));
        jPasswordField1_password_input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordField1_password_inputFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPasswordField1_password_inputFocusLost(evt);
            }
        });

        jButton1_login.setBackground(new java.awt.Color(233, 190, 95));
        jButton1_login.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton1_login.setForeground(new java.awt.Color(37, 35, 33));
        jButton1_login.setText("LOGIN");
        jButton1_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_loginActionPerformed(evt);
            }
        });

        jCheckBox1_Show_Password.setBackground(new java.awt.Color(245, 232, 182));
        jCheckBox1_Show_Password.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        jCheckBox1_Show_Password.setForeground(new java.awt.Color(17, 18, 11));
        jCheckBox1_Show_Password.setText("Show password");
        jCheckBox1_Show_Password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1_Show_PasswordActionPerformed(evt);
            }
        });

        jLabel6_Forgot_Password.setBackground(new java.awt.Color(245, 232, 182));
        jLabel6_Forgot_Password.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        jLabel6_Forgot_Password.setForeground(new java.awt.Color(17, 18, 11));
        jLabel6_Forgot_Password.setText("Forgot Password?");
        jLabel6_Forgot_Password.setToolTipText("");
        jLabel6_Forgot_Password.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel7.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(17, 18, 11));
        jLabel7.setText("Don't have account yet?");
        jLabel7.setToolTipText("");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton2_SignUp.setBackground(new java.awt.Color(233, 190, 95));
        jButton2_SignUp.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton2_SignUp.setForeground(new java.awt.Color(17, 18, 11));
        jButton2_SignUp.setText("SIGN UP");
        jButton2_SignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2_SignUpActionPerformed(evt);
            }
        });

        jButton3_Guest.setBackground(new java.awt.Color(233, 190, 95));
        jButton3_Guest.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        jButton3_Guest.setForeground(new java.awt.Color(17, 18, 11));
        jButton3_Guest.setText("GUEST");
        jButton3_Guest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3_GuestActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Montserrat", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(17, 18, 11));
        jLabel8.setText(" Continue as a guest?");

        jSeparator1.setForeground(new java.awt.Color(30, 30, 30));

        jSeparator2.setForeground(new java.awt.Color(30, 30, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/username.png"))); // NOI18N

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/password.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addComponent(jLabel8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(242, 242, 242)
                        .addComponent(jLabel3_login)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(223, 223, 223))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1_username_input, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6_Forgot_Password)
                            .addComponent(jButton1_login, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jCheckBox1_Show_Password)
                                    .addComponent(jPasswordField1_password_input, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                    .addComponent(jButton3_Guest, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2_SignUp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(64, 64, 64))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3_login)
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1_username_input, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPasswordField1_password_input, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jCheckBox1_Show_Password)
                .addGap(18, 18, 18)
                .addComponent(jButton1_login, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6_Forgot_Password)
                .addGap(27, 27, 27)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton2_SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3_Guest, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/LOGO1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(92, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(55, 55, 55)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(91, 91, 91))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3_GuestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3_GuestActionPerformed
        // TODO add your handling code here:
        GuestPageForm guest = new GuestPageForm();
        guest.show();

        this.dispose();
    }//GEN-LAST:event_jButton3_GuestActionPerformed

    private void jButton2_SignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2_SignUpActionPerformed
        // TODO add your handling code here:
        RegistrationPageForm registrationpage = new RegistrationPageForm();
        registrationpage.show();

        this.dispose();
    }//GEN-LAST:event_jButton2_SignUpActionPerformed

    private void jCheckBox1_Show_PasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1_Show_PasswordActionPerformed
        // TODO add your handling code here:
        if(jCheckBox1_Show_Password.isSelected())
        jPasswordField1_password_input.setEchoChar((char)0);
        else
        jPasswordField1_password_input.setEchoChar('•');
    }//GEN-LAST:event_jCheckBox1_Show_PasswordActionPerformed

    private void jButton1_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_loginActionPerformed
        // TODO add your handling code here:
        Main_Authenticate.handleLogin(this);
    }//GEN-LAST:event_jButton1_loginActionPerformed

    private void jTextField1_username_inputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1_username_inputFocusGained
        // TODO add your handling code here:
        if(jTextField1_username_input.getText().equals("Username")) {
            jTextField1_username_input.setText("");
        }
    }//GEN-LAST:event_jTextField1_username_inputFocusGained

    private void jTextField1_username_inputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1_username_inputFocusLost
        // TODO add your handling code here:
        if(jTextField1_username_input.getText().isEmpty()) {
            jTextField1_username_input.setText("Username");
        }
    }//GEN-LAST:event_jTextField1_username_inputFocusLost

    private void jPasswordField1_password_inputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField1_password_inputFocusGained
        // TODO add your handling code here:
        char[] passwordChars = jPasswordField1_password_input.getPassword();
        String passwordText = new String(passwordChars);
        
        if(passwordText.equals("Password")) {
            jPasswordField1_password_input.setText("");
            jPasswordField1_password_input.setEchoChar('•');
        }
    }//GEN-LAST:event_jPasswordField1_password_inputFocusGained

    private void jPasswordField1_password_inputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField1_password_inputFocusLost
        // TODO add your handling code here:
        char[] passwordChars = jPasswordField1_password_input.getPassword();
        String passwordText = new String(passwordChars);
        
        if(passwordText.isEmpty()) {
            jPasswordField1_password_input.setEchoChar((char)0);
            jPasswordField1_password_input.setText("Password");
        }
    }//GEN-LAST:event_jPasswordField1_password_inputFocusLost

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
            java.util.logging.Logger.getLogger(LoginPageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginPageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginPageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginPageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginPageForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1_login;
    private javax.swing.JButton jButton2_SignUp;
    private javax.swing.JButton jButton3_Guest;
    private javax.swing.JCheckBox jCheckBox1_Show_Password;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel3_login;
    private javax.swing.JLabel jLabel6_Forgot_Password;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1_password_input;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1_username_input;
    // End of variables declaration//GEN-END:variables
}

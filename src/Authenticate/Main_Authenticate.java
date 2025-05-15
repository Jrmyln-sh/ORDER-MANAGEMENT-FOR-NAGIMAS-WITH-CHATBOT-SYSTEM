/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
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

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import at.favre.lib.crypto.bcrypt.BCrypt;
import DatabaseConnection.DBConnection;
import Customer.HomePageForm;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.sendgrid.*;
import java.io.IOException;

/**
 *
 * @author Jeremy Malana
 */
public class Main_Authenticate {

    private static boolean termsShown = false;
    private static int loginAttempts = 0;
    private static long loggedInCustomerId = -1;
    private static long loggedInGuestId = -1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launchApplication();
    }

    // FOR LOGO
    private static void launchApplication() {
        LogoForm splash = new LogoForm();
        splash.setVisible(true);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splash.dispose();
        new LoginPageForm().setVisible(true);
    }

    public static void handleLogin(LoginPageForm loginPageForm) {
        String username = loginPageForm.getUsernameInput();
        String password = loginPageForm.getPasswordInput();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginPageForm,
                    "Please enter username & password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isAdmin = authenticateAdminUser(username, password);
        boolean isCustomer = authenticateCustomerUser(loginPageForm, username, password);

        if (isAdmin) {
            AdminValidationPageForm adminValidationPage = new AdminValidationPageForm(username);
            adminValidationPage.setVisible(true);
            loginPageForm.dispose();
        } else if (isCustomer) {
            loginPageForm.dispose();
        } else {
            loginAttempts++;
            if (loginAttempts >= 3) {
                JOptionPane.showMessageDialog(loginPageForm,
                        "You have exceeded the maximum number of login attempts. The system will now exit.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(loginPageForm,
                        "Invalid username or password",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean authenticateAdminUser(String username, String password) {
        boolean isAuthenticated = false;

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT password_admin FROM admin_users WHERE username_admin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password_admin");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHashedPassword.toCharArray());
                if (result.verified) {
                    isAuthenticated = true;
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        return isAuthenticated;
    }

    private static boolean authenticateCustomerUser(LoginPageForm loginPageForm, String username, String password) {
        boolean isAuthenticated = false;

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT id, customer_name, customer_password FROM customers WHERE customer_username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("customer_password");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHashedPassword.toCharArray());
                if (result.verified) {
                    isAuthenticated = true;
                    loggedInCustomerId = resultSet.getLong("id");
                    loggedInGuestId = -1; // Ensure guest ID is reset when customer logs in
                    String customerName = resultSet.getString("customer_name");
                    HomePageForm customer = new HomePageForm(username, customerName);
                    customer.setVisible(true);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        return isAuthenticated;
    }

    public static long getLoggedInCustomerId() {
        return loggedInCustomerId;
    }

    public static long getLoggedInGuestId() {
        return loggedInGuestId;
    }

    public static void handleRegistration(RegistrationPageForm registrationPageForm) {
        if (!registrationPageForm.isTermsSelected()) {
            JOptionPane.showMessageDialog(registrationPageForm,
                    "You must agree to the terms and conditions to register.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = registrationPageForm.getName();
        String email = registrationPageForm.getEmail();
        String username = registrationPageForm.getUsername();
        String password = registrationPageForm.getPassword();
        String confirmPassword = registrationPageForm.getConfirmPassword();

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(registrationPageForm,
                    "Please fill out all fields",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(registrationPageForm,
                    "Passwords do not match",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String passwordStrength = evaluatePasswordStrength(password, registrationPageForm);
        if (passwordStrength.equals("Weak")) {
            JOptionPane.showMessageDialog(registrationPageForm,
                    "Password is too weak. Please choose a stronger password.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        try (Connection connection = DBConnection.getConnection()) {
            int customerId = generateCustomerId(connection);

            String insertQuery = "INSERT INTO customers (customer_id, customer_name, customer_username, customer_password, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, customerId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, hashedPassword);
            preparedStatement.setString(5, email);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(registrationPageForm,
                    "Registration successful",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            LoginPageForm login = new LoginPageForm();
            login.setVisible(true);
            registrationPageForm.dispose();

        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(registrationPageForm,
                    "An error occurred while registering. Please try again.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    private static int generateCustomerId(Connection connection) throws SQLException {
        String query = "SELECT MAX(customer_id) AS max_id FROM customers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int maxId = resultSet.getInt("max_id");
                return maxId + 1;
            } else {
                return 1;
            }
        }
    }

    public static String evaluatePasswordStrength(String password, RegistrationPageForm registrationPageForm) {
        if (password.isEmpty()) {
            registrationPageForm.setProgressBarValue(0);
            registrationPageForm.setProgressBarColor(new Color(238, 238, 238));
            registrationPageForm.setPasswordStrengthLabel("");
            return "";
        }

        if (password.length() < 6) {
            registrationPageForm.setProgressBarValue(33);
            registrationPageForm.setProgressBarColor(Color.RED);
            registrationPageForm.setPasswordStrengthLabel("Weak");
            return "Weak";
        }

        boolean hasLetters = Pattern.compile("[a-zA-Z]").matcher(password).find();
        boolean hasDigits = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecialChars = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();

        if (password.length() >= 8 && hasLetters && hasDigits && hasSpecialChars) {
            registrationPageForm.setProgressBarValue(100);
            registrationPageForm.setProgressBarColor(Color.GREEN);
            registrationPageForm.setPasswordStrengthLabel("Strong");
            return "Strong";
        } else if (password.length() >= 6 && hasLetters && hasDigits) {
            registrationPageForm.setProgressBarValue(66);
            registrationPageForm.setProgressBarColor(Color.YELLOW);
            registrationPageForm.setPasswordStrengthLabel("Medium");
            return "Medium";
        } else {
            registrationPageForm.setProgressBarValue(33);
            registrationPageForm.setProgressBarColor(Color.RED);
            registrationPageForm.setPasswordStrengthLabel("Weak");
            return "Weak";
        }
    }

    public static void showTermsAndConditions(RegistrationPageForm registrationPageForm) {
        String terms = "<html><body style='width: 380px;'>"
                + "<h2>Terms and Conditions</h2>"
                + "<p>1. Introduction</p>"
                + "<p>Welcome to our application. By using our application, you agree to be bound by these terms and conditions.</p>"
                + "<p>2. Use of the Application</p>"
                + "<p>You agree to use the application only for lawful purposes and in a way that does not infringe the rights of others.</p>"
                + "<p>3. Privacy</p>"
                + "<p>We are committed to protecting your privacy. Please review our privacy policy for more information.</p>"
                + "<p>4. Disclaimer</p>"
                + "<p>The application is provided 'as is' without warranty of any kind. We do not guarantee the accuracy, completeness, or usefulness of the information provided.</p>"
                + "<p>5. Limitation of Liability</p>"
                + "<p>We shall not be liable for any damages arising out of or in connection with the use of the application.</p>"
                + "<p>6. Changes to Terms</p>"
                + "<p>We reserve the right to change these terms and conditions at any time. Your continued use of the application constitutes your acceptance of the new terms.</p>"
                + "<p>7. Contact Us</p>"
                + "<p>If you have any questions about these terms and conditions, please contact us.</p>"
                + "</body></html>";

        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText(terms);
        textPane.setEditable(false);
        textPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(350, 500));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JOptionPane.showMessageDialog(registrationPageForm, scrollPane, "Terms and Conditions", JOptionPane.PLAIN_MESSAGE);
    }

    public static boolean isTermsShown() {
        return termsShown;
    }

    public static void setTermsShown(boolean shown) {
        termsShown = shown;
    }

    public static void handleGuestBrowsing(GuestPageForm guestPageForm) {
        String username = guestPageForm.getUsername();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(guestPageForm,
                    "Please enter a username",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            long guestId = generateGuestId(connection);

            String checkQuery = "SELECT guest_id FROM guest_customers WHERE guest_name_customer = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                // Duplicate guest name found
                guestId = checkRs.getLong("guest_id");
                loggedInGuestId = guestId;
                loggedInCustomerId = -1;
            } else {
                // Insert new guest customer
                String insertQuery = "INSERT INTO guest_customers (guest_id, guest_name_customer) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setLong(1, guestId);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                loggedInGuestId = guestId;
                loggedInCustomerId = -1;
            }

            JOptionPane.showMessageDialog(guestPageForm,
                    "ENJOY BROWSING FOR NAGIMAS",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE);

            HomePageForm homePage = new HomePageForm(username, username);
            homePage.setVisible(true);
            guestPageForm.dispose();

        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(guestPageForm,
                    "An error occurred while saving your username. Please try again.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    private static long generateGuestId(Connection connection) throws SQLException {
        String query = "SELECT MAX(guest_id) AS max_id FROM guest_customers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long maxId = resultSet.getLong("max_id");
                return maxId + 1;
            } else {
                return 1;
            }
        }
    }

    public static void sendResetCodeEmail(String email) {
        String resetCode = generateResetCode();
        Timestamp expirationTime = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

        if (storeResetCode(email, resetCode, expirationTime)) {
            try {
                Email from = new Email("zensuzui048@gmail.com");
                String subject = "Password Reset Code";
                Email to = new Email(email);
                Content content = new Content("text/plain", "Your password reset code is: " + resetCode);
                Mail mail = new Mail(from, subject, to, content);

                SendGrid sg = new SendGrid("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");
                Request request = new Request();
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);

                if (response.getStatusCode() != 202) {
                    throw new IOException("Failed to send email");
                }

                JOptionPane.showMessageDialog(null, "Reset code sent to your email", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to send email: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Failed to store reset code. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private static boolean storeResetCode(String email, String resetCode, Timestamp expirationTime) {
        try (Connection connection = DBConnection.getConnection()) {
            String updateQuery = "UPDATE customers SET reset_code = ?, reset_code_expiration = ? WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, resetCode);
            preparedStatement.setTimestamp(2, expirationTime);
            preparedStatement.setString(3, email);
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }

    public static boolean verifyResetCode(String email, String resetCode) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT reset_code, reset_code_expiration FROM customers WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedResetCode = resultSet.getString("reset_code");
                Timestamp expirationTime = resultSet.getTimestamp("reset_code_expiration");

                if (resetCode.equals(storedResetCode) && new Timestamp(System.currentTimeMillis()).before(expirationTime)) {
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        return false;
    }

    public static void resetPassword(String email, String newPassword) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());

        try (Connection connection = DBConnection.getConnection()) {
            String updateQuery = "UPDATE customers SET customer_password = ?, reset_code = null, reset_code_expiration = null WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Password reset successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to reset password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }
}
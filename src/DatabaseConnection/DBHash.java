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
package DatabaseConnection;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Jeremy Malana
 */
public class DBHash {
    public static void hashAdminPasswords() {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT id, password_admin FROM admin_users";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password_admin");
                if (!password.startsWith("$2a$")) {
                    String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

                    String updateQuery = "UPDATE admin_users SET password_admin = ? WHERE id = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                    updateStmt.setString(1, hashedPassword);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                }
            }

            System.out.println("Admin passwords updated with hashed values.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public static void hashCustomerPasswords() {
        try (Connection connection = DBConnection.getConnection()) {
            String selectQuery = "SELECT id, customer_password FROM customers";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("customer_password");
                if (!password.startsWith("$2a$")) {
                    String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

                    String updateQuery = "UPDATE customers SET customer_password = ? WHERE id = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                    updateStmt.setString(1, hashedPassword);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                }
            }

            System.out.println("Customer passwords updated with hashed values.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        hashAdminPasswords();
        hashCustomerPasswords();
    }
}
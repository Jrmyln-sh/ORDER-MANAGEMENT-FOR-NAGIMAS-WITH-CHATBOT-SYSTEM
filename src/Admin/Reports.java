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
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author Jeremy Malana
 */
public class Reports {
    static class InventoryItem {
        String productId;
        String productName;
        int stock;
        String stockStatus;
        String productStatus;
    
        public InventoryItem(String productId, String productName, int stock, String stockStatus, String productStatus) {
            this.productId = productId;
            this.productName = productName;
            this.stock = stock;
            this.stockStatus = stockStatus;
            this.productStatus = productStatus;
        }
    }
    
    public void displayInventoryReports(JTable table) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"PRODUCT ID", "PRODUCT NAME", "STOCK", "STOCK STATUS", "PRODUCT STATUS"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        try {
            List<InventoryItem> items = getInventoryData();
            
            for (InventoryItem item : items) {
                model.addRow(new Object[]{
                        item.productId,
                        item.productName,
                        item.stock,
                        item.stockStatus,
                        item.productStatus
                });
            }
            
            table.setModel(model);
    
            JTableHeader header = table.getTableHeader();
            java.awt.Font headerFont = header.getFont();
            header.setFont(new java.awt.Font(headerFont.getName(), java.awt.Font.BOLD, headerFont.getSize()));
            
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(70);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
            table.getColumnModel().getColumn(4).setPreferredWidth(120);
            
            table.setDefaultRenderer(Object.class, new StockStatusColorRenderer());
            
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading inventory data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private List<InventoryItem> getInventoryData() throws SQLException, ClassNotFoundException {
        List<InventoryItem> inventoryItems = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT product_id, product_name, quantity, product_status FROM products";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String productId = rs.getString("product_id");
                    String productName = rs.getString("product_name");
                    int stock = rs.getInt("quantity");
                    String productStatus = rs.getString("product_status");
                    
                    String stockStatus = determineStockStatus(stock);
                   
                    if (stock == 0 && !"Out of Stock".equals(productStatus)) {
                        updateProductStatus(productId, "Out of Stock");
                        productStatus = "Out of Stock";
                    }
                    
                    inventoryItems.add(new InventoryItem(
                            productId, productName, stock, stockStatus, productStatus));
                }
            }
        }
        
        return inventoryItems;
    }
    
    private String determineStockStatus(int stock) {
        if (stock >= 100) return "High";
        if (stock >= 60) return "Medium";
        if (stock >= 30) return "Low";
        if (stock > 0) return "Very Low";
        return "Out of Stock";
    }
    
    private void updateProductStatus(String productId, String status) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE products SET product_status = ? WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, status);
                stmt.setString(2, productId);
                stmt.executeUpdate();
            }
        }
    }
    
    private static class StockStatusColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                  boolean isSelected, boolean hasFocus,
                                                  int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setFont(getFont().deriveFont(java.awt.Font.BOLD));
            
            if (column == 3) {
                String status = value != null ? value.toString() : "";
                if ("High".equals(status)) {
                    c.setForeground(new Color(0, 128, 0)); 
                } else if ("Medium".equals(status)) {
                    c.setForeground(new Color(0, 0, 255));
                } else if ("Low".equals(status)) {
                    c.setForeground(new Color(255, 165, 0));
                } else if ("Very Low".equals(status)) {
                    c.setForeground(new Color(255, 0, 0));
                } else if ("Out of Stock".equals(status)) {
                    c.setForeground(new Color(128, 0, 0));
                }
            } else if (column == 4) {
                String status = value != null ? value.toString() : "";
                if ("Out of Stock".equals(status)) {
                    c.setForeground(new Color(128, 0, 0));
                } else if ("Available".equals(status)) {
                    c.setForeground(new Color(0, 128, 0));
                }
            } else {
                if (!isSelected) {
                    c.setForeground(table.getForeground());
                }
            }
            return c;
        }
    }
    
    public void exportInventoryReportBasedOnSelection(JTable table, JComboBox<String> reportTypeComboBox) {
        String selectedReportType = (String) reportTypeComboBox.getSelectedItem();
        if (selectedReportType == null || selectedReportType.isEmpty()) {
            selectedReportType = "DAILY";
        }
        
        exportInventoryReportToPdf(table, selectedReportType);
    }
    
    public boolean exportInventoryReportToPdf(JTable table, String dateRangeType) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Inventory Report");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(new Date());
            fileChooser.setSelectedFile(new java.io.File("Inventory_" + dateRangeType + "_Report_" + today + ".pdf"));
            
            if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            List<InventoryItem> filteredItems = getFilteredInventoryData(dateRangeType);
    
            generateInventoryPDFWithIText(filePath, filteredItems, dateRangeType);
            
            JOptionPane.showMessageDialog(null, 
                "Inventory Report successfully exported to:\n" + filePath,
                "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error exporting inventory report: " + e.getMessage(),
                "Export Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    private void generateInventoryPDFWithIText(String filePath, List<InventoryItem> items, String dateRangeType) throws Exception {
    
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateTimeFormat.format(new Date());
        
        List<InventoryItem> topStockedProducts = getTopStockedProducts(items, 3);
        List<InventoryItem> outOfStockProducts = getOutOfStockProducts(items);
        List<InventoryItem> lowStockProducts = getLowStockProducts(items, 10);
        
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 24, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font subtitleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font boldFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD, new java.awt.Color(255, 255, 255));
        
        com.lowagie.text.Font highFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, new java.awt.Color(0, 128, 0));
        com.lowagie.text.Font mediumFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, new java.awt.Color(0, 0, 255));
        com.lowagie.text.Font lowFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, new java.awt.Color(255, 165, 0));
        com.lowagie.text.Font veryLowFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, new java.awt.Color(255, 0, 0));
        com.lowagie.text.Font outOfStockFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, new java.awt.Color(128, 0, 0));
        
        Paragraph mainTitle = new Paragraph("INVENTORY REPORT", titleFont);
        mainTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(mainTitle);
        
        Paragraph subtitle = new Paragraph(dateRangeType.toUpperCase() + " REPORT", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingBefore(10);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);
        
        Paragraph reportInfo = new Paragraph();
        reportInfo.add(Chunk.NEWLINE);
        reportInfo.add(new Chunk("Generated On: " + timestamp, boldFont));
        reportInfo.add(Chunk.NEWLINE);
        reportInfo.add(new Chunk("Report Type: " + dateRangeType, boldFont));
        reportInfo.setSpacingAfter(20);
        document.add(reportInfo);
        
        PdfPTable table = new PdfPTable(5); 
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(20);
        
        float[] columnWidths = {1.5f, 3f, 1f, 1.5f, 1.5f};
        table.setWidths(columnWidths);
        
        java.awt.Color headerBgColor = new java.awt.Color(51, 51, 102);
        
        PdfPCell headerCell;
        headerCell = new PdfPCell(new Phrase("PRODUCT ID", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("PRODUCT NAME", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
    
        headerCell = new PdfPCell(new Phrase("STOCK", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("STOCK STATUS", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("PRODUCT STATUS", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
    
        boolean alternateRow = false;
        for (InventoryItem item : items) {
    
            java.awt.Color bgColor = alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE;
            alternateRow = !alternateRow;
    
            PdfPCell cell = new PdfPCell(new Phrase(item.productId, boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.productName, boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
    
            cell = new PdfPCell(new Phrase(String.valueOf(item.stock), boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            com.lowagie.text.Font statusFont;
            switch (item.stockStatus) {
                case "High": statusFont = highFont; break;
                case "Medium": statusFont = mediumFont; break;
                case "Low": statusFont = lowFont; break;
                case "Very Low": statusFont = veryLowFont; break;
                case "Out of Stock": statusFont = outOfStockFont; break;
                default: statusFont = boldFont;
            }
            
            cell = new PdfPCell(new Phrase(item.stockStatus, statusFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            com.lowagie.text.Font productStatusFont;
            if ("Available".equals(item.productStatus)) {
                productStatusFont = highFont;
            } else if ("Out of Stock".equals(item.productStatus)) {
                productStatusFont = outOfStockFont;
            } else {
                productStatusFont = boldFont;
            }
            
            cell = new PdfPCell(new Phrase(item.productStatus, productStatusFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
        }
        
        document.add(table);
        
        Paragraph summaryTitle = new Paragraph("SUMMARY OF INVENTORY REPORTS", subtitleFont);
        summaryTitle.setAlignment(Element.ALIGN_CENTER);
        summaryTitle.setSpacingBefore(20);
        summaryTitle.setSpacingAfter(15);
        document.add(summaryTitle);
        
        document.add(new Paragraph("Top 3 Most Stocked Products:", boldFont));
        if (topStockedProducts.isEmpty()) {
            document.add(new Paragraph("No data available", normalFont));
        } else {
            PdfPTable topStockedTable = new PdfPTable(3);
            topStockedTable.setWidthPercentage(100);
            topStockedTable.setSpacingBefore(5);
            topStockedTable.setSpacingAfter(15);
            
            headerCell = new PdfPCell(new Phrase("PRODUCT ID", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            topStockedTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("PRODUCT NAME", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            topStockedTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("STOCK", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            topStockedTable.addCell(headerCell);
            
            alternateRow = false;
            for (InventoryItem item : topStockedProducts) {
                java.awt.Color rowColor = alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE;
                alternateRow = !alternateRow;
                
                PdfPCell cell = new PdfPCell(new Phrase(item.productId, boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                topStockedTable.addCell(cell);
                
                cell = new PdfPCell(new Phrase(item.productName, boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                topStockedTable.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(item.stock), boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                topStockedTable.addCell(cell);
            }
            document.add(topStockedTable);
        }
        
        document.add(new Paragraph("Out of Stock Products:", boldFont));
        if (outOfStockProducts.isEmpty()) {
            document.add(new Paragraph("No products are out of stock", normalFont));
        } else {
            PdfPTable outOfStockTable = new PdfPTable(3);
            outOfStockTable.setWidthPercentage(100);
            outOfStockTable.setSpacingBefore(5);
            outOfStockTable.setSpacingAfter(15);
            
    
            headerCell = new PdfPCell(new Phrase("PRODUCT ID", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            outOfStockTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("PRODUCT NAME", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            outOfStockTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("PRODUCT STATUS", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            outOfStockTable.addCell(headerCell);
            
            alternateRow = false;
            for (InventoryItem item : outOfStockProducts) {
                java.awt.Color rowColor = alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE;
                alternateRow = !alternateRow;
                
                PdfPCell cell = new PdfPCell(new Phrase(item.productId, boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                outOfStockTable.addCell(cell);
                
                cell = new PdfPCell(new Phrase(item.productName, boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                outOfStockTable.addCell(cell);
                
                cell = new PdfPCell(new Phrase(item.productStatus, outOfStockFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                outOfStockTable.addCell(cell);
            }
            document.add(outOfStockTable);
        }
        
        document.add(new Paragraph("Low Stock Alerts (Products with less than 10 units):", boldFont));
        if (lowStockProducts.isEmpty()) {
            document.add(new Paragraph("No products have low stock", normalFont));
        } else {
            PdfPTable lowStockTable = new PdfPTable(4);
            lowStockTable.setWidthPercentage(100);
            lowStockTable.setSpacingBefore(5);
            lowStockTable.setSpacingAfter(15);
    
            headerCell = new PdfPCell(new Phrase("PRODUCT ID", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            lowStockTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("PRODUCT NAME", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            lowStockTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("STOCK", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            lowStockTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("STOCK STATUS", headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            lowStockTable.addCell(headerCell);
    
            alternateRow = false;
            for (InventoryItem item : lowStockProducts) {
                java.awt.Color rowColor = alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE;
                alternateRow = !alternateRow;
                
                PdfPCell cell = new PdfPCell(new Phrase(item.productId, boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                lowStockTable.addCell(cell);
                
                cell = new PdfPCell(new Phrase(item.productName, boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                lowStockTable.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf(item.stock), boldFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                lowStockTable.addCell(cell);
    
                com.lowagie.text.Font statusFont;
                switch (item.stockStatus) {
                    case "Low": statusFont = lowFont; break;
                    case "Very Low": statusFont = veryLowFont; break;
                    default: statusFont = boldFont;
                }
                
                cell = new PdfPCell(new Phrase(item.stockStatus, statusFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                lowStockTable.addCell(cell);
            }
            document.add(lowStockTable);
        }
        
        document.close();
    }
    
    private List<InventoryItem> getFilteredInventoryData(String dateRangeType) throws SQLException, ClassNotFoundException {
        List<InventoryItem> inventoryItems = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql;
            
            if (dateRangeType.equalsIgnoreCase("ALL")) {
                sql = "SELECT product_id, product_name, quantity, product_status FROM products";
            } else {
                String dateCondition;
                switch (dateRangeType.toUpperCase()) {
                    case "DAILY":
                        dateCondition = "DATE(last_updated) = CURDATE()";
                        break;
                    case "WEEKLY":
                        dateCondition = "last_updated >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
                        break;
                    case "MONTHLY":
                        dateCondition = "last_updated >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
                        break;
                    case "YEARLY":
                        dateCondition = "last_updated >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR)";
                        break;
                    default:
                        dateCondition = "1=1";
                }
    
                sql = "SELECT product_id, product_name, quantity, product_status " +
                     "FROM products " +
                     "WHERE " + dateCondition;
            }
            
            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String productId = rs.getString("product_id");
                    String productName = rs.getString("product_name");
                    int stock = rs.getInt("quantity");
                    String productStatus = rs.getString("product_status");
                    
                    String stockStatus = determineStockStatus(stock);
                    
                    inventoryItems.add(new InventoryItem(
                            productId, productName, stock, stockStatus, productStatus));
                }
            } catch (SQLException e) {
                System.err.println("Error with date-filtered query: " + e.getMessage());
                System.err.println("Falling back to basic query without date filtering");
                
                sql = "SELECT product_id, product_name, quantity, product_status FROM products";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String productId = rs.getString("product_id");
                    String productName = rs.getString("product_name");
                    int stock = rs.getInt("quantity");
                    String productStatus = rs.getString("product_status");
                    
                    String stockStatus = determineStockStatus(stock);
                    
                    inventoryItems.add(new InventoryItem(
                            productId, productName, stock, stockStatus, productStatus));
                }
            }
        }
        
        return inventoryItems;
    }
    
    private List<InventoryItem> getTopStockedProducts(List<InventoryItem> items, int n) {
        List<InventoryItem> sortedItems = new ArrayList<>(items);
        sortedItems.sort((a, b) -> Integer.compare(b.stock, a.stock));
        int limit = Math.min(n, sortedItems.size());
        return sortedItems.subList(0, limit);
    }
    
    private List<InventoryItem> getOutOfStockProducts(List<InventoryItem> items) {
        List<InventoryItem> outOfStockItems = new ArrayList<>();
        for (InventoryItem item : items) {
            if (item.stock == 0) {
                outOfStockItems.add(item);
            }
        }
        return outOfStockItems;
    }
    
    private List<InventoryItem> getLowStockProducts(List<InventoryItem> items, int threshold) {
        List<InventoryItem> lowStockItems = new ArrayList<>();
        for (InventoryItem item : items) {
            if (item.stock > 0 && item.stock < threshold) {
                lowStockItems.add(item);
            }
        }
        return lowStockItems;
    }

/* 
END OF INVENTORY REPORTS
*/
    
   
/**
 * CUSTOMER REPORTS
 */
    static class CustomerReportItem {
        String registrationDate;
        String customerId;
        String customerName;
        String customerType;
        int totalOrders;
    
        public CustomerReportItem(String registrationDate, String customerId, String customerName, 
                                 String customerType, int totalOrders) {
            this.registrationDate = registrationDate;
            this.customerId = customerId;
            this.customerName = customerName;
            this.customerType = customerType;
            this.totalOrders = totalOrders;
        }
    }
    
    public void displayCustomerReports(JTable table) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"DATE REGISTRATION", "CUSTOMER ID", "CUSTOMER NAME", "CUSTOMER TYPE", "TOTAL ORDERS"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        try {
            List<CustomerReportItem> items = new ArrayList<>();
            
            try {
                items.addAll(getRegularCustomers());
            } catch (Exception e) {
                System.err.println("Error loading regular customers: " + e.getMessage());
            }
            
            try {
                items.addAll(getGuestCustomers());
            } catch (Exception e) {
                System.err.println("Error loading guest customers: " + e.getMessage());
            }
            
            items.sort((a, b) -> b.registrationDate.compareTo(a.registrationDate));
            
            for (CustomerReportItem item : items) {
                model.addRow(new Object[]{
                        item.registrationDate,
                        item.customerId,
                        item.customerName,
                        item.customerType,
                        item.totalOrders
                });
            }
            
            table.setModel(model);
            
            JTableHeader header = table.getTableHeader();
            java.awt.Font headerFont = header.getFont();
            header.setFont(new java.awt.Font(headerFont.getName(), java.awt.Font.BOLD, headerFont.getSize()));
            
            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(180);
            table.getColumnModel().getColumn(3).setPreferredWidth(120);
            table.getColumnModel().getColumn(4).setPreferredWidth(120);
            
            table.setDefaultRenderer(Object.class, new CustomerReportRenderer());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading customer data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
            table.setModel(model);
        }
    }
    
    private static class CustomerReportRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                  boolean isSelected, boolean hasFocus,
                                                  int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setFont(getFont().deriveFont(java.awt.Font.BOLD));
            
            if (column == 4) {
                setHorizontalAlignment(JLabel.CENTER);
            } else {
                setHorizontalAlignment(JLabel.LEFT);
            }
            
            if (column == 4 && value != null) {
                int orderCount = Integer.parseInt(value.toString());
                if (orderCount > 5) {
                    c.setForeground(new Color(0, 128, 0));
                } else {
                    if (!isSelected) {
                        c.setForeground(table.getForeground());
                    }
                }
            } else {
                if (!isSelected) {
                    c.setForeground(table.getForeground());
                }
            }
            
            return c;
        }
    }
    
    private List<CustomerReportItem> getAllCustomerData() throws SQLException, ClassNotFoundException {
        List<CustomerReportItem> allCustomers = new ArrayList<>();
        
        allCustomers.addAll(getRegularCustomers());
        allCustomers.addAll(getGuestCustomers());
        
        allCustomers.sort((a, b) -> b.registrationDate.compareTo(a.registrationDate));
        
        return allCustomers;
    }
    
    private List<CustomerReportItem> getRegularCustomers() throws SQLException, ClassNotFoundException {
        List<CustomerReportItem> customers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT customer_id, customer_name, created_at FROM customers";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String customerId = String.valueOf(rs.getInt("customer_id"));
                    String customerName = rs.getString("customer_name");
                    String registrationDate = rs.getString("created_at");
                    
                    if (registrationDate != null) {
                        try {
                            registrationDate = formatDateTime(registrationDate);
                        } catch (Exception e) {
                            System.err.println("Error formatting date: " + e.getMessage());
                        }
                    }
                    
                    int totalOrders = getCompletedOrdersCount(customerId, "regular");
                    
                    customers.add(new CustomerReportItem(
                            registrationDate, 
                            customerId, 
                            customerName, 
                            "Regular", 
                            totalOrders));
                }
            }
        }
        
        return customers;
    }
    
    private List<CustomerReportItem> getGuestCustomers() throws SQLException, ClassNotFoundException {
        List<CustomerReportItem> guests = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT guest_id, guest_name_customer, created_at FROM guest_customers";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String guestId = String.valueOf(rs.getInt("guest_id"));
                    String guestName = rs.getString("guest_name_customer");
                    String registrationDate = rs.getString("created_at");
                    
                    if (registrationDate != null) {
                        try {
                            registrationDate = formatDateTime(registrationDate);
                        } catch (Exception e) {
                            System.err.println("Error formatting date: " + e.getMessage());
                        }
                    }
                    
                    int totalOrders = getCompletedOrdersCount(guestId, "guest");
                    
                    guests.add(new CustomerReportItem(
                            registrationDate, 
                            guestId, 
                            guestName, 
                            "Guest", 
                            totalOrders));
                }
            }
        }
        
        return guests;
    }
    
    private int getCompletedOrdersCount(String customerId, String customerType) throws SQLException, ClassNotFoundException {
        int count = 0;
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql;
            if (customerType.equalsIgnoreCase("regular")) {
                sql = "SELECT COUNT(*) as order_count FROM customer_orders WHERE customer_id = ? AND order_status = 'Completed'";
            } else {
                sql = "SELECT COUNT(*) as order_count FROM guest_orders WHERE guest_id = ? AND order_status = 'Completed'";
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, customerId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    count = rs.getInt("order_count");
                }
            }
        }
        
        return count;
    }
    
    public boolean exportCustomerReportToPdf(JTable table, String dateRangeType) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Customer Report");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(new Date());
            fileChooser.setSelectedFile(new java.io.File("Customer_" + dateRangeType + "_Report_" + today + ".pdf"));
            
            if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            List<CustomerReportItem> tableItems = extractDataFromTable(table);
            
            generateCustomerPDFWithIText(filePath, tableItems, dateRangeType);
            
            JOptionPane.showMessageDialog(null, 
                "Customer Report successfully exported to:\n" + filePath,
                "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error exporting customer report: " + e.getMessage(),
                "Export Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    private List<CustomerReportItem> extractDataFromTable(JTable table) {
        List<CustomerReportItem> items = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String registrationDate = model.getValueAt(i, 0).toString();
            String customerId = model.getValueAt(i, 1).toString();
            String customerName = model.getValueAt(i, 2).toString();
            String customerType = model.getValueAt(i, 3).toString();
            int totalOrders = Integer.parseInt(model.getValueAt(i, 4).toString());
            
            items.add(new CustomerReportItem(
                registrationDate,
                customerId,
                customerName,
                customerType,
                totalOrders
            ));
        }
        
        return items;
    }
    
    private List<CustomerReportItem> getFilteredCustomerData(String dateRangeType) throws SQLException, ClassNotFoundException {
        List<CustomerReportItem> allCustomers = getAllCustomerData();
        
        if (dateRangeType.equalsIgnoreCase("DAILY")) {
            return filterCustomersByDate(allCustomers, 0);
        } else if (dateRangeType.equalsIgnoreCase("WEEKLY")) {
            return filterCustomersByDate(allCustomers, 7);
        } else if (dateRangeType.equalsIgnoreCase("MONTHLY")) {
            return filterCustomersByDate(allCustomers, 30);
        } else if (dateRangeType.equalsIgnoreCase("YEARLY")) {
            return filterCustomersByDate(allCustomers, 365);
        }
        
        return allCustomers;
    }
    
    private List<CustomerReportItem> filterCustomersByDate(List<CustomerReportItem> customers, int daysBack) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -daysBack);
        Date cutoffDate = cal.getTime();
        
        List<CustomerReportItem> filtered = new ArrayList<>();
        
        for (CustomerReportItem customer : customers) {
            try {
                Date registrationDate = format.parse(customer.registrationDate);
                
                if (daysBack == 0) {
                    Calendar regCal = Calendar.getInstance();
                    regCal.setTime(registrationDate);
                    
                    Calendar todayCal = Calendar.getInstance();
                    
                    if (regCal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR) &&
                        regCal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR)) {
                        filtered.add(customer);
                    }
                } else {
                    if (registrationDate.after(cutoffDate)) {
                        filtered.add(customer);
                    }
                }
            } catch (Exception e) {
                filtered.add(customer);
            }
        }
        
        return filtered;
    }
    
    private String formatDateTime(String dateTime) {
        try {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dbFormat.parse(dateTime);
            
            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return displayFormat.format(date);
        } catch (Exception e) {
            return dateTime;
        }
    }
    
    private int getTotalRegularCustomers() throws SQLException, ClassNotFoundException {
        int count = 0;
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) as total FROM customers";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        }
        
        return count;
    }
    
    private int getTotalGuestCustomers() throws SQLException, ClassNotFoundException {
        int count = 0;
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) as total FROM guest_customers";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        }
        
        return count;
    }
    
    private void generateCustomerPDFWithIText(String filePath, List<CustomerReportItem> items, String dateRangeType) throws Exception {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = timestampFormat.format(new Date());
        
        int totalRegularCustomersInReport = 0;
        int totalGuestCustomersInReport = 0;
        int totalOrdersPlaced = 0;
        
        for (CustomerReportItem item : items) {
            if (item.customerType.equals("Regular")) {
                totalRegularCustomersInReport++;
            } else {
                totalGuestCustomersInReport++;
            }
            totalOrdersPlaced += item.totalOrders;
        }
        
        int totalCustomersInReport = items.size();
        
        int totalAllRegularCustomers = getTotalRegularCustomers();
        int totalAllGuestCustomers = getTotalGuestCustomers();
        
        double regularPercentage = 0;
        double guestPercentage = 0;
        
        if (totalCustomersInReport > 0) {
            regularPercentage = (double) totalRegularCustomersInReport / totalCustomersInReport * 100;
            guestPercentage = (double) totalGuestCustomersInReport / totalCustomersInReport * 100;
        }
        
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 24, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font subtitleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font boldFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD, new java.awt.Color(255, 255, 255));
        com.lowagie.text.Font whiteHeaderFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, java.awt.Color.WHITE);
        
        Paragraph mainTitle = new Paragraph("CUSTOMER REPORT", titleFont);
        mainTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(mainTitle);
        
        Paragraph subtitle = new Paragraph(dateRangeType.toUpperCase() + " REPORT", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingBefore(10);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);
        
        Paragraph reportInfo = new Paragraph();
        reportInfo.add(new Chunk("Generated On: " + timestamp, boldFont));
        reportInfo.add(Chunk.NEWLINE);
        reportInfo.add(new Chunk("Report Type: " + dateRangeType, boldFont));
        reportInfo.setSpacingAfter(20);
        document.add(reportInfo);
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(20);
        
        float[] columnWidths = {2f, 1.5f, 3f, 1.5f, 1f};
        table.setWidths(columnWidths);
        
        java.awt.Color headerBgColor = new java.awt.Color(51, 51, 102);
        
        PdfPCell headerCell;
        headerCell = new PdfPCell(new Phrase("DATE REGISTRATION", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("CUSTOMER ID", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("CUSTOMER NAME", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("CUSTOMER TYPE", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("TOTAL ORDERS", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        table.addCell(headerCell);
        
        boolean alternateRow = false;
        for (CustomerReportItem item : items) {
            java.awt.Color bgColor = alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE;
            alternateRow = !alternateRow;
            
            PdfPCell cell = new PdfPCell(new Phrase(item.registrationDate, boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.customerId, boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.customerName, boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.customerType, boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(String.valueOf(item.totalOrders), boldFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        
        document.add(table);
        
        Paragraph summaryTitle = new Paragraph("SUMMARY OF CUSTOMER REPORTS", subtitleFont);
        summaryTitle.setAlignment(Element.ALIGN_CENTER);
        summaryTitle.setSpacingBefore(20);
        summaryTitle.setSpacingAfter(15);
        document.add(summaryTitle);
        
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(70);
        summaryTable.setSpacingBefore(10);
        summaryTable.setSpacingAfter(20);
        summaryTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        float[] summaryColumnWidths = {3f, 1f};
        summaryTable.setWidths(summaryColumnWidths);
        
        headerCell = new PdfPCell(new Phrase("METRIC", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        summaryTable.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("VALUE", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        summaryTable.addCell(headerCell);
        
        PdfPCell cell;
        alternateRow = false;
        
        cell = new PdfPCell(new Phrase("TOTAL OF ALL CUSTOMERS", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(totalAllRegularCustomers), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL OF ALL GUEST", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(totalAllGuestCustomers), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL OF CUSTOMERS (" + dateRangeType.toUpperCase() + ")", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(totalRegularCustomersInReport), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL OF GUEST (" + dateRangeType.toUpperCase() + ")", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(totalGuestCustomersInReport), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("CUSTOMER TYPES BREAKDOWN (BY PERCENTAGE)", whiteHeaderFont));
        cell.setPadding(5);
        cell.setBackgroundColor(headerBgColor);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = false;
        
        cell = new PdfPCell(new Phrase("REGULAR CUSTOMERS", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.format("%.2f%%", regularPercentage), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("GUEST CUSTOMERS", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.format("%.2f%%", guestPercentage), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        
        document.add(summaryTable);
        
        document.close();
    }

/*
END OF CUSTOMER REPORTS
*/


/**
 * SALES REPORTS
 */
    static class SalesReportItem {
        String transactionNumber;
        String customerName;
        String customerType;
        String productName;
        String category;
        int quantity;
        double unitPrice;
        double totalPrice;
        String orderStatus;
    
        public SalesReportItem(String transactionNumber, String customerName, 
                              String customerType, String productName, String category,
                              int quantity, double unitPrice, double totalPrice, String orderStatus) {
            this.transactionNumber = transactionNumber;
            this.customerName = customerName;
            this.customerType = customerType;
            this.productName = productName;
            this.category = category;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
            this.orderStatus = orderStatus;
        }
    }
    
    public void displaySalesReports(JTable table) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"TRANSACTION NUMBER", "CUSTOMER NAME", "CUSTOMER TYPE", 
                             "PRODUCTS", "CATEGORY", "QUANTITY", "UNIT PRICE", "TOTAL PRICE", "ORDER STATUS"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return Integer.class;
                if (columnIndex == 6 || columnIndex == 7) return Double.class;
                return String.class;
            }
        };
    
        try {
            List<SalesReportItem> items = new ArrayList<>();
            
            try {
                items.addAll(getRegularCustomerSales());
            } catch (Exception e) {
                System.err.println("Error loading regular customer sales: " + e.getMessage());
                e.printStackTrace();
            }
            
            try {
                items.addAll(getGuestCustomerSales());
            } catch (Exception e) {
                System.err.println("Error loading guest customer sales: " + e.getMessage());
                e.printStackTrace();
            }
            
            items.sort((a, b) -> b.transactionNumber.compareTo(a.transactionNumber));
            
            for (SalesReportItem item : items) {
                model.addRow(new Object[]{
                        item.transactionNumber,
                        item.customerName,
                        item.customerType,
                        item.productName,
                        item.category,
                        item.quantity,
                        item.unitPrice,
                        item.totalPrice,
                        item.orderStatus
                });
            }
            
            table.setModel(model);
            
            JTableHeader header = table.getTableHeader();
            java.awt.Font headerFont = header.getFont();
            header.setFont(new java.awt.Font(headerFont.getName(), java.awt.Font.BOLD, headerFont.getSize()));
            
            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(120);
            table.getColumnModel().getColumn(3).setPreferredWidth(180);
            table.getColumnModel().getColumn(4).setPreferredWidth(120);
            table.getColumnModel().getColumn(5).setPreferredWidth(80);
            table.getColumnModel().getColumn(6).setPreferredWidth(100);
            table.getColumnModel().getColumn(7).setPreferredWidth(100);
            table.getColumnModel().getColumn(8).setPreferredWidth(120);
            
            table.setDefaultRenderer(Object.class, new SalesReportRenderer());
            table.setDefaultRenderer(Double.class, new SalesReportRenderer());
            table.setDefaultRenderer(Integer.class, new SalesReportRenderer());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading sales data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
            table.setModel(model);
        }
    }
    
    private static class SalesReportRenderer extends DefaultTableCellRenderer {
        private static final NumberFormat currencyFormat;
        
        static {
            currencyFormat = NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols symbols = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols();
            symbols.setCurrencySymbol("₱");
            ((DecimalFormat) currencyFormat).setDecimalFormatSymbols(symbols);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                  boolean isSelected, boolean hasFocus,
                                                  int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setFont(getFont().deriveFont(java.awt.Font.BOLD));
            
            if (column == 6 || column == 7) {
                if (value != null) {
                    Double amount = (Double) value;
                    setText(currencyFormat.format(amount));
                }
                setHorizontalAlignment(JLabel.RIGHT);
            } 
            else if (column == 5) {
                setHorizontalAlignment(JLabel.CENTER);
            }
            else if (column == 8) {
                setHorizontalAlignment(JLabel.CENTER);
                
                if (value != null) {
                    String status = value.toString();
                    if (status.equalsIgnoreCase("Completed")) {
                        c.setForeground(new Color(0, 128, 0));
                    } else if (status.equalsIgnoreCase("Processing")) {
                        c.setForeground(new Color(255, 165, 0));
                    } else if (status.equalsIgnoreCase("Cancelled")) {
                        c.setForeground(new Color(220, 20, 60));
                    } else {
                        if (!isSelected) {
                            c.setForeground(table.getForeground());
                        }
                    }
                }
            } else {
                setHorizontalAlignment(JLabel.LEFT);
                if (!isSelected) {
                    c.setForeground(table.getForeground());
                }
            }
            
            return c;
        }
    }
    
    private List<SalesReportItem> getRegularCustomerSales() throws SQLException, ClassNotFoundException {
        List<SalesReportItem> salesItems = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = 
                "SELECT co.transaction_number, co.order_status, c.customer_name, " +
                "coi.product_id, coi.quantity, coi.price, " +
                "p.product_name, p.categories " +
                "FROM customer_orders co " +
                "JOIN customers c ON co.customer_id = c.customer_id " +
                "JOIN customer_order_items coi ON co.customer_order_id = coi.order_id " +
                "JOIN products p ON coi.product_id = p.product_id " +
                "ORDER BY co.transaction_number DESC";
                
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String transactionNumber = rs.getString("transaction_number");
                    String customerName = rs.getString("customer_name");
                    String productName = rs.getString("product_name");
                    String category = rs.getString("categories");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("price") / quantity;
                    double totalPrice = rs.getDouble("price");
                    String orderStatus = rs.getString("order_status");
                    
                    salesItems.add(new SalesReportItem(
                        transactionNumber,
                        customerName,
                        "Regular",
                        productName,
                        category,
                        quantity,
                        unitPrice,
                        totalPrice,
                        orderStatus
                    ));
                }
            }
        }
        
        return salesItems;
    }
    
    private List<SalesReportItem> getGuestCustomerSales() throws SQLException, ClassNotFoundException {
        List<SalesReportItem> salesItems = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = 
                "SELECT go.transaction_number, go.order_status, gc.guest_name_customer, " +
                "goi.product_id, goi.quantity, goi.price, " +
                "p.product_name, p.categories " +
                "FROM guest_orders go " +
                "JOIN guest_customers gc ON go.guest_id = gc.guest_id " +
                "JOIN guest_order_items goi ON go.guest_order_id = goi.order_id " +
                "JOIN products p ON goi.product_id = p.product_id " +
                "ORDER BY go.transaction_number DESC";
                
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String transactionNumber = rs.getString("transaction_number");
                    String customerName = rs.getString("guest_name_customer");
                    String productName = rs.getString("product_name");
                    String category = rs.getString("categories");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("price") / quantity;
                    double totalPrice = rs.getDouble("price");
                    String orderStatus = rs.getString("order_status");
                    
                    salesItems.add(new SalesReportItem(
                        transactionNumber,
                        customerName,
                        "Guest",
                        productName,
                        category,
                        quantity,
                        unitPrice,
                        totalPrice,
                        orderStatus
                    ));
                }
            }
        }
        
        return salesItems;
    }
    
    public boolean exportSalesReportToPdf(JTable table, String dateRangeType) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Sales Report");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(new Date());
            fileChooser.setSelectedFile(new java.io.File("Sales_" + dateRangeType + "_Report_" + today + ".pdf"));
            
            if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            List<SalesReportItem> tableItems = extractDataFromSalesTable(table);
            
            generateSalesPDFWithIText(filePath, tableItems, dateRangeType);
            
            JOptionPane.showMessageDialog(null, 
                "Sales Report successfully exported to:\n" + filePath,
                "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error exporting sales report: " + e.getMessage(),
                "Export Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    private List<SalesReportItem> extractDataFromSalesTable(JTable table) {
        List<SalesReportItem> items = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String transactionNumber = model.getValueAt(i, 0).toString();
            String customerName = model.getValueAt(i, 1).toString();
            String customerType = model.getValueAt(i, 2).toString();
            String productName = model.getValueAt(i, 3).toString();
            String category = model.getValueAt(i, 4).toString();
            int quantity = Integer.parseInt(model.getValueAt(i, 5).toString());
            
            double unitPrice = 0;
            double totalPrice = 0;
            
            try {
                unitPrice = Double.parseDouble(model.getValueAt(i, 6).toString().replace("₱", "").replace(",", ""));
            } catch (Exception e) {
                unitPrice = (double) model.getValueAt(i, 6);
            }
            
            try {
                totalPrice = Double.parseDouble(model.getValueAt(i, 7).toString().replace("₱", "").replace(",", ""));
            } catch (Exception e) {
                totalPrice = (double) model.getValueAt(i, 7);
            }
            
            String orderStatus = model.getValueAt(i, 8).toString();
            
            items.add(new SalesReportItem(
                transactionNumber,
                customerName,
                customerType,
                productName,
                category,
                quantity,
                unitPrice,
                totalPrice,
                orderStatus
            ));
        }
        
        return items;
    }
    
    private void generateSalesPDFWithIText(String filePath, List<SalesReportItem> items, String dateRangeType) throws Exception {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = timestampFormat.format(new Date());
        
        double totalSales = 0;
        double totalCompletedSales = 0;
        int totalPendingOrders = 0;
        int totalCancelledOrders = 0;
        int totalTransactions = 0;
        
        Set<String> uniqueTransactions = new HashSet<>();
        Set<String> completedTransactions = new HashSet<>();
        Set<String> pendingTransactions = new HashSet<>();
        Set<String> cancelledTransactions = new HashSet<>();
        
        Map<String, Integer> productPurchaseCount = new HashMap<>();
        Map<String, Integer> categoryPurchaseCount = new HashMap<>();
        
        for (SalesReportItem item : items) {
            if (!item.orderStatus.equalsIgnoreCase("Cancelled")) {
                totalSales += item.totalPrice;
            }
            
            uniqueTransactions.add(item.transactionNumber);
            
            if (item.orderStatus.equalsIgnoreCase("Completed")) {
                totalCompletedSales += item.totalPrice;
                completedTransactions.add(item.transactionNumber);
            } else if (item.orderStatus.equalsIgnoreCase("Pending")) {
                pendingTransactions.add(item.transactionNumber);
            } else if (item.orderStatus.equalsIgnoreCase("Cancelled")) {
                cancelledTransactions.add(item.transactionNumber);
            }
            
            Integer productCount = productPurchaseCount.getOrDefault(item.productName, 0);
            productPurchaseCount.put(item.productName, productCount + item.quantity);
            
            Integer categoryCount = categoryPurchaseCount.getOrDefault(item.category, 0);
            categoryPurchaseCount.put(item.category, categoryCount + item.quantity);
        }
        
        totalTransactions = uniqueTransactions.size();
        
        List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productPurchaseCount.entrySet());
        sortedProducts.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        List<Map.Entry<String, Integer>> top3Products = sortedProducts.size() > 3 ? 
            sortedProducts.subList(0, 3) : sortedProducts;
            
        String mostPopularCategory = "";
        int maxCategoryCount = 0;
        for (Map.Entry<String, Integer> entry : categoryPurchaseCount.entrySet()) {
            if (entry.getValue() > maxCategoryCount) {
                mostPopularCategory = entry.getKey();
                maxCategoryCount = entry.getValue();
            }
        }
        
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 24, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font subtitleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font boldFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD, new java.awt.Color(255, 255, 255));
        com.lowagie.text.Font whiteHeaderFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.BOLD, java.awt.Color.WHITE);
        
        Paragraph mainTitle = new Paragraph("SALES REPORT", titleFont);
        mainTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(mainTitle);
        
        Paragraph subtitle = new Paragraph(dateRangeType.toUpperCase() + " REPORT", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingBefore(10);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);
        
        Paragraph reportInfo = new Paragraph();
        reportInfo.add(new Chunk("Generated On: " + timestamp, boldFont));
        reportInfo.add(Chunk.NEWLINE);
        reportInfo.add(new Chunk("Report Type: " + dateRangeType, boldFont));
        reportInfo.setSpacingAfter(20);
        document.add(reportInfo);
        
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(20);
        
        float[] columnWidths = {1.5f, 1.5f, 1f, 1.5f, 1f, 0.7f, 1f, 1f, 1f};
        table.setWidths(columnWidths);
        
        java.awt.Color headerBgColor = new java.awt.Color(51, 51, 102);
        
        PdfPCell headerCell;
        String[] headers = {"TRANSACTION NUMBER", "CUSTOMER NAME", "CUSTOMER TYPE", 
                           "PRODUCTS", "CATEGORY", "QUANTITY", "UNIT PRICE", "TOTAL PRICE", "ORDER STATUS"};
        
        for (String header : headers) {
            headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(headerBgColor);
            headerCell.setPadding(5);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
        
        NumberFormat pesoCurrencyFormat = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) pesoCurrencyFormat).getDecimalFormatSymbols();
        symbols.setCurrencySymbol("₱");
        ((DecimalFormat) pesoCurrencyFormat).setDecimalFormatSymbols(symbols);
        
        boolean alternateRow = false;
        
        for (SalesReportItem item : items) {
            java.awt.Color bgColor = alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE;
            alternateRow = !alternateRow;
            
            PdfPCell cell = new PdfPCell(new Phrase(item.transactionNumber, normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.customerName, normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.customerType, normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.productName, normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.category, normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(String.valueOf(item.quantity), normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(pesoCurrencyFormat.format(item.unitPrice), normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(pesoCurrencyFormat.format(item.totalPrice), normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(item.orderStatus, normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        
        document.add(table);
        
        Paragraph summaryTitle = new Paragraph("SUMMARY OF SALES REPORTS", subtitleFont);
        summaryTitle.setAlignment(Element.ALIGN_CENTER);
        summaryTitle.setSpacingBefore(20);
        summaryTitle.setSpacingAfter(15);
        document.add(summaryTitle);
        
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(70);
        summaryTable.setSpacingBefore(10);
        summaryTable.setSpacingAfter(20);
        summaryTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        float[] summaryColumnWidths = {3f, 1.5f};
        summaryTable.setWidths(summaryColumnWidths);
        
        headerCell = new PdfPCell(new Phrase("METRIC", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        summaryTable.addCell(headerCell);
        
        headerCell = new PdfPCell(new Phrase("VALUE", headerFont));
        headerCell.setBackgroundColor(headerBgColor);
        headerCell.setPadding(5);
        summaryTable.addCell(headerCell);
        
        PdfPCell cell;
        alternateRow = false;
        
        cell = new PdfPCell(new Phrase("TOP 3 MOST PRODUCTS PURCHASED", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setRowspan(top3Products.size() > 0 ? top3Products.size() : 1);
        summaryTable.addCell(cell);
        
        if (top3Products.size() > 0) {
            for (int i = 0; i < top3Products.size(); i++) {
                Map.Entry<String, Integer> entry = top3Products.get(i);
                
                cell = new PdfPCell(new Phrase(String.format("%d. %s (%d units)", 
                    i+1, entry.getKey(), entry.getValue()), normalFont));
                cell.setPadding(5);
                cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
                summaryTable.addCell(cell);
                
                if (i < top3Products.size() - 1) {
                    summaryTable.completeRow();
                }
            }
        } else {
            cell = new PdfPCell(new Phrase("No products found", normalFont));
            cell.setPadding(5);
            cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
            summaryTable.addCell(cell);
        }
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("MOST POPULAR CATEGORY", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(!mostPopularCategory.isEmpty() ? 
            mostPopularCategory + " (" + maxCategoryCount + " units)" : "No category found", normalFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL SALES (₱) (COMPLETED ORDERS)", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(pesoCurrencyFormat.format(totalCompletedSales), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL PENDING ORDERS", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(pendingTransactions.size()), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL CANCELLED ORDERS", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(cancelledTransactions.size()), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL TRANSACTIONS", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(String.valueOf(totalTransactions), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryTable.addCell(cell);
        alternateRow = !alternateRow;
        
        cell = new PdfPCell(new Phrase("TOTAL SALES (EXCLUDING CANCELLED)", boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        summaryTable.addCell(cell);
        
        cell = new PdfPCell(new Phrase(pesoCurrencyFormat.format(totalSales), boldFont));
        cell.setPadding(5);
        cell.setBackgroundColor(alternateRow ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(cell);
        
        document.add(summaryTable);
        
        document.close();
    }
}
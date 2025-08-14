package com.thogakade.dao;

import com.thogakade.database.DatabaseManager;
import com.thogakade.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class CustomerDAO {

    public boolean addCustomer(Customer customer) {
        String query = "INSERT INTO customers (id, title, name, dob, salary, address, city, province, postal_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customer.getId());
            pstmt.setString(2, customer.getTitle());
            pstmt.setString(3, customer.getName());
            pstmt.setDate(4, Date.valueOf(customer.getDob()));
            pstmt.setDouble(5, customer.getSalary());
            pstmt.setString(6, customer.getAddress());
            pstmt.setString(7, customer.getCity());
            pstmt.setString(8, customer.getProvince());
            pstmt.setString(9, customer.getPostalCode());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String query = "SELECT * FROM customers";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getDouble("salary"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("province"),
                        rs.getString("postal_code")
                );
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customers SET title=?, name=?, dob=?, salary=?, address=?, city=?, province=?, postal_code=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customer.getTitle());
            pstmt.setString(2, customer.getName());
            pstmt.setDate(3, Date.valueOf(customer.getDob()));
            pstmt.setDouble(4, customer.getSalary());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getCity());
            pstmt.setString(7, customer.getProvince());
            pstmt.setString(8, customer.getPostalCode());
            pstmt.setString(9, customer.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(String customerId) {
        String query = "DELETE FROM customers WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
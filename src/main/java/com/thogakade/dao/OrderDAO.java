package com.thogakade.dao;

import com.thogakade.database.DatabaseManager;
import com.thogakade.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class OrderDAO {

    public boolean addOrder(Order order) {
        String query = "INSERT INTO orders (order_id, order_date, customer_id, total) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, order.getOrderId());
            pstmt.setDate(2, Date.valueOf(order.getOrderDate()));
            pstmt.setString(3, order.getCustomerId());
            pstmt.setDouble(4, order.getTotal());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Order> getAllOrders() {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getString("order_id"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getString("customer_id"),
                        rs.getDouble("total")
                );
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public boolean updateOrder(Order order) {
        String query = "UPDATE orders SET order_date=?, customer_id=?, total=? WHERE order_id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(order.getOrderDate()));
            pstmt.setString(2, order.getCustomerId());
            pstmt.setDouble(3, order.getTotal());
            pstmt.setString(4, order.getOrderId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrder(String orderId) {
        String query = "DELETE FROM orders WHERE order_id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, orderId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Order getOrderById(String orderId) {
        String query = "SELECT * FROM orders WHERE order_id = ?";
        Order order = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                order = new Order(
                        rs.getString("order_id"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getString("customer_id"),
                        rs.getDouble("total")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }
}
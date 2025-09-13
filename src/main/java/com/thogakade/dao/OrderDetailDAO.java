package com.thogakade.dao;

import com.thogakade.database.DatabaseManager;
import com.thogakade.model.OrderDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class OrderDetailDAO {

    public boolean addOrderDetail(OrderDetail orderDetail) {
        String query = "INSERT INTO order_details (order_id, item_code, qty, unit_price, discount, total) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, orderDetail.getOrderId());
            pstmt.setString(2, orderDetail.getItemCode());
            pstmt.setInt(3, orderDetail.getQty());
            pstmt.setDouble(4, orderDetail.getUnitPrice());
            pstmt.setDouble(5, orderDetail.getDiscount());
            pstmt.setDouble(6, orderDetail.getTotal());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<OrderDetail> getOrderDetailsByOrderId(String orderId) {
        ObservableList<OrderDetail> orderDetails = FXCollections.observableArrayList();
        String query = "SELECT * FROM order_details WHERE order_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                        rs.getString("order_id"),
                        rs.getString("item_code"),
                        rs.getInt("qty"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("discount"),
                        rs.getDouble("total")
                );
                orderDetails.add(orderDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }

    public boolean updateOrderDetail(OrderDetail orderDetail) {
        String query = "UPDATE order_details SET qty=?, unit_price=?, discount=?, total=? WHERE order_id=? AND item_code=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderDetail.getQty());
            pstmt.setDouble(2, orderDetail.getUnitPrice());
            pstmt.setDouble(3, orderDetail.getDiscount());
            pstmt.setDouble(4, orderDetail.getTotal());
            pstmt.setString(5, orderDetail.getOrderId());
            pstmt.setString(6, orderDetail.getItemCode());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderDetail(String orderId, String itemCode) {
        String query = "DELETE FROM order_details WHERE order_id=? AND item_code=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, orderId);
            pstmt.setString(2, itemCode);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllOrderDetails(String orderId) {
        String query = "DELETE FROM order_details WHERE order_id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, orderId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
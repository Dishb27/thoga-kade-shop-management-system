package com.thogakade.dao;

import com.thogakade.database.DatabaseManager;
import com.thogakade.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ItemDAO {

    public boolean addItem(Item item) {
        String query = "INSERT INTO items (code, description, pack_size, unit_price, qty_on_hand) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, item.getCode());
            pstmt.setString(2, item.getDescription());
            pstmt.setString(3, item.getPackSize());
            pstmt.setDouble(4, item.getUnitPrice());
            pstmt.setInt(5, item.getQtyOnHand());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Item> getAllItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        String query = "SELECT * FROM items";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getString("pack_size"),
                        rs.getDouble("unit_price"),
                        rs.getInt("qty_on_hand")
                );
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public boolean updateItem(Item item) {
        String query = "UPDATE items SET description=?, pack_size=?, unit_price=?, qty_on_hand=? WHERE code=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, item.getDescription());
            pstmt.setString(2, item.getPackSize());
            pstmt.setDouble(3, item.getUnitPrice());
            pstmt.setInt(4, item.getQtyOnHand());
            pstmt.setString(5, item.getCode());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(String itemCode) {
        String query = "DELETE FROM items WHERE code=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, itemCode);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

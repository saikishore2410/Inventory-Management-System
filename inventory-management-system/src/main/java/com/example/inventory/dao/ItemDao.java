package com.example.inventory.dao;

import com.example.inventory.models.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    private Connection connection;

    public ItemDao(Connection connection) {
        this.connection = connection;
    }

    public void addItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (name, quantity, vendorId) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setInt(3, item.getVendorId());
            pstmt.executeUpdate();
        }
    }

    public void updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET name = ?, quantity = ?, vendorId = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getQuantity());
            pstmt.setInt(3, item.getVendorId());
            pstmt.setInt(4, item.getId());
            pstmt.executeUpdate();
        }
    }

    public Item getItem(int id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Item(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("vendorId"));
            }
        }
        return null;
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getInt("vendorId")));
            }
        }
        return items;
    }

    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
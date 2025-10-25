package com.example.inventory.dao;

import com.example.inventory.models.Vendor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorDao {
    private Connection connection;

    public VendorDao(Connection connection) {
        this.connection = connection;
    }

    public void addVendor(Vendor vendor) throws SQLException {
        String sql = "INSERT INTO vendors (name, contact_info) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, vendor.getName());
            pstmt.setString(2, vendor.getContactInfo());
            pstmt.executeUpdate();
        }
    }

    public void updateVendor(Vendor vendor) throws SQLException {
        String sql = "UPDATE vendors SET name = ?, contact_info = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, vendor.getName());
            pstmt.setString(2, vendor.getContactInfo());
            pstmt.setInt(3, vendor.getId());
            pstmt.executeUpdate();
        }
    }

    public Vendor getVendor(int id) throws SQLException {
        String sql = "SELECT * FROM vendors WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Vendor(rs.getInt("id"), rs.getString("name"), rs.getString("contact_info"));
            }
        }
        return null;
    }

    public List<Vendor> getAllVendors() throws SQLException {
        List<Vendor> vendors = new ArrayList<>();
        String sql = "SELECT * FROM vendors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vendors.add(new Vendor(rs.getInt("id"), rs.getString("name"), rs.getString("contact_info")));
            }
        }
        return vendors;
    }

    public void deleteVendor(int id) throws SQLException {
        String sql = "DELETE FROM vendors WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
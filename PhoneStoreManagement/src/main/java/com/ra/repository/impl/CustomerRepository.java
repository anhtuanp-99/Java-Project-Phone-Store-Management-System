package com.ra.repository.impl;

import com.ra.config.DBConnection;
import com.ra.model.Customer;
import com.ra.repository.ICustomerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {

    private Customer mapRow(ResultSet rs) throws SQLException{
        Customer c = new Customer();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        c.setRole(rs.getString("role"));
        c.setAddress(rs.getString("address"));
        return c;
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT id, name , phone, email, role, address FROM customer ORDER BY id";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                Customer c = mapRow(rs);
                list.add(c);
            }
        } catch (SQLException e){
            System.out.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Customer findById(int id) {
        String sql = "SELECT id, name, phone, email, role, address FROM customer WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e){
            System.out.println("Lỗi không tìm thấy khách hàng ID: " + id + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        String sql = "SELECT id, name, phone, email, role, address FROM customer WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e){
            System.out.println("Lỗi không tìm thấy khách hàng có email: " + email + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean save(Customer customer) {
        String sql = "INSERT INTO customer(name, phone, email, password, role, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPassword()); // được hash ở tầng Service
            stmt.setString(5, customer.getRole());
            stmt.setString(6, customer.getAddress());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            if (e.getSQLState().equals("23505")){
                System.out.println("Email đã tồn tại trong hệ thống!");
            } else {
                System.out.println("Lỗi khi thêm mới khách hàng: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE customer SET name=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3 , customer.getAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM customer WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            System.out.println("Lỗi khi xóa khách hàng:" + e.getMessage());
            return false;
        }
    }
}

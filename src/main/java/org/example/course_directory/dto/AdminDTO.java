package org.example.course_directory.dto;

import org.example.course_directory.entyty.Administrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDTO {
    private final Connection connection;

    public AdminDTO(Connection connection) {
        this.connection = connection;
    }

    // Метод для регистрации администратора
    public boolean registerAdmin(Administrator admin) {
        String sql = "INSERT INTO administrators (firstname, lastname, email, password, is_admin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getFirstname());
            stmt.setString(2, admin.getLastname());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPasswordHash());  // Пароль уже должен быть хеширован
            stmt.setBoolean(5, admin.isAdmin());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Проверка, существует ли email в таблице администраторов
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM administrators WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Получение администратора по email
    public Administrator getAdminByEmail(String email) {
        String sql = "SELECT * FROM administrators WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Administrator(
                            rs.getInt("id"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getBoolean("is_admin"),
                            rs.getTimestamp("last_login").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Если администратора нет
    }
}


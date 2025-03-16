package org.example.course_directory.dao;
import org.example.course_directory.entyty.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, password_hash) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPasswordHash());  // Пароль должен быть хеширован

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExists(String email) {
        return false;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Теперь извлекаем также и id
                    Integer id = rs.getInt("id");  // Предполагаем, что в таблице есть колонка id
                    if (rs.wasNull()) {  // Проверяем, если id действительно существует в записи
                        return null;  // Если id пусто или null, возвращаем null
                    }

                    // Создаем объект User с id, первым и последним именем, email, паролем и ролью
                    return new User(
                            id,  // Устанавливаем id
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getBoolean("is_admin")  // Если в БД есть колонка для админа
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Если пользователя нет
    }

}

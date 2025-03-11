package org.example.course_directory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.course_directory.connection.DatabaseConnection;
import org.example.course_directory.dto.UserDTO;
import org.example.course_directory.entyty.User;
import org.example.course_directory.services.NotificationService;
import org.example.course_directory.services.OpenNewWindow;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegController {

    private static final Logger logger = Logger.getLogger(RegController.class.getName());
    private NotificationService notificationService = new NotificationService();

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField mailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button regButton;

    @FXML
    public void initialize() {
        firstNameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                lastNameField.requestFocus();
            }
        });

        lastNameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                mailField.requestFocus();
            }
        });

        mailField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                regButton.requestFocus();
            }
        });

        createDefaultAdmins();
    }

    public void autoriz(ActionEvent event) {
        System.out.println("Переход в авторизацию");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        OpenNewWindow openNewWindow = new OpenNewWindow();
        openNewWindow.openNewWindow(currentStage, "/org/example/course_directory/fxml/authWindow.fxml", "Авторизация");
    }

    public void registr(ActionEvent event) {
        System.out.println("Попытка регистрации");
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = mailField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();
        boolean isAdmin = false;

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            notificationService.showNotification("Ошибка", "Заполните все поля!", "Для регистрации требуется заполнить все поля.");
            return;
        }

        if (!isValidEmail(email)) {
            notificationService.showNotification("Ошибка", "Некорректный email", "Введите корректный email-адрес.");
            return;
        }

        if (!isValidPassword(password)) {
            notificationService.showNotification("Ошибка", "Слабый пароль", "Пароль должен содержать минимум 8 символов, включая буквы, цифры и спецсимволы.");
            return;
        }

        try (Connection connection = new DatabaseConnection().connectToDatabase()) {
            UserDTO userDTO = new UserDTO(connection);
            if (userDTO.emailExists(email)) {
                notificationService.showNotification("Ошибка", "Email уже зарегистрирован", "Этот email уже используется.");
                return;
            }

            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            User newUser = new User(firstName, lastName, email, passwordHash, isAdmin);

            if (userDTO.registerUser(newUser)) {
                notificationService.showNotification("Успех", "Регистрация прошла успешно", "Теперь вы можете войти в систему.");
                autoriz(event);
            } else {
                notificationService.showNotification("Ошибка", "Не удалось зарегистрироваться", "Попробуйте позже.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при регистрации пользователя", e);
            notificationService.showNotification("Ошибка", "Ошибка базы данных", "Попробуйте позже.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }


    private boolean isValidPassword(String password) {
        String passwordRegex = "^.{5,}$";
        return password.matches(passwordRegex);
    }

    private void createDefaultAdmins() {
        try (Connection connection = new DatabaseConnection().connectToDatabase()) {
            if (adminExists(connection)) {
                return; // Если администраторы уже есть, не добавляем заново
            }

            String sql = "INSERT INTO administrators (firstname, lastname, email, password, is_admin) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                String[][] admins = {
                        {"Admin1", "One", "admin1@gmail.com", "admin1234"},
                        {"Admin2", "Two", "admin2@gmail.com", "admin1234"},
                        {"Admin3", "Three", "admin3@gmail.com", "admin1234"}
                };

                for (String[] admin : admins) {
                    stmt.setString(1, admin[0]); // Имя
                    stmt.setString(2, admin[1]); // Фамилия
                    stmt.setString(3, admin[2]); // Email
                    stmt.setString(4, BCrypt.hashpw(admin[3], BCrypt.gensalt(12))); // Хешированный пароль
                    stmt.setBoolean(5, true); // is_admin = true
                    stmt.executeUpdate();
                }

                logger.info("Администраторы успешно созданы.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при создании администраторов", e);
        }
    }

    private boolean adminExists(Connection connection) {
        String sql = "SELECT COUNT(*) FROM administrators";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // Если есть хотя бы один администратор, возвращаем true
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при проверке администраторов", e);
        }
        return false;
    }


}

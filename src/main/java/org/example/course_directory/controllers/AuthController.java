package org.example.course_directory.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.course_directory.connection.DatabaseConnection;
import org.example.course_directory.dto.AdminDTO;
import org.example.course_directory.dto.UserDTO;
import org.example.course_directory.entyty.Administrator;
import org.example.course_directory.entyty.User;
import org.example.course_directory.services.NotificationService;
import org.example.course_directory.services.OpenNewWindow;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthController {
    private NotificationService notificationService = new NotificationService();
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button autorisButton;

    @FXML
    private CheckBox adminBox;


    @FXML
    public void initialize() {
        emailField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                autorisButton.requestFocus();
            }
        });

        autorisButton.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                // Вызываем метод авторизации
            }
        });
// Запрещаем изменение размера окна авторизации
        Platform.runLater(() -> {
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.fullScreenProperty().addListener((obs, wasFullScreen, isFullScreen) -> {
                if (isFullScreen) {
                    stage.setFullScreen(false);
                }
            });
        });

    }

    // Метод для авторизации
    public void autoris(ActionEvent event) {
        System.out.println("Попытка авторизации");

        String email = emailField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            notificationService.showNotification("Ошибка", "Заполните все поля!", "Для авторизации требуется заполнить все поля.");
            return;
        }

        boolean isAdmin = adminBox.isSelected(); // Проверяем, отмечен ли чекбокс для администратора
        String adminName = ""; // Инициализируем переменную adminName, чтобы она была доступна в любом блоке

        try (Connection connection = new DatabaseConnection().connectToDatabase()) {

            if (isAdmin) {
                // Ищем администратора
                AdminDTO adminDTO = new AdminDTO(connection);
                Administrator admin = adminDTO.getAdminByEmail(email);
                if (admin == null) {
                    notificationService.showNotification("Ошибка", "Администратор не найден", "Проверьте введенные данные и попробуйте снова.");
                    return;
                }

                // Проверяем пароль с хранимым хешем
                if (!BCrypt.checkpw(password, admin.getPassword())) {
                    notificationService.showNotification("Ошибка", "Неверный пароль", "Пожалуйста, проверьте пароль и попробуйте снова.");
                    return;
                }

                System.out.println("Вход как администратор");
                adminName = admin.getFirstName(); // Получаем имя администратора

                // Передаем имя администратора в метод открытия окна
                openAdminWindow(event, adminName);
            } else {
                // Ищем пользователя
                UserDTO userDAO = new UserDTO(connection);
                User user = userDAO.getUserByEmail(email);

                if (user == null) {
                    notificationService.showNotification("Ошибка", "Пользователь не найден", "Проверьте введенные данные и попробуйте снова.");
                    return;
                }

                // Проверяем пароль с хранимым хешем
                if (!BCrypt.checkpw(password, user.getPasswordHash())) {
                    notificationService.showNotification("Ошибка", "Неверный пароль", "Пожалуйста, проверьте пароль и попробуйте снова.");
                    return;
                }

                boolean isUserAdmin = user.isAdmin();
                if (isUserAdmin && adminBox.isSelected()) {
                    System.out.println("Вход как администратор");
                    openAdminWindow(event, adminName); // Здесь вам нужно передать имя администратора
                } else {
                    System.out.println("Вход как пользователь");
                    openUserWindow(event);
                    // Отображаем имя пользователя в метке
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при авторизации", e);
            notificationService.showNotification("Ошибка", "Ошибка базы данных", "Попробуйте позже.");
        }
    }

    // Метод для открытия окна администратора
    private void openAdminWindow(ActionEvent event, String adminName) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        OpenNewWindow openNewWindow = new OpenNewWindow();
        // Открываем новое окно и получаем ссылку на его контроллер
        AdminHomeController adminHomeController = openNewWindow.openNewWindowWithController(
                currentStage,
                "/org/example/course_directory/fxml/admin/adminHome.fxml",
                "Админ-панель"
        );

        // Устанавливаем имя администратора в метку через контроллер
        adminHomeController.setAdminLabel(adminName); // Здесь передаем имя администратора
    }


    // Метод для открытия окна пользователя
    private void openUserWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        OpenNewWindow openNewWindow = new OpenNewWindow();
        openNewWindow.openNewWindow(currentStage, "/org/example/course_directory/fxml/user/userHome.fxml", "Пользователь");
    }


    // Метод для перехода в окно регистрации
    public void registr(ActionEvent event) {
        System.out.println("Переход в регистрацию");

        // Получаем текущее окно (Stage)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Создаем объект OpenNewWindow и открываем новое окно
        OpenNewWindow openNewWindow = new OpenNewWindow();
        openNewWindow.openNewWindow(currentStage, "/org/example/course_directory/fxml/registration.fxml", "Регистрация");

    }
}

package org.example.course_directory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.course_directory.services.NotificationService;
import org.example.course_directory.services.OpenNewWindow;

public class StartWindowController {
    private NotificationService notificationService = new NotificationService();

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button autorisButton;

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
    }

    // Метод для авторизации (пока без реализации)
    public void autoris(ActionEvent actionEvent) {
        System.out.println("Попытка авторизации");
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty() ) {
            System.out.println("Заполните все поля!");

            notificationService.showNotification("Ошибка", "Заполните все поля!", "Для авторизации требуется заполнить все поля.");

        }
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

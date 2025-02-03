package org.example.course_directory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.course_directory.connection.DatabaseConnection;
import org.example.course_directory.dao.UserDAO;
import org.example.course_directory.entyty.User;
import org.example.course_directory.services.NotificationService;
import org.example.course_directory.services.OpenNewWindow;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;

public class RegController {

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
    public void initialize(){
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

        regButton.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                // Вызываем метод регистрации
            }
        });
    }

    public void autoriz(javafx.event.ActionEvent event){
        System.out.println("Переход в регистрацию");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        OpenNewWindow openNewWindow = new OpenNewWindow();
        openNewWindow.openNewWindow(currentStage, "/org/example/course_directory/fxml/startWindow.fxml", "Авторизация");
    }
    public void registr(ActionEvent event) {
        System.out.println("Попытка регистрации");
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = mailField.getText();
        String password = passwordField.getText();  // Нужно хешировать

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Заполните все поля!");

            notificationService.showNotification("Ошибка", "Заполните все поля!", "Для регистрации требуется заполнить все поля.");


        }

        // Хеширование пароля перед сохранением
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        // Создаем объект пользователя
        User newUser = new User(firstName, lastName, email, passwordHash);

        // Сохраняем в БД через DAO
        try (Connection connection = new DatabaseConnection().connectToDatabase()) {
            UserDAO userDAO = new UserDAO(connection);
            if (userDAO.registerUser(newUser)) {
                System.out.println("Регистрация успешна!");
                notificationService.showNotification("Успех", "Успешная регистрация!", "Вы успешно зарегистрировались, можете входить!");
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                OpenNewWindow openNewWindow = new OpenNewWindow();
                openNewWindow.openNewWindow(currentStage, "/org/example/course_directory/fxml/startWindow.fxml", "Авторизация");

            } else {
                System.out.println("Ошибка при регистрации.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

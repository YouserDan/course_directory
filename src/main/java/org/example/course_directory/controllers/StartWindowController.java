package org.example.course_directory.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import javafx.scene.Node;

import java.io.IOException;

public class StartWindowController {

    @FXML
    private TextField firstNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button autorisButton;

    @FXML
    public void initialize() {
        firstNameField.setOnKeyPressed(event -> {
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


    public void autoris(ActionEvent actionEvent) {
        System.out.println("Попытка авторизации");
    }
    public void registr(javafx.event.ActionEvent event){
        System.out.println("Переход в регистрацию");
        try {
            // Загрузка нового окна
            FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/registration.fxml"));
            Parent root = loader.load();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Регистрация");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);

            // Закрываем текущее окно
            // Получаем текущий Stage из события
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить окно регистрации");
        }
    }
}

package org.example.course_directory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;

import java.io.IOException;

public class RegController {

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
                // Вызываем метод авторизации
            }
        });
    }

    public void autoriz(javafx.event.ActionEvent event){
        System.out.println("Переход в регистрацию");
        try {
            // Загрузка нового окна
            FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/startWindow.fxml"));
            Parent root = loader.load();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Авторизация");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);

            // Закрываем текущее окно
            // Получаем текущий Stage из события
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить окно входа");
        }
    }
    public void registr(ActionEvent actionEvent) {
        System.out.println("Попытка регистрации");
    }


}

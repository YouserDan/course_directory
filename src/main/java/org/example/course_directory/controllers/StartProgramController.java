package org.example.course_directory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartProgramController {
    @FXML
    Button switchButton;

    @FXML
    protected void handleSwitchWindow() throws IOException {
        // Загружаем новое окно
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/course_directory/adminMenu.fxml"));

        Scene newScene = new Scene(fxmlLoader.load());

        // Получаем текущую сцену
        Stage currentStage = (Stage) switchButton.getScene().getWindow();

        currentStage.show();
        // Создаем новое окно (Stage)
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Админ Меню");


        newStage.centerOnScreen();

        // Показываем новое окно
        newStage.show();

        // закрываем текущее окно
        currentStage.close();
    }

    @FXML
    public void handleExit(ActionEvent actionEvent) {
        // Закрыть приложение
        System.out.println("vmdvmkdmk");
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow(); // Получаем текущее окно
        stage.close(); // Закрыть текущее окно
    }
}

package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartProgrammController {
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
    }
}

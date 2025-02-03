package org.example.course_directory.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OpenNewWindow {

    // Метод теперь принимает текущий Stage (окно), которое нужно закрыть
    public void openNewWindow(Stage currentStage, String fxmlFile, String title) {
        try {
            // Загружаем новый FXML файл
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Создаем новое окно
            Stage newStage = new Stage();
            newStage.setTitle(title);
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.centerOnScreen();
            newStage.show();

            // Закрываем текущее окно (предыдущее)
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить окно.");
        }
    }
}

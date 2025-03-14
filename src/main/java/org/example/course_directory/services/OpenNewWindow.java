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
            IconManager.applyIcon(newStage);
            newStage.setTitle(title);
            newStage.setScene(new Scene(root));
            newStage.centerOnScreen();
            newStage.show();
            // Закрываем текущее окно (предыдущее)
            currentStage.close();

            // Запрещаем изменение размера окна
            newStage.setResizable(false);

            // Принудительно блокируем полноэкранный режим
            newStage.setFullScreen(false);

            // Добавляем слушатель на изменение полноэкранного режима
            newStage.fullScreenProperty().addListener((obs, wasFullScreen, isFullScreen) -> {
                if (isFullScreen) {
                    newStage.setFullScreen(false); // Принудительно блокируем полноэкранный режим
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить окно.");
        }
    }

    public <T> T openNewWindowWithController(Stage currentStage, String fxmlPath, String title) {
        try {
            // Загружаем FXML файл
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Получаем контроллер из загруженного FXML
            T controller = loader.getController();

            // Создаем новую сцену
            Scene scene = new Scene(root);

            // Устанавливаем сцену в новое окно
            Stage stage = new Stage();
            IconManager.applyIcon(stage);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setFullScreen(false);
            stage.show();
            // Закрываем текущее окно (предыдущее)
            currentStage.close();

            return controller; // Возвращаем контроллер
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

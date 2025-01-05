package org.example.course_directory.controllers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import javafx.scene.Node;

import java.io.IOException;

public class UserHomeController {

    @FXML
    private SplitPane splitPane;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Проверяем и настраиваем разделители после полной загрузки UI
//            System.out.println("Найдено разделителей: " + splitPane.lookupAll(".split-pane-divider").size());

            splitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setMouseTransparent(true); // Отключаем взаимодействие с мышью
            });

            // Фиксируем положение разделителя
            splitPane.setDividerPositions(0.3);
        });
    }



    public void backToMenu(javafx.event.ActionEvent event) {
        try {
            // Загрузка нового окна
            FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/startWindow.fxml"));
            Parent root = loader.load();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Авторизация");
            stage.setScene(new Scene(root));
            stage.show();

            // Получаем текущий Stage из события
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить окно просмотра курсов");
        }
    }

    public void viewCourses(javafx.event.ActionEvent event) {
        try {
            // Загрузка нового окна
            FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/user/userCourses.fxml"));
            Parent root = loader.load();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Каталог курсов");
            stage.setScene(new Scene(root));
            stage.show();

            // Получаем текущий Stage из события
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить окно просмотра курсов");
        }

    }
}


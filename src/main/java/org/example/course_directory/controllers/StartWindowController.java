package org.example.course_directory.controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import javafx.scene.Node;
import javafx.stage.Window;

import java.io.IOException;

public class StartWindowController {
    public void adminAutoris(){
        System.out.println("Попытка авторизации");
    }
    public void regAdmin(){
        System.out.println("Попытка регистрации админа");
    }

    public void viewCourses(javafx.event.ActionEvent event) {
        try {
            // Загрузка нового окна
            FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/userMenu.fxml"));
            Parent root = loader.load();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Просмотр курсов");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);

            // Закрываем текущее окно
            // Получаем текущий Stage из события
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить окно просмотра курсов");
        }
    }


}

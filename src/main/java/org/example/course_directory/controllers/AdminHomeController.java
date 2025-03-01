package org.example.course_directory.controllers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import javafx.scene.Node;
import org.example.course_directory.services.NotificationService;

import java.io.File;
import java.io.IOException;

public class AdminHomeController {
    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane courseCatalog;

    @FXML
    private AnchorPane helpPage;

    @FXML
    private AnchorPane homePage;

    @FXML
    private AnchorPane courseEditor;

    @FXML
    private AnchorPane addCourse;

    @FXML
    private ImageView imageView;


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

            courseCatalog.setVisible(false);
            helpPage.setVisible(false);
            homePage.setVisible(true);
            courseEditor.setVisible(false);
            addCourse.setVisible(false);
        });
    }



    public void backToMenu(javafx.event.ActionEvent event) {
        NotificationService notificationService = new NotificationService();
        boolean confirmExit = notificationService.showConfirmationDialog("Подтверждение", "Вы уверены, что хотите выйти в главное меню?");

        if (confirmExit) {
            try {
                FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/authWindow.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Авторизация");
                stage.setScene(new Scene(root));
                stage.show();

                // Закрываем текущее окно
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Не удалось загрузить окно авторизации");
            }
        }
    }

    public void viewCourses(javafx.event.ActionEvent event) {
        helpPage.setVisible(false);
        courseCatalog.setVisible(true);
        homePage.setVisible(false);
        courseEditor.setVisible(false);
        addCourse.setVisible(false);
    }

    public void openHelpPage(ActionEvent event) {
        helpPage.setVisible(true);
        courseCatalog.setVisible(false);
        homePage.setVisible(false);
        courseEditor.setVisible(false);
        addCourse.setVisible(false);
    }

    public void openHomePage(ActionEvent event) {
        homePage.setVisible(true);
        helpPage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        addCourse.setVisible(false);
    }

    public void editCourse(ActionEvent event){
        homePage.setVisible(false);
        helpPage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(true);
        addCourse.setVisible(false);
    }

    public void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");

        // Фильтр для изображений
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Получаем текущее окно (Stage)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Открываем диалоговое окно
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("Выбран файл: " + file.getAbsolutePath());

            // Создаём Image из выбранного файла
            Image image = new Image(file.toURI().toString());

            // Отображаем в ImageView
            imageView.setImage(image);
        }
    }


    public void openAddPage(ActionEvent event) {
        addCourse.setVisible(true);
        homePage.setVisible(false);
        helpPage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
    }
}


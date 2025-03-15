package org.example.course_directory.controllers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import javafx.scene.Node;
import org.example.course_directory.cardMaker.CourseLoader;
import org.example.course_directory.services.IconManager;
import org.example.course_directory.services.NotificationService;

import java.io.IOException;
import java.sql.SQLException;

public class UserHomeController {

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane courseCatalog;

    @FXML
    private AnchorPane helpPage;

    @FXML
    private AnchorPane homePage;

    @FXML
    private Label userNameLabel;
    public void setUserLabel(String name) {
        userNameLabel.setText(name); // Устанавливаем имя в метку
    }

    //Для карточек курса
    @FXML
    private FlowPane courseFlowPane; // Это поле должно быть связано с FXML
    private CourseLoader courseLoader;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {

            splitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setMouseTransparent(true); // Отключаем взаимодействие с мышью
            });

            // Фиксируем положение разделителя
            splitPane.setDividerPositions(0.3);
            homePage.setVisible(true);
            courseCatalog.setVisible(false);
            helpPage.setVisible(false);


            // Инициализация courseLoader, передаем courseFlowPane
            courseLoader = new CourseLoader(courseFlowPane);
        });
    }

    public void viewCourses(ActionEvent actionEvent) {
        if (courseLoader == null) {
            System.out.println("Ошибка: CourseLoader не инициализирован!");
            return;
        }

        Platform.runLater(() -> {
            courseLoader.loadCourses();
            homePage.setVisible(false);
            courseCatalog.setVisible(true);
            helpPage.setVisible(false);
        });
    }



    public void openHelpPage(ActionEvent event) {
        helpPage.setVisible(true);
        courseCatalog.setVisible(false);
        homePage.setVisible(false);
    }

    public void openHomePage(ActionEvent event) {
        homePage.setVisible(true);
        helpPage.setVisible(false);
        courseCatalog.setVisible(false);
    }


    public void backToMenu(javafx.event.ActionEvent event) {
        NotificationService notificationService = new NotificationService();
        boolean confirmExit = notificationService.showConfirmationDialog("Подтверждение", "Вы уверены, что хотите выйти в главное меню?");

        if (confirmExit) {
            try {
                FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/authWindow.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                IconManager.applyIcon(stage);
                stage.setTitle("Авторизация");
                stage.setScene(new Scene(root));
                stage.setFullScreen(false);
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



}


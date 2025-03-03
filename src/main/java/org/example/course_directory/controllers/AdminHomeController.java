package org.example.course_directory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import org.example.course_directory.dao.CourseDAO;
import org.example.course_directory.entyty.Course;
import org.example.course_directory.services.NotificationService;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AdminHomeController {

    @FXML
    private AnchorPane homePage;
    @FXML
    private AnchorPane courseCatalog;
    @FXML
    private AnchorPane courseEditor;
    @FXML
    private AnchorPane helpPage;
    @FXML
    private AnchorPane addCourse;




    // Поля, которые будут связаны с элементами формы
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField programmingLanguageField;
    @FXML private TextField imageUrlField;
    @FXML private TextField levelField;
    @FXML private TextField durationField;
    @FXML private TextField accessField;
    @FXML private TextField priceField;
    @FXML private TextField keywordsField;
    @FXML private TextArea descriptionField;
    @FXML private TextField languageOfCourseField;
    @FXML private TextField resourceUrlField;
    @FXML private TextField createdByField;

    @FXML
    private ImageView imageView;


    @FXML
    private Button saveCourseButton;

    public void initialize(){
        homePage.setVisible(true);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);

    }

    // Метод для сохранения курса в базу данных
    @FXML
    public void saveCourse() {
        try {
            // Считываем данные из формы
            String title = titleField.getText();
            String author = authorField.getText();
            String programmingLanguage = programmingLanguageField.getText();
            String imageUrl = imageUrlField.getText();
            String level = levelField.getText();
            String duration = durationField.getText();
            String access = accessField.getText();
            double price = Double.parseDouble(priceField.getText());
            String keywords = keywordsField.getText();
            String description = descriptionField.getText();
            String languageOfCourse = languageOfCourseField.getText();
            String resourceUrl = resourceUrlField.getText();
            String createdBy = createdByField.getText();

            // Создаем объект Course с введенными данными
            Course newCourse = new Course(
                    title,
                    author,
                    programmingLanguage,
                    imageUrl,
                    level,
                    duration,
                    access,
                    price,
                    keywords,
                    description,
                    languageOfCourse,
                    resourceUrl,
                    createdBy
            );

            // Устанавливаем время создания и обновления
            newCourse.setCreatedAt(java.time.LocalDateTime.now());
            newCourse.setUpdatedAt(java.time.LocalDateTime.now());

            // Добавляем курс в базу данных через CourseDAO
            CourseDAO courseDAO = new CourseDAO();
            courseDAO.addCourse(newCourse);

            // Уведомляем пользователя о том, что курс был успешно добавлен
            showAlert("Успех", "Курс был успешно добавлен в базу данных.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при добавлении курса в базу данных.");
        } catch (NumberFormatException e) {
            showAlert("Ошибка ввода", "Пожалуйста, убедитесь, что цена введена корректно.");
        }
    }

    // Метод для отображения сообщений об успехе или ошибке
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void viewCourses(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(true);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
    }

    public void editCourse(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(true);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
    }

    public void openHelpPage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(true);
        addCourse.setVisible(false);
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

    public void openHomePage(ActionEvent actionEvent) {
        homePage.setVisible(true);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
    }

    public void openAddPage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(true);
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
}

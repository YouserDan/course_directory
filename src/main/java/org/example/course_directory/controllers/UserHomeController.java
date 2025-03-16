package org.example.course_directory.controllers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import javafx.scene.Node;
import org.example.course_directory.cardMaker.CourseLoader;
import org.example.course_directory.entyty.Course;
import org.example.course_directory.services.IconManager;
import org.example.course_directory.services.NotificationService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class UserHomeController {
    //Для заполнения панели о курса
    @FXML private Label courseAboutTitleLabel;
    @FXML private Label courseAboutAutorLabel;
    @FXML private Label courseAboutLevelLabel;
    @FXML private Label courseAboutProgLangLabel;
    @FXML private Label courseAboutDurationLabel;
    @FXML private Label courseAboutDurationTypeLabel;
    @FXML private Label courseAboutAccessLabel;
    @FXML private Label courseAboutPriceLabel;
    @FXML private Label courseAboutCurrencyLabel;
    @FXML private Label courseAboutCourseLanguageLabel;
    @FXML private ImageView courseAboutImageView;
    @FXML private Text courseAboutDescriptionText;

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane courseCatalog;

    @FXML
    private AnchorPane helpPage;

    @FXML
    private AnchorPane homePage;

    @FXML
    private AnchorPane aboutCoursePage;

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
            aboutCoursePage.setVisible(false);

            Platform.runLater(() -> {
                Stage stage = (Stage) homePage.getScene().getWindow();
                stage.setResizable(false);
                stage.setFullScreen(false);
                stage.fullScreenProperty().addListener((obs, wasFullScreen, isFullScreen) -> {
                    if (isFullScreen) {
                        stage.setFullScreen(false);
                    }
                });
            });


            // Инициализация courseLoader, передаем courseFlowPane
            courseLoader = new CourseLoader(courseFlowPane);

            courseLoader = new CourseLoader(courseFlowPane);
            courseLoader.setUserHomeController(this); // Передаем текущий контроллер
            courseLoader.loadCourses(); // Только после установки контроллера
        });
    }

    @FXML
    private void backToCatalog(ActionEvent event) {
        aboutCoursePage.setVisible(false); // Скрываем страницу о курсе
        courseCatalog.setVisible(true); // Показываем каталог курсов
    }


    @FXML
    private void openCourseResourse(ActionEvent event) {
        if (currentCourse != null) {
            String url = currentCourse.getResourceUrl();
            System.out.println("Попытка открыть ссылку: " + url); // Выводим текущий URL

            if (url == null || url.isEmpty()) {
                System.out.println("❌ Ошибка: Ссылка на ресурс отсутствует!");
            } else {
                try {
                    Desktop.getDesktop().browse(new URI(url)); // Открываем в браузере
                } catch (IOException | URISyntaxException e) {
                    System.out.println("Ошибка при открытии ссылки: " + e.getMessage());
                }
            }
        } else {
            System.out.println("❌ Ошибка: currentCourse не инициализирован.");
        }
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
            aboutCoursePage.setVisible(false);
        });
    }

    public void openHelpPage(ActionEvent event) {
        helpPage.setVisible(true);
        courseCatalog.setVisible(false);
        homePage.setVisible(false);
        aboutCoursePage.setVisible(false);
    }

    public void openHomePage(ActionEvent event) {
        homePage.setVisible(true);
        helpPage.setVisible(false);
        courseCatalog.setVisible(false);
        aboutCoursePage.setVisible(false);
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

    private Course currentCourse; // Это будет хранить текущий выбранный курс
    public void showAboutCoursePage(Course course) {
        if (course == null) return;

        // Сохраняем курс в переменную currentCourse
        currentCourse = course;

        // Заполняем данные в панели
        courseAboutTitleLabel.setText(course.getTitle());
        courseAboutAutorLabel.setText(course.getAuthor());
        courseAboutLevelLabel.setText(course.getLevel());
        courseAboutProgLangLabel.setText(course.getProgrammingLanguage());
        courseAboutDurationLabel.setText(String.valueOf(course.getDuration()));
        courseAboutDurationTypeLabel.setText(course.getDurationType());
        courseAboutAccessLabel.setText(course.getAccess());
        courseAboutPriceLabel.setText(String.valueOf(course.getPrice()));
        courseAboutCurrencyLabel.setText(course.getCurrency());
        courseAboutCourseLanguageLabel.setText(course.getCourseLanguage());
        courseAboutDescriptionText.setText(course.getDescription());

        // Загружаем картинку курса
        if (course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
            Image image = new Image(course.getImageUrl(), true);
            courseAboutImageView.setImage(image);
        }

        // Делаем панель видимой
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        helpPage.setVisible(false);
        aboutCoursePage.setVisible(true);
    }

}


package org.example.course_directory.controllers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
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
import org.example.course_directory.connection.DatabaseConnection;
import org.example.course_directory.dao.ClickDAO;
import org.example.course_directory.entyty.Course;
import org.example.course_directory.services.IconManager;
import org.example.course_directory.services.NotificationService;
import org.example.course_directory.services.ClickService;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class UserHomeController {

    private ClickService clickService;
    private int userId; // ID текущего пользователя
    private Course currentCourse; // Текущий выбранный курс

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

    //Сортировка
    @FXML private SplitMenuButton sortMenu;

    //Поиск
    @FXML
    private TextField search; // Поле поиска


    @FXML
    public void initialize() {

        Platform.runLater(() -> {

            splitPane.lookupAll(".split-pane-divider").forEach(divider -> {
                divider.setMouseTransparent(true); // Отключаем взаимодействие с мышью
            });

            sortMenu.getItems().get(0).setOnAction(event -> sortCoursesByPopularity());

            Platform.runLater(() -> {
                sortMenu.getItems().get(1).setOnAction(event -> sortCoursesByDateAdded());  // Добавляем обработчик для сортировки по дате
                // Остальной код...
            });

            search.textProperty().addListener((observable, oldValue, newValue) -> {
                filterCourses(newValue);
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

    private void filterCourses(String searchText) {
        List<Course> filteredCourses = new ArrayList<>();

        if (searchText == null || searchText.isEmpty()) {
            courseFlowPane.getChildren().clear();
            for (Course course : courseLoader.getCourses()) {
                courseLoader.addCourseCard(course);
            }
            return;
        }

        String lowerCaseSearch = searchText.toLowerCase();

        // Фильтрация с проверкой на null
        for (Course course : courseLoader.getCourses()) {
            String title = course.getTitle() != null ? course.getTitle().toLowerCase() : "";
            String programmingLanguage = course.getProgrammingLanguage() != null ? course.getProgrammingLanguage().toLowerCase() : "";
            String keywords = course.getKeywords() != null ? course.getKeywords().toLowerCase() : "";

            if (title.contains(lowerCaseSearch) ||
                    programmingLanguage.contains(lowerCaseSearch) ||
                    keywords.contains(lowerCaseSearch)) {
                filteredCourses.add(course);
            }
        }

        // Обновляем FlowPane
        courseFlowPane.getChildren().clear();
        for (Course course : filteredCourses) {
            courseLoader.addCourseCard(course);
        }
    }




    public void sortCoursesByDateAdded() {
        try {
            // Загружаем курсы, отсортированные по дате добавления
            List<Course> sortedCourses = courseLoader.getCourses();
            sortedCourses.sort((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()));  // Сортировка вручную, если нужно
            courseFlowPane.getChildren().clear();  // Очищаем старые карточки
            for (Course course : sortedCourses) {
                courseLoader.addCourseCard(course);  // Добавляем отсортированные курсы в FlowPane
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortCoursesByPopularity() {
        try {
            Map<Integer, Integer> popularityMap = clickService.getCoursesPopularity();

            // Загружаем все курсы (или бери из текущего courseLoader'а, если хранятся в списке)
            List<Course> allCourses = courseLoader.getCourses(); // Сделай метод getCourses() в CourseLoader, чтобы возвращал список

            // Сортируем: сначала по популярности, потом остальные
            List<Course> sortedCourses = allCourses.stream()
                    .sorted(Comparator.comparingInt((Course c) -> popularityMap.getOrDefault(c.getId(), 0))
                            .reversed())
                    .collect(Collectors.toList());

            // Очищаем FlowPane и заново рендерим отсортированные карточки
            courseFlowPane.getChildren().clear();
            for (Course course : sortedCourses) {
                courseLoader.addCourseCard(course); // Сделай метод addCourseCard(), который рендерит одну карточку
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    // Фиксируем переход
                    clickService.recordTransition(userId, currentCourse.getId());

                    Desktop.getDesktop().browse(new URI(url)); // Открываем в браузере
                } catch (IOException | URISyntaxException e) {
                    System.out.println("Ошибка при открытии ссылки: " + e.getMessage());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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


    public UserHomeController() {
        // Инициализируем ClickService
        this.clickService = new ClickService(new ClickDAO(new DatabaseConnection().connectToDatabase()));
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void showAboutCoursePage(Course course) {
        if (course == null) return;

        // Сохраняем курс в переменную currentCourse
        currentCourse = course;

        System.out.println(userId);
        // Проверка userId перед записью клика
        if (userId <= 0) {
            System.out.println("❌ Ошибка: userId не установлен! (значение: " + userId + ")");
            return;
        }

        // Регистрируем клик по карточке курса
        try {
            clickService.recordClick(userId, course.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при записи клика по курсу: " + e.getMessage());
        }

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


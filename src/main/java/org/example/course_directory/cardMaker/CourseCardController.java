package org.example.course_directory.cardMaker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.course_directory.entyty.Course;

public class CourseCardController {
    @FXML
    private VBox courseCard; // Основной контейнер карточки
    @FXML
    private ImageView courseImage; // Картинка курса
    @FXML
    private Label courseTitle; // Название курса
    @FXML
    private Label progLang; // Язык программирования
    @FXML
    private Label courseAccess; // Доступ (платный/бесплатный)
    @FXML
    private Button openCourseButton; // Кнопка "Посмотреть"

    private Course course; // Данные курса

    // Устанавливаем данные курса в карточку
    public void setCourseData(Course course) {
        this.course = course;

        // Устанавливаем текстовые данные
        courseTitle.setText(course.getTitle());
        progLang.setText(course.getProgrammingLanguage());
        courseAccess.setText(course.getAccess()); // Например, "Бесплатный" или "Платный"

        // Загружаем картинку, если есть
        if (course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
            try {
                courseImage.setImage(new Image(course.getImageUrl()));
            } catch (Exception e) {
                System.out.println("Ошибка загрузки изображения");
            }
        }

//        // Назначаем действие для кнопки "Посмотреть"
//        openCourseButton.setOnAction(event -> openCourseLink());
    }

//    // Открытие ссылки на курс в браузере
//    private void openCourseLink() {
//        if (course.getResourceUrl() != null && !course.getResourceUrl().isEmpty()) {
//            try {
//                Desktop.getDesktop().browse(new URI(course.getResourceUrl()));
//            } catch (IOException | URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

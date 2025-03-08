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
    private ImageView courseImageView; // Картинка курса
    @FXML
    private Label courseTitle; // Название курса
    @FXML
    private Label progLang; // Язык программирования
    @FXML
    private Label courseAccess; // Доступ (платный/бесплатный)
    @FXML
    private Button openCourseButton; // Кнопка "Посмотреть"

    // Устанавливаем данные курса в карточку
    public void setCourse(Course course) {
        // Устанавливаем текстовые данные
        System.out.println("Устанавливаем курс: " + course.getTitle()); // Отладочная информация

        courseTitle.setText(course.getTitle());
        progLang.setText(course.getProgrammingLanguage());
        courseAccess.setText(course.getAccess());

        // Устанавливаем картинку для курса
        if (course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
            Image image = new Image(course.getImageUrl());
            courseImageView.setImage(image);
        } else {
            // Если URL пустой, можно задать дефолтное изображение
            Image defaultImage = new Image("default_image_path_here"); // Укажите путь к изображению по умолчанию
            courseImageView.setImage(defaultImage);
        }
    }


}

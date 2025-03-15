package org.example.course_directory.cardMaker;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.course_directory.controllers.AdminHomeController;
import org.example.course_directory.entyty.Course;


import java.awt.*;
import java.net.URI;

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
    private Course currentCourse; // Хранит последний установленный курс


    public void setCourse(Course course) {
        // Проверяем, изменились ли данные курса
        if (currentCourse != null && currentCourse.equals(course)) {
            System.out.println("Данные курса не изменились, пропускаем установку.");
            return;
        }

        System.out.println("Обновляем курс: " + course.getTitle()); // Отладочная информация

        // Устанавливаем текстовые данные
        courseTitle.setText(course.getTitle());
        progLang.setText(course.getProgrammingLanguage());
        courseAccess.setText(course.getAccess());

        // Загружаем картинку только если URL изменился
        if (course.getImageUrl() != null && !course.getImageUrl().equals(currentCourse == null ? null : currentCourse.getImageUrl())) {
            Image image = new Image(course.getImageUrl(), true);
            courseImageView.setImage(image);
        }

        // Сохраняем текущий курс
        currentCourse = course;
    }

    // Метод, который вызывается при нажатии на кнопку
    @FXML
    private void openAboutCoursePage() {
        if (adminHomeController != null && currentCourse != null) {
            // Передаем курс в AdminHomeController
            adminHomeController.showAboutCoursePage(currentCourse);
        } else {
            System.out.println("Ошибка: AdminHomeController не установлен или курс отсутствует.");
        }
    }


    //Для передачи данных курса в админХомКонтроллер
    private AdminHomeController adminHomeController;

    public void setAdminHomeController(AdminHomeController adminHomeController) {
        this.adminHomeController = adminHomeController;
        if (adminHomeController == null) {
            System.out.println("Ошибка: передан NULL в CourseCardController.setAdminHomeController!");
        } else {
            System.out.println("AdminHomeController успешно передан в CourseCardController.");
        }
    }



}

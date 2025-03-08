package org.example.course_directory.cardMaker;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.course_directory.dao.CourseDAO;
import org.example.course_directory.entyty.Course;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CourseLoader {
    private final FlowPane flowPane;  // Контейнер, куда добавляются карточки курсов
    private final CourseDAO courseDAO;  // Работа с базой данных

    public CourseLoader(FlowPane flowPane) {
        this.flowPane = flowPane;
        this.courseDAO = new CourseDAO();
    }

    // Метод для загрузки всех курсов
    public void loadCourses() {
        List<Course> courses = null;
        try {
            courses = courseDAO.getAllCourses();
            System.out.println("Курсы загружены: " + courses.size());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка загрузки курсов!");
            return;
        }

        if (flowPane == null) {
            System.out.println("Ошибка: FlowPane не инициализирован!");
            return;
        }

        // Очищаем FlowPane перед добавлением новых элементов
        flowPane.getChildren().clear();

        // Загружаем все курсы и добавляем их в FlowPane
        for (Course course : courses) {
            addCourseToFlowPane(course);
        }

        System.out.println("Курсы добавлены");
        System.out.println("Количество элементов в FlowPane: " + flowPane.getChildren().size());
    }


    // Метод для добавления одной карточки в FlowPane
    private void addCourseToFlowPane(Course course) {
        try {
            // Загрузка FXML файла для карточки курса
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/course_directory/fxml/cardMaker/CourseCard.fxml"));
            Parent courseCard = loader.load();

            // Получение контроллера и передача данных
            CourseCardController cardController = loader.getController();
            cardController.setCourse(course);

            // Добавление карточки в FlowPane
            flowPane.getChildren().add(courseCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

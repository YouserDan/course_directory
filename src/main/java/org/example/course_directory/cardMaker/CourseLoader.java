package org.example.course_directory.cardMaker;


import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.course_directory.cardMaker.CourseCardController;
import org.example.course_directory.dao.CourseDAO;
import org.example.course_directory.entyty.Course;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CourseLoader {
    private FlowPane flowPane;  // Контейнер, куда добавляются карточки курсов
    private CourseDAO courseDAO;  // Работа с базой данных

    public CourseLoader(FlowPane flowPane) {
        this.flowPane = flowPane;
        this.courseDAO = new CourseDAO();
    }

    // Метод для загрузки всех курсов
    public void loadCourses() {
        List<Course> courses = null;
        try {
            courses = courseDAO.getAllCourses(); // Получаем список курсов из базы данных
            System.out.println("Курсы загружены");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Курсы не загружены ОШИБКА");
            return; // Если ошибка, просто выходим
        }

        flowPane.getChildren().clear(); // Очищаем старые карточки

        for (Course course : courses) {
            addCourseToFlowPane(course);
        }
        System.out.println("Курсы добавлены");
    }

    // Метод для добавления одной карточки в FlowPane
    public void addCourseToFlowPane(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/course_directory/cardMaker/CourseCard.fxml"));
            VBox card = loader.load(); // Загружаем карточку

//            CourseCardController controller = loader.getController();
//            controller.setCourseData(course, () -> {
//                // Здесь можно добавить логику при клике на курс (например, открытие страницы)
//            });

            flowPane.getChildren().add(card); // Добавляем карточку в FlowPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

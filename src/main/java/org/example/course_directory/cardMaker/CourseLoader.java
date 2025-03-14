package org.example.course_directory.cardMaker;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import org.example.course_directory.controllers.AdminHomeController;
import org.example.course_directory.dto.CourseDTO;
import org.example.course_directory.entyty.Course;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseLoader {
    private final FlowPane flowPane;  // Контейнер, куда добавляются карточки курсов
    private final CourseDTO courseDTO;  // Работа с базой данных

    private List<Course> cachedCourses = new ArrayList<>();


    public CourseLoader(FlowPane flowPane) {
        this.flowPane = flowPane;
        this.courseDTO = new CourseDTO();
    }

    // Метод для загрузки всех курсов
    public void loadCourses() {
        List<Course> newCourses;
        try {
            newCourses = courseDTO.getAllCourses();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка загрузки курсов!");
            return;
        }

        // Проверяем, изменились ли курсы
        if (isCourseListEqual(newCourses, cachedCourses)) {
            System.out.println("Курсы не изменились, загрузка не требуется.");
            return;
        }

        // Обновляем кэш
        cachedCourses = newCourses;

        // Очищаем и загружаем новые курсы
        Platform.runLater(() -> {
            flowPane.getChildren().clear();
            for (Course course : newCourses) {
                addCourseToFlowPane(course);
            }
            System.out.println("Курсы обновлены.");
        });
    }

    // Метод для сравнения двух списков курсов
    private boolean isCourseListEqual(List<Course> list1, List<Course> list2) {
        if (list1.size() != list2.size()) return false;

        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }



    private AdminHomeController adminHomeController;

    public void setAdminHomeController(AdminHomeController adminHomeController) {
        this.adminHomeController = adminHomeController;
        if (adminHomeController == null) {
            System.out.println("Ошибка: передан NULL в CourseLoader.setAdminHomeController!");
        } else {
            System.out.println("AdminHomeController успешно передан в CourseLoader.");
        }
    }

    private void addCourseToFlowPane(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/course_directory/fxml/cardMaker/CourseCard.fxml"));
            Parent courseCard = loader.load();

            CourseCardController cardController = loader.getController();
            cardController.setCourse(course);
            cardController.setAdminHomeController(adminHomeController);
            cardController.setAdminHomeController(this.adminHomeController);

            // Проверяем adminHomeController перед передачей в карточку курса
            if (adminHomeController != null) {
                cardController.setAdminHomeController(adminHomeController);
                System.out.println("AdminHomeController передан в карточку курса: " + course.getTitle());
            } else {
                System.out.println("Ошибка: AdminHomeController в CourseLoader равен NULL перед передачей в карточку!");
            }

            flowPane.getChildren().add(courseCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AdminHomeController getAdminHomeController() {
        return adminHomeController;
    }
}

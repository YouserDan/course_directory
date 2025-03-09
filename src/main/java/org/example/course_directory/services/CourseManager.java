package org.example.course_directory.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.course_directory.entyty.Course;

public class CourseManager {
    private final ObservableList<Course> courseList = FXCollections.observableArrayList();

    public ObservableList<Course> getCourseList() {
        return courseList;
    }

    public void addCourse(Course course) {
        courseList.add(course);
    }

    public void removeCourse(Course course) {
        courseList.remove(course);
    }

    public void clearCourses() {
        courseList.clear();
    }

    // Метод для загрузки данных (можно заменить на запрос в БД)
    public void loadCourses() {

    }
}

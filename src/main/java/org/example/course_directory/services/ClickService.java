package org.example.course_directory.services;

import org.example.course_directory.dao.ClickDAO;
import org.example.course_directory.entyty.Click;

import java.sql.SQLException;
import java.util.Map;

public class ClickService {

    private ClickDAO clickDAO;

    public ClickService(ClickDAO clickDAO) {
        this.clickDAO = clickDAO;
    }

    // Метод для записи клика
    public void recordClick(int userId, int courseId) throws SQLException {
        // Добавляем новый клик
        clickDAO.addClick(userId, courseId);
    }

    // Метод для увеличения счетчика переходов
    public void recordTransition(int userId, int courseId) throws SQLException {
        // Увеличиваем количество переходов
        clickDAO.incrementTransitionCount(userId, courseId);
    }

    // Метод для получения статистики по курсам
    public void printClicksByCourse() throws SQLException {
        for (Click click : clickDAO.getClicksByCourse()) {
            System.out.println("Course ID: " + click.getCourseId() + ", Total Transitions: " + click.getTransitionCount());
        }
    }

    // Метод для получения статистики по пользователям
    public void printClicksByUser() throws SQLException {
        for (Click click : clickDAO.getClicksByUser()) {
            System.out.println("User ID: " + click.getUserId() + ", Total Transitions: " + click.getTransitionCount());
        }
    }
    public Map<Integer, Integer> getCoursesPopularity() throws SQLException {
        return clickDAO.getCoursesPopularity();
    }

}

package org.example.course_directory.dao;

import org.example.course_directory.entyty.Click;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClickDAO {

    private Connection connection;

    public ClickDAO(Connection connection) {
        this.connection = connection;
    }

    // Метод для добавления нового клика
    public void addClick(int userId, int courseId) throws SQLException {
        String sql = "INSERT INTO clicks (user_id, course_id, clicked_at, transition_count) VALUES (?, ?, NOW(), 0)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        }
    }

    // Метод для обновления количества переходов
    public void incrementTransitionCount(int userId, int courseId) throws SQLException {
        String sql = "UPDATE clicks SET transition_count = transition_count + 1 WHERE user_id = ? AND course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        }
    }

    // Метод для получения статистики по переходам
    public List<Click> getClicksByCourse() throws SQLException {
        List<Click> clicks = new ArrayList<>();
        String sql = "SELECT course_id, MAX(clicked_at) as last_click, SUM(transition_count) as total_transitions " +
                "FROM clicks GROUP BY course_id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                Timestamp lastClick = resultSet.getTimestamp("last_click");
                int totalTransitions = resultSet.getInt("total_transitions");

                Click click = new Click(0, courseId, lastClick, totalTransitions); // userId = 0, так как не используется
                clicks.add(click);
            }
        }
        return clicks;
    }


    // Метод для получения статистики по пользователям
    public List<Click> getClicksByUser() throws SQLException {
        List<Click> clicks = new ArrayList<>();
        String sql = "SELECT user_id, MAX(clicked_at) as last_click, SUM(transition_count) as total_transitions " +
                "FROM clicks GROUP BY user_id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                Timestamp lastClick = resultSet.getTimestamp("last_click");
                int totalTransitions = resultSet.getInt("total_transitions");

                Click click = new Click(userId, 0, lastClick, totalTransitions); // courseId = 0, так как не используется
                clicks.add(click);
            }
        }
        return clicks;
    }

    // Метод для получения популярности курсов (course_id -> total_transitions)
    public Map<Integer, Integer> getCoursesPopularity() throws SQLException {
        Map<Integer, Integer> popularityMap = new java.util.LinkedHashMap<>();
        String sql = "SELECT course_id, SUM(transition_count) AS total_transitions " +
                "FROM clicks GROUP BY course_id ORDER BY total_transitions DESC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                int totalTransitions = resultSet.getInt("total_transitions");
                popularityMap.put(courseId, totalTransitions);
            }
        }
        return popularityMap;
    }



}

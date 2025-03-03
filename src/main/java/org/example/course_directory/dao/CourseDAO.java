package org.example.course_directory.dao;

import org.example.course_directory.connection.DatabaseConnection;
import org.example.course_directory.entyty.Course;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class CourseDAO {

    private Connection connection;

    // Конструктор CourseDAO, который получает соединение
    public CourseDAO() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        this.connection = dbConnection.connectToDatabase();
    }

    // Метод для добавления курса
    public void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (title, author, programming_language, image_url, level, duration, access, price, " +
                "keywords, description, language_of_course, resource_url, created_by, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getAuthor());
            statement.setString(3, course.getProgrammingLanguage());
            statement.setString(4, course.getImageUrl());
            statement.setString(5, course.getLevel());
            statement.setString(6, course.getDuration());
            statement.setString(7, course.getAccess());
            statement.setDouble(8, course.getPrice());
            statement.setString(9, course.getKeywords());
            statement.setString(10, course.getDescription());
            statement.setString(11, course.getLanguageOfCourse());
            statement.setString(12, course.getResourceUrl());
            statement.setString(13, course.getCreatedBy());
            statement.setTimestamp(14, Timestamp.valueOf(course.getCreatedAt()));
            statement.setTimestamp(15, Timestamp.valueOf(course.getUpdatedAt()));

            statement.executeUpdate();
        }
    }

    // Метод для получения курса по ID
    public Course getCourseById(int id) throws SQLException {
        String sql = "SELECT * FROM courses WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToCourse(resultSet);
            } else {
                return null;  // Если курс с таким ID не найден
            }
        }
    }

    // Метод для обновления информации о курсе
    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE courses SET title = ?, author = ?, programming_language = ?, image_url = ?, level = ?, " +
                "duration = ?, access = ?, price = ?, keywords = ?, description = ?, language_of_course = ?, resource_url = ?, " +
                "updated_at = ?, updated_by = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getAuthor());
            statement.setString(3, course.getProgrammingLanguage());
            statement.setString(4, course.getImageUrl());
            statement.setString(5, course.getLevel());
            statement.setString(6, course.getDuration());
            statement.setString(7, course.getAccess());
            statement.setDouble(8, course.getPrice());
            statement.setString(9, course.getKeywords());
            statement.setString(10, course.getDescription());
            statement.setString(11, course.getLanguageOfCourse());
            statement.setString(12, course.getResourceUrl());
            statement.setTimestamp(13, Timestamp.valueOf(course.getUpdatedAt()));
            statement.setString(14, course.getUpdatedBy());
            statement.setInt(15, course.getId());

            statement.executeUpdate();
        }
    }

    // Метод для удаления курса по ID
    public void deleteCourse(int id) throws SQLException {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Метод для получения списка всех курсов
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                courses.add(mapResultSetToCourse(resultSet));
            }
        }
        return courses;
    }

    // Метод для преобразования ResultSet в объект Course
    private Course mapResultSetToCourse(ResultSet resultSet) throws SQLException {
        Course course = new Course(
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("programming_language"),
                resultSet.getString("image_url"),
                resultSet.getString("level"),
                resultSet.getString("duration"),
                resultSet.getString("access"),
                resultSet.getDouble("price"),
                resultSet.getString("keywords"),
                resultSet.getString("description"),
                resultSet.getString("language_of_course"),
                resultSet.getString("resource_url"),
                resultSet.getString("created_by")
        );

        course.setId(resultSet.getInt("id"));
        course.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        course.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        course.setUpdatedBy(resultSet.getString("updated_by"));

        return course;
    }
}

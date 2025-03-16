package org.example.course_directory.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    //Host
//    private static final String URL = "jdbc:mysql://185.192.247.69:3306/default_db";
//    private static final String USERNAME = "gen_user";
//    private static final String PASSWORD = "123456789db";

    //local
   private static final String URL = "jdbc:mysql://localhost:3306/course_directory_DB";
    private static final String USERNAME = "root";
   private static final String PASSWORD = "EW4kDaNlol7";

    public Connection connectToDatabase() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Соединение с базой данных успешно установлено!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных:");
            e.printStackTrace();
            return null;
        }
    }
}
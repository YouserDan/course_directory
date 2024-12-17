package org.example.course_directory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import org.example.course_directory.connection.DatabaseConnection;
import org.example.course_directory.connection.ExecuteQuery;

public class StartProgram extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connectToDatabase();
        ExecuteQuery executeQuery = new ExecuteQuery(connection);
        executeQuery.selectAllDataFromTable("administrator");


        FXMLLoader fxmlLoader = new FXMLLoader(StartProgram.class.getResource("auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

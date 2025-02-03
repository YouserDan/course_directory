package org.example.course_directory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Locale;

import org.example.course_directory.connection.DatabaseConnection;
import org.example.course_directory.connection.ExecuteQuery;

public class StartProgram extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("java version: "+System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));

        FXMLLoader fxmlLoader = new FXMLLoader(StartProgram.class.getResource("fxml/startWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

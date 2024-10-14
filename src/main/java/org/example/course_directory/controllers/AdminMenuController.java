package org.example.course_directory.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminMenuController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

}

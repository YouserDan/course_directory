package org.example.course_directory;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartProgrammController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
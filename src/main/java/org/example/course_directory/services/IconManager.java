package org.example.course_directory.services;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class IconManager {
    private static final Image ICON = new Image(Objects.requireNonNull(
            IconManager.class.getResourceAsStream("/program_icon.png")));

    public static void applyIcon(Stage stage) {
        stage.getIcons().add(ICON);
    }
}
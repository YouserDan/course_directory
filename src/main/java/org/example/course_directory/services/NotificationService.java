package org.example.course_directory.services;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

public class NotificationService {

    // Метод для отображения уведомлений с произвольным заголовком и текстом
    public void showNotification(String title, String header, String content) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        // Контент уведомления
        Label label = new Label(content);
        dialog.getDialogPane().setContent(label);

        // Добавляем кнопку "ОК"
        ButtonType okButton = new ButtonType("ОК");
        dialog.getDialogPane().getButtonTypes().add(okButton);

        // Показываем диалог
        dialog.showAndWait();
    }
}
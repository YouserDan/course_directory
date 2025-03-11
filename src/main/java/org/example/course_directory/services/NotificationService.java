package org.example.course_directory.services;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

import java.util.Optional;

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
    public boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Устанавливаем кнопки "Да" и "Нет"
        ButtonType yesButton = new ButtonType("Да");
        ButtonType noButton = new ButtonType("Нет");
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Показываем диалог и ждём ответа
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }
}
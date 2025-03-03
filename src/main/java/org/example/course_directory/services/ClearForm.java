package org.example.course_directory.services;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class ClearForm {
    // Универсальный метод для очистки формы
    public static void clearForm(Node... nodes) {
        for (Node node : nodes) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            }
            else if (node instanceof TextArea) {
                ((TextArea) node).clear();
            }
            else if (node instanceof ComboBox) {
                ((ComboBox<?>) node).getSelectionModel().clearSelection();
            }
            else if (node instanceof ChoiceBox) {
                ((ChoiceBox<?>) node).getSelectionModel().clearSelection();
            }
            else if (node instanceof Spinner) {
                // Проверяем тип и передаем объект Integer
                Spinner<?> spinner = (Spinner<?>) node;
                if (spinner.getValueFactory() instanceof SpinnerValueFactory.IntegerSpinnerValueFactory) {
                    // Устанавливаем значение 0 для IntegerSpinner
                    ((SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory()).setValue(0);
                } else {
                    // Если это не IntegerSpinner, то можно установить в значение по умолчанию для другого типа
//                    spinner.getValueFactory().setValue(spinner.getValueFactory().getDefaultValue());
                    System.out.print("Не удалось обнулить");
                }
            }

            else if (node instanceof ImageView) {
                ((ImageView) node).setImage(null); // Сброс изображения
            }
            else if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false); // Сброс выбора
            }
//            else if (node instanceof DatePicker) {
//                ((DatePicker) node).setValue(null); // Сброс даты
//            }
            // Добавьте другие проверки для других типов, если нужно
        }
    }
}

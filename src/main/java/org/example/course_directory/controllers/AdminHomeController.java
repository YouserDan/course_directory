package org.example.course_directory.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.course_directory.StartProgram;
import org.example.course_directory.dao.CourseDAO;
import org.example.course_directory.entyty.Course;
import org.example.course_directory.services.ClearForm;
import org.example.course_directory.services.NotificationService;
import org.example.course_directory.connection.DatabaseConnection;
import javafx.scene.control.cell.PropertyValueFactory;



import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminHomeController {

    @FXML
    private AnchorPane homePage;
    @FXML
    private AnchorPane courseCatalog;
    @FXML
    private AnchorPane courseEditor;
    @FXML
    private AnchorPane helpPage;
    @FXML
    private AnchorPane addCourse;
    @FXML
    private AnchorPane editCourse;


    // Заполнение формы курса
    @FXML private TextField courseNameFieldAdd;
    @FXML private TextField courseAutorFieldAdd;
    @FXML private ChoiceBox programmingLanguageChoiseAdd;
    @FXML private ImageView imageView;
    @FXML private Spinner spinnerAdd;
    @FXML private ChoiceBox dataTypeAdd;
    @FXML private ChoiceBox levelChoiseAdd;
    @FXML private ChoiceBox accessAdd;
    @FXML private TextField priceFieldAdd;
    @FXML private ChoiceBox currencyChoiceBox;
    @FXML private TextField keywordsFieldAdd;
    @FXML private TextArea descriptionAdd;
    @FXML private TextField languageOfCourseAdd;
    @FXML private TextField urlAdd;
    @FXML private String createdByField = "Admin";


    //Изменение формы курса
    @FXML private TextField courseNameFieldEdit;
    @FXML private TextField courseAutorFieldEdit;
    @FXML private ChoiceBox programmingLanguageChoiseEdit;
    @FXML private ImageView imageEdit;
    @FXML private Spinner spinnerEdit;
    @FXML private ChoiceBox dataTypeEdit;
    @FXML private ChoiceBox levelChoiseEdit;
    @FXML private ChoiceBox accessEdit;
    @FXML private TextField priceFieldEdit;
    @FXML private ChoiceBox currencyEditChoiceBox;
    @FXML private TextField keywordsFieldEdit;
    @FXML private TextArea descriptionEdit;
    @FXML private TextField languageOfCourseEdit;
    @FXML private TextField urlEdit;

    // Выведение формы в TableView
    @FXML private TableView<Course> tableView;
    @FXML private TableColumn<Course, Integer> colId;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colAuthor;
    @FXML private TableColumn<Course, String> colProgLang;
    @FXML private TableColumn<Course, String> colImgURL;
    @FXML private TableColumn<Course, String> colLevel;
    @FXML private TableColumn<Course, Integer> colDuration;
    @FXML private TableColumn<Course, String> colDurType;
    @FXML private TableColumn<Course, String> colAccess;
    @FXML private TableColumn<Course, Double> colPrice;
    @FXML private TableColumn<Course, String> colCurrency;
    @FXML private TableColumn<Course, String> colDescription;
    @FXML private TableColumn<Course, String> colCourseLang;
    @FXML private TableColumn<Course, String> colResourceURL;
    @FXML private TableColumn<Course, String> colCreatedBy;
    @FXML private TableColumn<Course, String> colCreatedAt;
    @FXML private TableColumn<Course, String> colUpdatedBy;
    @FXML private TableColumn<Course, String> colUpdatedAt;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();


    @FXML
    private Button saveCourseButton;

    public void initialize(){
        homePage.setVisible(true);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);

        //Подгружаем таблицу
        loadDataFromDatabase();
        // Устанавливаем диапазон значений (от 1 до 1000, шаг 1)
        spinnerAdd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
        // Включаем ввод вручную (если нужно)
        spinnerAdd.setEditable(true);

        // Устанавливаем диапазон значений (от 1 до 1000, шаг 1)
        spinnerEdit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
        // Включаем ввод вручную (если нужно)
        spinnerEdit.setEditable(true);

        // Устанавливаем обработчик события изменения типа курса
        accessAdd.setOnAction(event -> handleAccessChoiceSelection());

    }
    private void loadDataFromDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.connectToDatabase();

        if (connection == null) {
            System.out.println("Не удалось подключиться к БД");
            return;
        }

        String query = "SELECT * FROM courses";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Course course = new Course(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("programming_language"),
                        resultSet.getString("image_url"),
                        resultSet.getString("level"),
                        resultSet.getInt("duration"),
                        resultSet.getString("duration_type"),
                        resultSet.getString("access"),
                        resultSet.getDouble("price"),
                        resultSet.getString("currency"),
                        resultSet.getString("description"),
                        resultSet.getString("language_of_course"),
                        resultSet.getString("resource_url"),
                        resultSet.getString("created_by"),
                        resultSet.getString("created_at"),
                        resultSet.getString("updated_by"),
                        resultSet.getString("updated_at")
                );
                courseList.add(course);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(courseList);
        setupColumns();
    }

    private void setupColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colProgLang.setCellValueFactory(new PropertyValueFactory<>("programmingLanguage"));
        colImgURL.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colDurType.setCellValueFactory(new PropertyValueFactory<>("durationType"));
        colAccess.setCellValueFactory(new PropertyValueFactory<>("access"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCurrency.setCellValueFactory(new PropertyValueFactory<>("currency"));
        // Обрезаем описание, если оно слишком длинное
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setCellFactory(column -> {
            return new TableCell<Course, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Ограничение на длину текста (например, 20 символов)
                        String truncatedText = item.length() > 20 ? item.substring(0, 20) + "..." : item;
                        setText(truncatedText);
                    }
                }
            };
        });
        colCourseLang.setCellValueFactory(new PropertyValueFactory<>("languageOfCourse"));
        colResourceURL.setCellValueFactory(new PropertyValueFactory<>("resourceUrl"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("updatedBy"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));


    }



    private void handleAccessChoiceSelection() {
        // Проверяем, выбран ли тип курса "Бесплатный"
        if ("Бесплатный".equals(accessAdd.getValue())) {
            priceFieldAdd.setText("0");  // Устанавливаем цену в 0
            priceFieldAdd.setDisable(true);  // Блокируем поле
            currencyChoiceBox.setDisable(true);
        } else {
            priceFieldAdd.setDisable(false);  // Разблокируем поле для редактирования
            priceFieldAdd.clear();  // Очистим поле, если курс не бесплатный
            currencyChoiceBox.setDisable(false);
        }
    }

    // Метод для сохранения курса в базу данных
    @FXML
    public void saveCourse() {
        try {
            // Считываем данные из формы
            String title = courseNameFieldAdd.getText();
            String author = courseAutorFieldAdd.getText();
            String programmingLanguage = (String) programmingLanguageChoiseAdd.getValue();
            String imageUrl = String.valueOf(imageView.getImage());
            String level = (String) levelChoiseAdd.getValue();
            String duration = String.valueOf(spinnerAdd.getValue());
            String durationType = (String) dataTypeAdd.getValue();
            String access = (String) accessAdd.getValue();
            double price = 0;

            // Проверяем, является ли курс бесплатным
            if (!"Бесплатный".equals(access)) {
                price = Double.parseDouble(priceFieldAdd.getText());  // Если курс платный, берем цену из поля
            }

            String currency = (String) currencyChoiceBox.getValue();
            String keywords = keywordsFieldAdd.getText();
            String description = descriptionAdd.getText();
            String languageOfCourse = languageOfCourseAdd.getText();
            String resourceUrl = urlAdd.getText();
            String createdBy = createdByField;

            // Проверяем, не превышает ли описание 550 символов
            if (description.length() > 550) {
                // Показать уведомление, если длина описания больше 550 символов
                showAlert("Ошибка ввода", "Описание не может содержать более 550 символов.");
                return;  // Прерываем выполнение метода, чтобы не сохранять курс
            }

            // Создаем объект Course с введенными данными
            Course newCourse = new Course(
                    title,
                    author,
                    programmingLanguage,
                    imageUrl,
                    level,
                    duration,
                    durationType,
                    access,
                    price,
                    currency,
                    keywords,
                    description,
                    languageOfCourse,
                    resourceUrl,
                    createdBy
            );

            // Устанавливаем время создания и обновления
            newCourse.setCreatedAt(java.time.LocalDateTime.now());
            newCourse.setUpdatedAt(java.time.LocalDateTime.now());

            // Добавляем курс в базу данных через CourseDAO
            CourseDAO courseDAO = new CourseDAO();
            courseDAO.addCourse(newCourse);

            // Уведомляем пользователя о том, что курс был успешно добавлен
            showAlert("Успех", "Курс был успешно добавлен в базу данных.");

            // Очищаем форму после добавления
            ClearForm.clearForm(courseNameFieldAdd, courseAutorFieldAdd, programmingLanguageChoiseAdd, imageView,
                    spinnerAdd, dataTypeAdd, levelChoiseAdd, accessAdd, priceFieldAdd, currencyChoiceBox, keywordsFieldAdd, descriptionAdd,
                    languageOfCourseAdd, urlAdd);


            // Очистить текущую таблицу
            courseList.clear(); // Очистить коллекцию

            // Перезагрузить данные в таблицу
            loadDataFromDatabase(); // Снова загрузить данные из базы данных
            // Переключаем страницы
            homePage.setVisible(false);
            courseCatalog.setVisible(false);
            courseEditor.setVisible(true);
            helpPage.setVisible(false);
            addCourse.setVisible(false);
            editCourse.setVisible(false);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при добавлении курса в базу данных.");
        } catch (NumberFormatException e) {
            showAlert("Ошибка ввода", "Пожалуйста, убедитесь, что все поля заполнены корректно.");
        }
    }

    //Можно оптимизировать
    // Метод для отображения сообщений об успехе или ошибке
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void viewCourses(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(true);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
    }

    public void openEditorCoursePage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(true);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
    }

    public void openHelpPage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(true);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
    }

    public void backToMenu(javafx.event.ActionEvent event) {
        NotificationService notificationService = new NotificationService();
        boolean confirmExit = notificationService.showConfirmationDialog("Подтверждение", "Вы уверены, что хотите выйти в главное меню?");

        if (confirmExit) {
            try {
                FXMLLoader loader = new FXMLLoader(StartProgram.class.getResource("/org/example/course_directory/fxml/authWindow.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Авторизация");
                stage.setScene(new Scene(root));
                stage.show();

                // Закрываем текущее окно
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Не удалось загрузить окно авторизации");
            }
        }
    }

    public void openHomePage(ActionEvent actionEvent) {
        homePage.setVisible(true);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
    }

    public void openAddPage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(true);
        editCourse.setVisible(false);
    }

    public void backToEditor(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(true);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
        ClearForm.clearForm(courseNameFieldAdd, courseAutorFieldAdd, programmingLanguageChoiseAdd, imageView,
                spinnerAdd, dataTypeAdd, levelChoiseAdd, accessAdd, priceFieldAdd, currencyChoiceBox, keywordsFieldAdd, descriptionAdd,
                languageOfCourseAdd, urlAdd);
        ClearForm.clearForm(courseNameFieldEdit, courseAutorFieldEdit, programmingLanguageChoiseEdit, imageEdit,
                spinnerEdit, dataTypeEdit, levelChoiseEdit, accessEdit, priceFieldEdit, currencyEditChoiceBox, keywordsFieldEdit, descriptionEdit,
                languageOfCourseEdit, urlEdit);
    }


    public void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");

        // Фильтр для изображений
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Получаем текущее окно (Stage)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Открываем диалоговое окно
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("Выбран файл: " + file.getAbsolutePath());

            // Создаём Image из выбранного файла
            Image image = new Image(file.toURI().toString());

            // Отображаем в ImageView
            imageView.setImage(image);
        }
    }

    //УДАЛЕНИЕ КУРСА
    private NotificationService notificationService = new NotificationService();
    private CourseDAO courseDAO = new CourseDAO();

    @FXML
    public void deleteCourse() {
        // Получаем выбранный курс
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            // Если курс не выбран, показываем уведомление
            notificationService.showNotification("Ошибка", "Вы не выбрали курс", "Пожалуйста, выберите курс для удаления.");
            return;
        }

        // Показываем диалог подтверждения
        boolean confirmed = notificationService.showConfirmationDialog("Подтверждение удаления",
                "Вы уверены, что хотите удалить курс \"" + selectedCourse.getTitle() + "\"?");

        // Если пользователь подтвердил, удаляем курс
        if (confirmed) {
            try {
                // Удаляем курс из базы данных
                courseDAO.deleteCourse(selectedCourse.getId());

                // Показываем уведомление об успехе
                notificationService.showNotification("Успех", "Удаление курса",
                        "Курс \"" + selectedCourse.getTitle() + "\" был успешно удален.");

                // Очистить текущую таблицу
                courseList.clear(); // Очистить коллекцию

                // Перезагрузить данные в таблицу
                loadDataFromDatabase(); // Снова загрузить данные из базы данных
            } catch (SQLException e) {
                // В случае ошибки при удалении
                notificationService.showNotification("Ошибка", "Ошибка при удалении",
                        "Произошла ошибка при удалении курса. Попробуйте еще раз.");
                e.printStackTrace();
            }
        }
    }

    public void editCourse(ActionEvent actionEvent) {
        // Получаем выбранный курс
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            // Если курс не выбран, показываем уведомление
            notificationService.showNotification("Ошибка", "Вы не выбрали курс", "Пожалуйста, выберите курс для изменения.");
            return;
        }

        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(true);


    }

    public void saveEditedCourse(ActionEvent actionEvent) {

    }
}

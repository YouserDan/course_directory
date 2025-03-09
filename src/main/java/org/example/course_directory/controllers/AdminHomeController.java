package org.example.course_directory.controllers;

import javafx.application.Platform;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.example.course_directory.StartProgram;
import org.example.course_directory.cardMaker.CourseLoader;
import org.example.course_directory.dao.CourseDAO;
import org.example.course_directory.entyty.Course;
import org.example.course_directory.services.ClearForm;
import org.example.course_directory.services.NotificationService;
import org.example.course_directory.connection.DatabaseConnection;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.example.course_directory.services.CourseManager;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

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
    @FXML
    private AnchorPane aboutCoursePage;

    //Для поиска
    @FXML private TextField searchlInTableField;

    //Для карточек курса
    @FXML
    private FlowPane courseFlowPane; // Это поле должно быть связано с FXML
    private CourseLoader courseLoader;




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

//    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    private CourseManager courseManager = new CourseManager();

    @FXML
    private SplitPane splitPane;
    @FXML
    private Button saveCourseButton;

    public void initialize(){
        homePage.setVisible(true);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
        aboutCoursePage.setVisible(false);

        Platform.runLater(() -> {
            splitPane.lookupAll(".split-pane-divider").forEach(divider -> divider.setMouseTransparent(true));
            splitPane.setDividerPositions(0.3);
        });


        //Подгружаем таблицу
        loadDataFromDatabase();

        if (courseFlowPane == null) {
            System.out.println("Ошибка: courseFlowPane не загружен из FXML!");
        } else {
            courseLoader = new CourseLoader(courseFlowPane);
            System.out.println("CourseLoader успешно инициализирован!");
        }

// Проверяем, что tableView не пустой перед работой с ним
        if (tableView != null) {
            tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    String imageUrl = newSelection.getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Image image = new Image(imageUrl);
                        imageEdit.setImage(image);
                    }
                }
            });
        }

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

        // Устанавливаем обработчик для нажатия клавиши в поле поиска
        searchlInTableField.setOnAction(this::searchInTable);  // Это будет вызывать метод поиска при нажатии Enter

    }

    public void openAboutCoursePage(){
        System.out.println("Название курса");
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

                courseManager.addCourse(course);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(courseManager.getCourseList());
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

            // Получаем URL изображения (проверяем, не пусто ли оно)
            String imageUrl = "";
            if (imageView.getImage() != null) {
                imageUrl = imageView.getImage().getUrl();
            }

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
//            courseLoader.addCourseToFlowPane(course); // Добавляем курс в интерфейс

            // Уведомляем пользователя о том, что курс был успешно добавлен
            showAlert("Успех", "Курс был успешно добавлен в базу данных.");

            // Очищаем форму после добавления
            ClearForm.clearForm(courseNameFieldAdd, courseAutorFieldAdd, programmingLanguageChoiseAdd, imageView,
                    spinnerAdd, dataTypeAdd, levelChoiseAdd, accessAdd, priceFieldAdd, currencyChoiceBox, keywordsFieldAdd, descriptionAdd,
                    languageOfCourseAdd, urlAdd);

            // Очистить текущую таблицу
            courseManager.clearCourses(); // Очистить коллекцию

            // Перезагрузить данные в таблицу
            loadDataFromDatabase(); // Снова загрузить данные из базы данных
            // Переключаем страницы
            homePage.setVisible(false);
            courseCatalog.setVisible(false);
            courseEditor.setVisible(true);
            helpPage.setVisible(false);
            addCourse.setVisible(false);
            editCourse.setVisible(false);
            aboutCoursePage.setVisible(false);

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

    public void viewCourses(ActionEvent actionEvent) throws SQLException {
        // Проверяем, что courseLoader был инициализирован
        if (courseLoader != null) {
            courseLoader.loadCourses();  // Загружаем курсы
        } else {
            System.out.println("Ошибка: CourseLoader не инициализирован!");
        }

        homePage.setVisible(false);
        courseCatalog.setVisible(true);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
        aboutCoursePage.setVisible(false);
    }

    public void openEditorCoursePage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(true);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
        aboutCoursePage.setVisible(false);
    }

    public void openHelpPage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(true);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
        aboutCoursePage.setVisible(false);
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
        aboutCoursePage.setVisible(false);
    }

    public void openAddPage(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(true);
        editCourse.setVisible(false);
        aboutCoursePage.setVisible(false);
    }

    public void backToEditor(ActionEvent actionEvent) {
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(true);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(false);
        aboutCoursePage.setVisible(false);
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

        // Фильтр для выбора изображений
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Получаем текущее окно (Stage)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Открываем диалоговое окно выбора изображения
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("Выбран файл: " + file.getAbsolutePath());

            // Создаём Image из выбранного файла
            Image newImage = new Image(file.toURI().toString());

            // 🔹 Обновляем картинку в форме создания
            Platform.runLater(() -> imageView.setImage(newImage));

            // 🔹 Обновляем картинку в форме редактирования
            Platform.runLater(() -> imageEdit.setImage(newImage));

            // 🔹 Обновляем ссылку на изображение в объекте выбранного курса
            Course selectedCourse = tableView.getSelectionModel().getSelectedItem();
            if (selectedCourse != null) {
                selectedCourse.setImageUrl(file.toURI().toString());
            }
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
                courseManager.clearCourses(); // Очистить коллекцию

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
            Platform.runLater(() -> {
                notificationService.showNotification("Ошибка", "Вы не выбрали курс", "Пожалуйста, выберите курс для изменения.");
            });
            return;
        }

        // Заполняем форму данными из выбранного курса
        courseNameFieldEdit.setText(selectedCourse.getTitle());
        courseAutorFieldEdit.setText(selectedCourse.getAuthor());
        programmingLanguageChoiseEdit.setValue(selectedCourse.getProgrammingLanguage());
        // Устанавливаем изображение
        if (selectedCourse.getImageUrl() != null && !selectedCourse.getImageUrl().isEmpty()) {
            try {
                imageEdit.setImage(new Image(selectedCourse.getImageUrl()));
            } catch (Exception e) {
                notificationService.showNotification("Ошибка", "Ошибка загрузки изображения", "Проверьте URL изображения.");
            }
        }
        levelChoiseEdit.setValue(selectedCourse.getLevel());
        // Проверяем, что duration является числом
        try {
            int duration = Integer.parseInt(selectedCourse.getDuration());

            if (spinnerEdit.getValueFactory() == null) {
                spinnerEdit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, duration));
            } else {
                spinnerEdit.getValueFactory().setValue(duration);
            }
        } catch (NumberFormatException e) {
            notificationService.showNotification("Ошибка", "Некорректное значение длительности", "Пожалуйста, проверьте данные курса.");
        }
        dataTypeEdit.setValue(selectedCourse.getDurationType());
        accessEdit.setValue(selectedCourse.getAccess());
        priceFieldEdit.setText(String.valueOf(selectedCourse.getPrice()));
        currencyEditChoiceBox.setValue(selectedCourse.getCurrency());
        keywordsFieldEdit.setText(selectedCourse.getKeywords());
        descriptionEdit.setText(selectedCourse.getDescription());
        languageOfCourseEdit.setText(selectedCourse.getLanguageOfCourse());
        urlEdit.setText(selectedCourse.getResourceUrl());
        // Проверка на бесплатный курс
        // Обработчик изменения типа доступа
        accessEdit.setOnAction(event -> {
            updatePriceAndCurrencyFields();
        });

        // Вызовем функцию блокировки полей сразу после загрузки данных
        updatePriceAndCurrencyFields();
        // Переключение видимости страниц
        homePage.setVisible(false);
        courseCatalog.setVisible(false);
        courseEditor.setVisible(false);
        helpPage.setVisible(false);
        addCourse.setVisible(false);
        editCourse.setVisible(true);
        aboutCoursePage.setVisible(false);
    }

    private void updatePriceAndCurrencyFields() {
        // Получаем текущий тип доступа
        String access = (String) accessEdit.getValue();

        if ("Бесплатный".equals(access)) {
            // Если курс бесплатный, устанавливаем цену в 0 и блокируем поля
            priceFieldEdit.setText("0");
            priceFieldEdit.setDisable(true);  // Блокируем поле цены
            currencyEditChoiceBox.setDisable(true);  // Блокируем выбор валюты
        } else {
            // Если курс не бесплатный, разблокируем поля
            priceFieldEdit.setDisable(false);
            currencyEditChoiceBox.setDisable(false);
        }
    }

    public void saveEditedCourse(ActionEvent actionEvent) {
        Course selectedCourse = tableView.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            notificationService.showNotification("Ошибка", "Нет выбранного курса", "Выберите курс перед редактированием.");
            return;
        }

        try {
            // Обновляем данные курса из формы
            selectedCourse.setTitle(courseNameFieldEdit.getText());
            selectedCourse.setAuthor(courseAutorFieldEdit.getText());

            String programmingLanguage = (String) programmingLanguageChoiseEdit.getValue();
            if (programmingLanguage != null) {
                selectedCourse.setProgrammingLanguage(programmingLanguage);
            } else {
                notificationService.showNotification("Ошибка", "Не выбран язык программирования", "Пожалуйста, выберите язык программирования.");
                return;
            }

            // 🔹 ВАЖНО! Убедимся, что изображение сохраняется
            if (imageEdit.getImage() != null && imageEdit.getImage().getUrl() != null) {
                selectedCourse.setImageUrl(imageEdit.getImage().getUrl());
            } else {
                selectedCourse.setImageUrl(""); // Если нет картинки, сохраняем пустую строку
            }

            selectedCourse.setLevel((String) levelChoiseEdit.getValue());

            Integer duration = (Integer) spinnerEdit.getValue();
            if (duration != null) {
                selectedCourse.setDuration(String.valueOf(duration));
            } else {
                notificationService.showNotification("Ошибка", "Не выбрана продолжительность", "Пожалуйста, выберите продолжительность.");
                return;
            }

            selectedCourse.setDurationType((String) dataTypeEdit.getValue());
            selectedCourse.setAccess((String) accessEdit.getValue());

            try {
                selectedCourse.setPrice(Double.parseDouble(priceFieldEdit.getText()));
            } catch (NumberFormatException e) {
                notificationService.showNotification("Ошибка", "Неверный формат цены", "Введите правильную цену.");
                return;
            }

            selectedCourse.setCurrency((String) currencyEditChoiceBox.getValue());
            selectedCourse.setKeywords(keywordsFieldEdit.getText());
            selectedCourse.setDescription(descriptionEdit.getText());
            selectedCourse.setLanguageOfCourse(languageOfCourseEdit.getText());
            selectedCourse.setResourceUrl(urlEdit.getText());
            selectedCourse.setUpdatedBy("Admin");
            selectedCourse.setUpdatedAt(LocalDateTime.now());

            // 🔹 ЛОГ для отладки
            System.out.println("Сохраняемое изображение: " + selectedCourse.getImageUrl());

            // 🔹 Обновляем курс в базе данных
            courseDAO.updateCourse(selectedCourse);

            // 🔹 Обновляем таблицу
            tableView.refresh();

            // Показываем уведомление
            notificationService.showNotification("Успех", "Редактирование курса", "Изменения успешно сохранены.");

            // Возвращаемся в редактор курсов
            backToEditor(actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
            notificationService.showNotification("Ошибка", "Ошибка сохранения", "Произошла ошибка при сохранении изменений.");
        }
    }

    public void doReport(ActionEvent actionEvent) {
        // Получаем текущую дату и время в нужном формате
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = now.format(formatter);

        // Создаем объект FileChooser для выбора места сохранения
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчет");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel файлы (*.xlsx)", "*.xlsx"));

        // Устанавливаем начальное имя файла с датой и временем
        fileChooser.setInitialFileName("отчет_от_" + formattedDateTime + ".xlsx");

        // Показываем диалоговое окно выбора файла
        File file = fileChooser.showSaveDialog(new Stage());

        // Если пользователь выбрал файл для сохранения
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(file)) {
                Sheet sheet = workbook.createSheet("Курсы");

                // Создаём стиль для форматирования даты
                CreationHelper createHelper = workbook.getCreationHelper();
                CellStyle dateCellStyle = workbook.createCellStyle();
                dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy HH:mm"));

                // Заголовки
                Row headerRow = sheet.createRow(0);
                String[] columns = {"ID", "Название", "Автор", "Язык программирования", "URL изображения", "Уровень",
                        "Длительность", "Тип длительности", "Доступ", "Цена", "Валюта",
                        "Описание", "Язык курса", "URL ресурса", "Кем создано", "Дата создания",
                        "Кем обновлено", "Дата обновления"};

                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    CellStyle headerStyle = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    headerStyle.setFont(font);
                    cell.setCellStyle(headerStyle);
                }

                // Получаем данные из таблицы
                List<Course> courses = tableView.getItems();

                // Заполняем данные
                int rowNum = 1;
                for (Course course : courses) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(course.getId());
                    row.createCell(1).setCellValue(course.getTitle());
                    row.createCell(2).setCellValue(course.getAuthor());
                    row.createCell(3).setCellValue(course.getProgrammingLanguage());
                    row.createCell(4).setCellValue(course.getImageUrl());
                    row.createCell(5).setCellValue(course.getLevel());
                    row.createCell(6).setCellValue(course.getDuration());
                    row.createCell(7).setCellValue(course.getDurationType());
                    row.createCell(8).setCellValue(course.getAccess());
                    row.createCell(9).setCellValue(course.getPrice());
                    row.createCell(10).setCellValue(course.getCurrency());
                    row.createCell(11).setCellValue(course.getDescription());
                    row.createCell(12).setCellValue(course.getLanguageOfCourse());
                    row.createCell(13).setCellValue(course.getResourceUrl());
                    row.createCell(14).setCellValue(course.getCreatedBy());

                    // Дата создания
                    if (course.getCreatedAt() != null) {
                        Cell createdAtCell = row.createCell(15);
                        createdAtCell.setCellValue(java.sql.Timestamp.valueOf(course.getCreatedAt()));
                        createdAtCell.setCellStyle(dateCellStyle);
                    }

                    row.createCell(16).setCellValue(course.getUpdatedBy());

                    // Дата обновления
                    if (course.getUpdatedAt() != null) {
                        Cell updatedAtCell = row.createCell(17);
                        updatedAtCell.setCellValue(java.sql.Timestamp.valueOf(course.getUpdatedAt()));
                        updatedAtCell.setCellStyle(dateCellStyle);
                    }
                }

                // Авторазмер колонок
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Сохраняем в файл
                workbook.write(fileOut);

                // Уведомление
                notificationService.showNotification("Успех", "Отчет сохранен", "Файл успешно сохранен в " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                notificationService.showNotification("Ошибка", "Ошибка экспорта", "Не удалось сохранить отчет.");
            }
        }
    }

    @FXML
    private void searchInTable(ActionEvent event) {
        System.out.println("Поиск применён");
        String query = searchlInTableField.getText().toLowerCase();  // Получаем текст поиска
        filterTable(query);  // Применяем фильтрацию
    }
    @FXML
    private void handleSearchInput(KeyEvent event) {
        String query = searchlInTableField.getText().toLowerCase();  // Получаем текст поиска
        filterTable(query);  // Применяем фильтрацию
    }

    private void filterTable(String query) {
        ObservableList<Course> filteredCourses = FXCollections.observableArrayList();

        // Преобразуем запрос в нижний регистр для поиска без учета регистра
        query = query.toLowerCase();

        // Проходим по всем курсам и фильтруем по нескольким полям
        for (Course course : courseManager.getCourseList()) {
            // Проверяем, содержат ли какие-либо из полей курс совпадение с запросом
            if (course.getTitle().toLowerCase().contains(query) ||  // Проверяем название курса
                    course.getAuthor().toLowerCase().contains(query) ||  // Проверяем автора
                    course.getProgrammingLanguage().toLowerCase().contains(query) ||  // Проверяем язык программирования
                    course.getLevel().toLowerCase().contains(query) ||  // Проверяем уровень
                    course.getAccess().toLowerCase().contains(query) || // Доступ
                    course.getDescription().toLowerCase().contains(query) ||  // Проверяем описание
                    course.getLanguageOfCourse().toLowerCase().contains(query) ||  // Проверяем язык курса
                    course.getResourceUrl().toLowerCase().contains(query)) {  // Проверяем URL ресурса
                filteredCourses.add(course); // Добавляем курс, если совпадает
            }
        }

        // Обновляем таблицу с отфильтрованными курсами
        tableView.setItems(filteredCourses);

        // Если найден хотя бы один курс, выделим его и прокрутим
        if (!filteredCourses.isEmpty()) {
            // Дополнительно можно выделить и прокрутить ко всем найденным совпадениям
            Course firstMatch = filteredCourses.get(0);  // Берем первый подходящий курс
            selectAndScrollToCourse(firstMatch);
        }
    }

    private void selectAndScrollToCourse(Course course) {
        // Находим индекс нужного курса в таблице
        int index = tableView.getItems().indexOf(course);

        if (index != -1) {
            // Выделяем строку
            tableView.getSelectionModel().select(index);

            // Прокручиваем таблицу до выбранной строки
            tableView.scrollTo(index);
        }
    }

    public void reloadDataInTable(ActionEvent actionEvent) {
        // Очищаем список перед загрузкой новых данных
        courseManager.clearCourses();
        loadDataFromDatabase();
        setupColumns();
        tableView.refresh();
    }
}

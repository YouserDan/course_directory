module org.example.course_directory {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.course_directory to javafx.fxml;
    exports org.example.course_directory;
}
module org.example.course_directory {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;


    opens org.example.course_directory.entyty to javafx.base;
    opens org.example.course_directory to javafx.fxml;
    exports org.example.course_directory;
    exports org.example.course_directory.controllers;
    opens org.example.course_directory.controllers to javafx.fxml;
}
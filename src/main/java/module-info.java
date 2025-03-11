module org.example.course_directory {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires org.apache.poi.ooxml;
    requires mysql.connector.j;
    requires java.desktop;


    opens org.example.course_directory.entyty to javafx.base;
    opens org.example.course_directory to javafx.fxml;
    exports org.example.course_directory;
    exports org.example.course_directory.controllers;
    opens org.example.course_directory.controllers to javafx.fxml;
    opens org.example.course_directory.cardMaker to javafx.fxml;
}
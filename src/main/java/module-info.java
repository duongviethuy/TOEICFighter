module project.toeicfighter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens project.toeicfighter.controllers to javafx.fxml;
    opens project.toeicfighter.views to javafx.fxml;

    exports project.toeicfighter;
}
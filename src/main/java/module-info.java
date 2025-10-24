module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.todolist to javafx.fxml;
    opens com.example.todolist.controller to javafx.fxml;

    exports com.example.todolist;
    exports com.example.todolist.controller;
}
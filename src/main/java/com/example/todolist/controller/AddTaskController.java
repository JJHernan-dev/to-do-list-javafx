package com.example.todolist.controller;

import com.example.todolist.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class AddTaskController {

    @FXML
    private TextField txtTitle;

    @FXML
    private TextArea txtDescription;

    @FXML
    private DatePicker datePicker;

    private Task newTask;

    public Task getNewTask(){
        return newTask;
    }


    @FXML
    private void onSave(){
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        LocalDate dueDate = datePicker.getValue();

        if (title.isEmpty() || dueDate == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setHeaderText("Por favor, introduce un título y una fecha.");
            alert.showAndWait();
            return;
        }

        newTask = new Task(title, description, dueDate);
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel(){
        newTask = null;
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }


}

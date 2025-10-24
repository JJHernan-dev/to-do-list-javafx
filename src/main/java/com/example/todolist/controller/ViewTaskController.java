package com.example.todolist.controller;

import com.example.todolist.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ViewTaskController {

    @FXML
    private TextField txtTitle;

    @FXML
    private TextArea txtDescription;

    @FXML
    private DatePicker datePicker;

    @FXML
    private CheckBox chkCompleted;

    private Task task;

    public void setTask(Task task){
        this.task = task;
        txtTitle.setText(task.getTitle());
        txtDescription.setText(task.getDescription());
        datePicker.setValue(task.getDueDate());
        chkCompleted.setSelected(task.isCompleted());
    }

    @FXML
    private void onSave(){
        if (task != null){
            task.setTitle((txtTitle.getText()));
            task.setDescription(txtDescription.getText());
            task.setLocalDate(datePicker.getValue());
            task.setCompleted(chkCompleted.isSelected());
        }

        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClose(){
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }
}

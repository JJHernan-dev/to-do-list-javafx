package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.util.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.css.PseudoClass;

public class MainController {

    @FXML
    private TableView<Task> tableView;

    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private TableColumn<Task, String> dateColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;

    @FXML
    private Button btnAddTask;

    @FXML
    private Button btnDeleteTask;

    @FXML
    private Button btnMarkDone;

    @FXML
    private Button btnViewTask;

    @FXML
    private Label lblTaskCount;

    //Lista observable de tareas par que la tabla se actualice automaticamente.
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        //Columnas de la tabla
        //“Durante el desarrollo del proyecto, adapté el uso de PropertyValueFactory a expresiones lambda explícitas para mejorar la compatibilidad con el sistema modular de JavaFX.
        //Esto elimina dependencias reflejadas, aumenta la seguridad del código y permite al compilador verificar los accesos a propiedades.”
        titleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitle()));

        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDueDate() != null) {
                String formattedDate = cellData.getValue().getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                return new SimpleStringProperty(formattedDate);
            } else {
                return new javafx.beans.property.SimpleStringProperty("-");
            }
        });

        statusColumn.setCellFactory(column -> new TableCell<Task, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Task task = getTableRow().getItem();

                    if (task.isCompleted()) {
                        setText("✅");
                        setStyle("-fx-alignment: CENTER; -fx-text-fill: green; -fx-font-size: 16px;");
                    } else {
                        setText("Pendiente");
                        setStyle("-fx-alignment: CENTER; -fx-text-fill: black;");
                    }
                }
            }
        });

        //Cargar tareas desde archivo
        taskList.addAll(FileManager.loadTask());
        //Vincular la lista con la tabla
        tableView.setItems(taskList);
        //Guardar automáticamente cuando cambie la lista
        taskList.addListener((javafx.collections.ListChangeListener.Change<? extends Task> c) -> {
            FileManager.saveTasks(taskList);
        });

        //Vincular la lista observable con la tabla
        tableView.setItems(taskList);

        //Cambiar color de fondo de la fila si está completada
        PseudoClass completedClass = PseudoClass.getPseudoClass("completed");

        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    pseudoClassStateChanged(completedClass, false);
                } else {
                    pseudoClassStateChanged(completedClass, task.isCompleted());
                }
            }
        });

        //Cambio de configuración de colores para las tablas, esto hace que sea más legible dado que FX configura colores
        //predeterminados y al seleccionar una fila veremos el color del texto en blanco en vez de negro.
        tableView.getStylesheets().add(getClass().getResource("/com/example/todolist/styles.css").toExternalForm());

        //Actualización del contador de tareas que llevamos completadas o sin completar
        updateTaskCount();

    }

    //BOTONES
        @FXML
        private void onAddTask() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todolist/add-task-view.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Nueva Tarea");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                AddTaskController controller = loader.getController();
                Task newTask = controller.getNewTask();


                if (newTask != null) {
                    taskList.add(newTask);
                    updateTaskCount();
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }

        @FXML
        private void onDeleteTask() {
            Task selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                taskList.remove(selected);
                FileManager.saveTasks(taskList);
                updateTaskCount();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eliminar Tarea");
                alert.setHeaderText(null);
                alert.setContentText("Selecciona una tarea para eliminar");
                alert.showAndWait();
            }
        }

        @FXML
        private void onMarkDone() {
        Task selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null){
            selected.setCompleted(true);
            tableView.refresh();
            FileManager.saveTasks(taskList);
            updateTaskCount();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Marcar como Completada");
            alert.setContentText("Selecciona una tarea para marcarla como completada.");
            alert.showAndWait();
        }

    }

    @FXML
    private void onViewTask() {

        Task selectedTask = tableView.getSelectionModel().getSelectedItem();

        if (selectedTask == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin selección");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona una tarea para ver sus detalles");
            alert.showAndWait();
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todolist/view-task.fxml"));
            Parent root = loader.load();

            ViewTaskController controller = loader.getController();
            controller.setTask(selectedTask);

            Stage stage = new Stage();
            stage.setTitle("Detalles de la tarea");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);
            stage.setOnHidden(windowEvent -> {
                tableView.refresh();
                FileManager.saveTasks(taskList);
                updateTaskCount();
            });

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void updateTaskCount(){
        long completed = taskList.stream().filter(Task::isCompleted).count();
        long pending = taskList.size() - completed;
        lblTaskCount.setText(String.format("Tareas: %d totales | %d pendientes | %d completadas ✅"
        ,taskList.size(), pending, completed));
    }


}

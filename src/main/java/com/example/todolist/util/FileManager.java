package com.example.todolist.util;

import com.example.todolist.model.Task;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String FILE_NAME = "task.dat";

    public static void saveTasks(List<Task> task){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            oos.writeObject(new ArrayList<>(task));
        }catch(IOException e){
            System.out.println("Error al guardar las tareas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Task> loadTask(){
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Task>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            System.err.println("Error al cargar las tareas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}

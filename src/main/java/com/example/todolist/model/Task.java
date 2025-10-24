package com.example.todolist.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable{

    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;


    public Task(String title, String description, LocalDate dueDate){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;

    }

    // Getter y Setters
    public String getTitle(){

        return title;
    }

    public String getDescription(){

        return description;
    }

    public LocalDate getDueDate(){

        return dueDate;
    }

    public boolean isCompleted(){

        return completed;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public void setDescription(String description){

        this.description = description;
    }

    public void setLocalDate(LocalDate dueDate){

        this.dueDate = dueDate;
    }

    public void setCompleted(boolean completed){

        this.completed = completed;
    }

    @Override
    public String toString(){

        return title + (completed ? "(Completada)" : "");
    }
}

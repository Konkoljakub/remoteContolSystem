package com.example.rfida;

public class TasksC {
    private String taskNumber, taskTime, taskDate, taskName;


    public TasksC() {
    }

    public TasksC(String taskNumber, String taskTime, String taskDate, String taskName) {
        this.taskNumber = taskNumber;
        this.taskTime = taskTime;
        this.taskDate = taskDate;
        this.taskName = taskName;

    }

    public String getTaskNumber() {
        return taskNumber;
    }
    public String getTaskTime() {
        return taskTime;
    }
    public String getTaskDate() {
        return taskDate;
    }

    public String getTaskName(){
        if(taskNumber.equals("256BD32B")){
            taskName = "PRANIE";
            return taskName;
        }
        else if(taskNumber.equals("A3AC1E4D")){
            taskName = "MYCIE ZĘBÓW";
            return taskName;
        }
        else {
            taskName = "nieznane zadanie";
            return taskName;
        }
    }
}

package com.example.rfida;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Objects;

public class TasksRVAdapter extends RecyclerView.Adapter<TasksRVAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<TasksC> tasksCArrayList;
    private Context context;

    // creating constructor for our adapter class
    public TasksRVAdapter(ArrayList<TasksC> tasksCArrayList, Context context) {
        this.tasksCArrayList = tasksCArrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public TasksRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        TasksC tasksC = tasksCArrayList.get(position);
        holder.taskNumber.setText(tasksC.getTaskNumber());
        holder.taskTime.setText(tasksC.getTaskTime());
        holder.taskDate.setText(tasksC.getTaskDate());
        holder.taskName.setText(tasksC.getTaskName());
    }

    @Override
    public int getItemCount() {
        // zwraca wielkość tablicy danych
        return tasksCArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        // tworzenie zmiennych dla widoków.
        private final TextView taskNumber;
        private final TextView taskTime;
        private final TextView taskDate;
        private final TextView taskName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializacja widoków
            taskNumber = itemView.findViewById(R.id.idTVCard);
            taskTime = itemView.findViewById(R.id.idTVTaskTime);
            taskDate = itemView.findViewById(R.id.idTVTaskDate);
            taskName = itemView.findViewById(R.id.idTVTaskName);
        }
    }
}
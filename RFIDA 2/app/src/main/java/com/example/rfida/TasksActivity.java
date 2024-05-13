package com.example.rfida;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TasksActivity extends AppCompatActivity {

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView taskRV;
    private ArrayList<TasksC> tasksCArrayList;
    private TasksRVAdapter tasksRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        taskRV = findViewById(R.id.idRVTasks);
        loadingPB = findViewById(R.id.idProgressBar);
        db = FirebaseFirestore.getInstance();
        tasksCArrayList = new ArrayList<>();
        taskRV.setHasFixedSize(true);
        taskRV.setLayoutManager(new LinearLayoutManager(this));
        tasksRVAdapter = new TasksRVAdapter(tasksCArrayList, this);
        taskRV.setAdapter(tasksRVAdapter);
        db.collection("historia_1").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                TasksC c = d.toObject(TasksC.class);
                                tasksCArrayList.add(c);
                            }
                            tasksRVAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(TasksActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.getMessage();
                        Toast.makeText(TasksActivity.this, "Fail to get the data."+errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Auth token lookup failed" + errorMessage, e);
                    }
                });
    }
}
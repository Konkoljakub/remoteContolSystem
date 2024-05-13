package com.example.rfida;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    private String taskNumber, taskTime, taskDate, taskName;
    private Button viewTasksBtn;
    // creating a variable
    // for firebasefirestore.
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        // inicjalizacja edittext i button
        viewTasksBtn = findViewById(R.id.idBtnViewTasks);
        // dodanie onclick listener w celu zobaczenia danych w kolejnej aktywnosci
        viewTasksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(MainActivity.this,TasksActivity.class);
                startActivity(i);
            }
        });
    }
}
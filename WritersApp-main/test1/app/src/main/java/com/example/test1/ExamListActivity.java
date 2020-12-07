package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ExamListActivity extends AppCompatActivity {
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<exam> list;
    MyAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState){
            ActionBar actionBar;
            actionBar = getSupportActionBar();

            // Define ColorDrawable object and parse color
            // using parseColor method
            // with color hash code as its parameter
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#3889f4"));
            //getActionBar().setTitle("Hello world App");
            getSupportActionBar().setTitle("Write4H");

            // Set BackgroundDrawable
            actionBar.setBackgroundDrawable(colorDrawable);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_exam_list);
            recyclerView=(RecyclerView) findViewById(R.id.RecycleView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            list = new ArrayList<exam>();
            reference= FirebaseDatabase.getInstance().getReference().child("exam");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        exam e = snapshot1.getValue(exam.class);
                        list.add(e);
                    }
                    System.out.println(list);
                    adapter = new MyAdapter(ExamListActivity.this
                            ,list);
                    recyclerView.setAdapter(null);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //System.out.println(list);
                    //startActivity(getIntent());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
}
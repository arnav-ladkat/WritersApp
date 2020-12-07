package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_writer extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference, reference1;
    private String userID;
    EditText updateEditTextNumber3;
    EditText updateTextPersonName3;
    EditText updateTextEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_update_writer2);
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        updateEditTextNumber3 = (EditText) findViewById(R.id.updateEditTextNumber3);
        findViewById(R.id.submit1).setOnClickListener(this);
        updateTextPersonName3 = (EditText) findViewById(R.id.updateTextPersonName3);
        updateTextEmailAddress = (EditText) findViewById(R.id.updateTextEmailAddress);
        reference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if (userprofile != null) {
                    String name = userprofile.name;
                    String email = userprofile.email;
                    String phone = userprofile.phone;

                    updateTextPersonName3.setText(name);
                    updateTextEmailAddress.setText(email);
                    updateEditTextNumber3.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(update_writer.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit1:
                final String phone = updateEditTextNumber3.getText().toString().trim();
                reference.child("users").child(userID).child("phone").setValue(phone);
                Toast.makeText(this, "Successfully Updated.You Can check your profile.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, writer_activity.class));
                break;
        }
    }
}
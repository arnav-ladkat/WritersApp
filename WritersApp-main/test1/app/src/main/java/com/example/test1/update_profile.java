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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class update_profile extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference,reference1;
    private String userID;
    EditText updateEditTextNumber2;
    EditText updateTextTextPersonName3;
    EditText updateTextTextEmailAddress;

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
        setContentView(R.layout.activity_update_profile);
        reference=FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        updateEditTextNumber2 = (EditText) findViewById(R.id.updateEditTextNumber2);
        findViewById(R.id.submit).setOnClickListener(this);
        updateTextTextPersonName3 = (EditText) findViewById(R.id.updateTextTextPersonName3);
        updateTextTextEmailAddress = (EditText) findViewById(R.id.updateTextTextEmailAddress);


        reference.child("client").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userhelper userprofile = snapshot.getValue(userhelper.class);
                if(userprofile!=null)
                {
                    String name = userprofile.name;
                    String email = userprofile.email;
                    String phone = userprofile.phone;

                    updateTextTextPersonName3.setText(name);
                    updateTextTextEmailAddress.setText(email);
                    updateEditTextNumber2.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(update_profile.this,"Something wrong happened",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                final String phone = updateEditTextNumber2.getText().toString().trim();


                reference.child("client").child(userID).child("phone").setValue(phone);
                Toast.makeText(this,"Successfully Updated.You Can check your profile.",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, client_profile.class));
                break;
        }
    }
}
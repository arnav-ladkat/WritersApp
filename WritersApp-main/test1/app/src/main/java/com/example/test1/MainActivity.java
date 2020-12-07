package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText ediTextEmailAddress,editTextTextPassword;
    ProgressBar progressBar;

    private ToggleButton toggleButton;
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
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        ediTextEmailAddress=(EditText) findViewById(R.id.editTextEmailAddress);
        editTextTextPassword=(EditText) findViewById(R.id.editTextTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        findViewById(R.id.textView2).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
    }
    private void userLogin(){
        String email = ediTextEmailAddress.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            ediTextEmailAddress.setError("Email is required");
            ediTextEmailAddress.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ediTextEmailAddress.setError("Please enter a valid email");
            ediTextEmailAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextTextPassword.setError("Password is required");
            editTextTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextTextPassword.setError("Minimum length of password should be 6");
            editTextTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Boolean ToggleButtonState = toggleButton.isChecked();
        if(ToggleButtonState) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userId;
                        DatabaseReference reference;
                        reference = FirebaseDatabase.getInstance().getReference("users");
                        if(user.isEmailVerified()) {
                            userId=user.getUid();
                            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userprofile = snapshot.getValue(User.class);
                                    {
                                        if(userprofile!=null)
                                        {
                                            finish();
                                            Intent intent = new Intent(MainActivity.this, writer_activity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            FirebaseMessaging.getInstance().subscribeToTopic("writer")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            String msg = "Writer";
                                                            if (!task.isSuccessful()) {
                                                                msg = "Notif Subscription failed";
                                                            }
                                                            Log.d("TAG", "msg");
                                                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"You do not belong to this collection!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                        else{
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this,"Check your email to verify your account and then Log In.",Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userId;
                        DatabaseReference reference;
                        reference = FirebaseDatabase.getInstance().getReference("client");
                        if(user.isEmailVerified()) {
                            userId=user.getUid();
                            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userprofile = snapshot.getValue(User.class);
                                    {
                                        if(userprofile!=null)
                                        {
                                            finish();
                                            Intent intent = new Intent(MainActivity.this, client_profile.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            FirebaseMessaging.getInstance().subscribeToTopic("client")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            String msg = "Client";
                                                            if (!task.isSuccessful()) {
                                                                msg = "Notif Subscription failed";
                                                            }
                                                            Log.d("TAG", "msg");
                                                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"You do not belong to this collection!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                        else {
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView2:
                startActivity(new Intent(this,writer.class));
                break;

            case R.id.login:
                userLogin();
                break;
        }
    }
}
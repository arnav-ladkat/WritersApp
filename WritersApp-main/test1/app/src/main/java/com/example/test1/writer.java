package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class writer extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail,editTextPassword,editTextPhone,editTextTextPersonName;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private ToggleButton toggleButton2;

    ProgressBar progressBar2;
    private FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_client_reg);

        editTextTextPersonName = (EditText) findViewById(R.id.editTextTextPersonName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonSignUp).setOnClickListener(this);

    }



    private void register(){
        final String email = editTextEmail.getText().toString().trim();
        final String name = editTextTextPersonName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        if(name.isEmpty())
        {
            editTextTextPersonName.setError("Name is required");
            editTextTextPersonName.requestFocus();
        }
        if(phone.isEmpty())
        {
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return;
        }
        if(phone.length()<10)
        {
            editTextPhone.setError("Minimum length of phone number should be 10");
            editTextPhone.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(phone).matches())
        {
            editTextPhone.setError("Please enter a valid phone number");
            editTextPhone.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar2.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Boolean ToggleButtonState = toggleButton2.isChecked();
                    rootNode = FirebaseDatabase.getInstance();
                    if (ToggleButtonState) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.sendEmailVerification();
                        reference = rootNode.getReference("users");
                        userhelper helper = new userhelper(name, email, phone, password);
                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(helper);
                        finish();
                        Toast.makeText(writer.this, "Check your email to verify your account and then Log In.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(writer.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.sendEmailVerification();
                        reference = rootNode.getReference("client");
                        userhelper helperclass = new userhelper(name, email, phone, password);
                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(helperclass);
                        finish();
                        Toast.makeText(writer.this, "Check your email to verify your account and then Log In.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(writer.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonSignUp:
                register();
                break;

        }
    }
}
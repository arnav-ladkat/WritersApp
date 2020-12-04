package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClientandExamDetails extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference,examref;
    private String userID;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get the bundle


//Extract the data…

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientand_exam_details);
        findViewById(R.id.contact).setOnClickListener(this);
        //findViewById(R.id.logout2).setOnClickListener(this);
        //user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("client");
        examref = FirebaseDatabase.getInstance().getReference("exam");
        Bundle mybundle = getIntent().getExtras();

        if(mybundle.getString("One")!=null){
            userID = mybundle.getString("One");
        }
        else{
            userID="FnXMjoebSpRmXb7nkYEE9u8dIxW2";
        }

        System.out.println(userID);
        user = FirebaseAuth.getInstance().getCurrentUser();
        examref.child(userID).child("book_status").setValue("1");
        examref.child(userID).child("accepted_by").setValue(user.getUid().toString());
        //userID=clubkey[0];
        final TextView textViewFullName = (TextView) findViewById(R.id.clientTextViewFullName);
        final TextView textViewEmailAddress = (TextView) findViewById(R.id.clientTextViewEmailAddress);
        final TextView textViewPhoneNumber = (TextView) findViewById(R.id.clientTextViewPhoneNumber);
        final TextView examsub = (TextView) findViewById(R.id.examsub);
        final TextView examdate= (TextView) findViewById(R.id.examdate);
        final TextView examad= (TextView) findViewById(R.id.examad);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if(userprofile!=null){
                    String name = userprofile.name;
                    String email = userprofile.email;
                    phone = userprofile.phone;
                    textViewFullName.setText(name);
                    textViewEmailAddress.setText(email);
                    textViewPhoneNumber.setText(phone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClientandExamDetails.this,"Something wrong happened",Toast.LENGTH_LONG).show();
            }
        });


        examref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                formhelper exampro = snapshot.getValue(formhelper.class);
                if(exampro!=null){
                    String name = exampro.getName();
                    String address = exampro.getAddress();
                    String date = exampro.getDate();
                    String time = exampro.getTime();
                    examsub.setText(name);
                    examdate.setText(date+" "+time);
                    examad.setText(address);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact:
                Toast.makeText(ClientandExamDetails.this,"you clicked this button",Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+91"+phone+"&text=I%20am%20intrested%20to%20be%20writer%20for%20the%20Exam"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                finish();
                break;
        }
    }
}
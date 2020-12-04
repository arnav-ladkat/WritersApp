package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class client_profile extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference examref;
    private DatabaseReference writerref;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.logout2).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("client");
        examref = FirebaseDatabase.getInstance().getReference("exam");
        writerref = FirebaseDatabase.getInstance().getReference("users");

        userID=user.getUid();
        final String[] writerUid = new String[1];

        final TextView textViewFullName = (TextView) findViewById(R.id.textViewFullName);
        final TextView textViewEmailAddress = (TextView) findViewById(R.id.textViewEmailAddress);
        final TextView textViewPhoneNumber = (TextView) findViewById(R.id.textViewPhoneNumber);
        final TextView status = (TextView) findViewById(R.id.status);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if(userprofile!=null){
                    String name = userprofile.name;
                    String email = userprofile.email;
                    String phone = userprofile.phone;

                    textViewFullName.setText(name);
                    textViewEmailAddress.setText(email);
                    textViewPhoneNumber.setText(phone);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(client_profile.this,"Something wrong happened",Toast.LENGTH_LONG).show();
            }
        });

        examref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                formhelper exampro = snapshot.getValue(formhelper.class);
                if(exampro!=null){
                    String name = exampro.getName();
                    String email = exampro.getAddress();
                    String date = exampro.getDate();
                    String time = exampro.getTime();
                    writerUid[0] = exampro.getAccepted_by();
                    System.out.println(name +"\n"+email+"\n"+ writerUid[0] +"\n");
                    if(writerUid[0].equals("None"))
                    {
                        status.setText("Your request for "+name+" on "+date+" has been posted.");

                    }
                    writerref.child(writerUid[0]).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userprofile = snapshot.getValue(User.class);
                            if(userprofile!=null){
                                String name = userprofile.name;
                                String email = userprofile.email;
                                String phone = userprofile.phone;
                                String stat = name+" has accepted your request.This is their phone number "+phone;
                                status.setText(stat);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    Date d=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
                    String currentDateTimeString = sdf.format(d);
                    //System.out.println(currentDateTimeString);

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = df.format(currentTime);
                   // System.out.println(formattedDate);
                   // System.out.println("database time "+time);
                   // System.out.println("database date "+date);

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    Date strDate = null;
                    Date strDate2 = null;
                    try {
                        strDate = sdf1.parse(date);
                        strDate2 = sdf1.parse(formattedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (strDate2.after(strDate)) {
                        examref.child(userID).removeValue();
                        //finish();
                        //startActivity(getIntent());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                startActivity(new Intent(this, form.class));
                break;
            case R.id.logout2:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(client_profile.this,MainActivity.class));
                break;
            case R.id.update:
                startActivity(new Intent(this, update_profile.class));
                break;
            case R.id.delete:
                examref = FirebaseDatabase.getInstance().getReference("exam");
                examref.child(userID).removeValue();
                Toast.makeText(this,"Your Request for exam has been deleted/cancelled",Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
                break;
        }
    }
}
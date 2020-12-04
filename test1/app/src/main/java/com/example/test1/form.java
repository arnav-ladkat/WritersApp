package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class form extends AppCompatActivity {
    EditText dateformat;
    EditText editTextTime;
    int hour,min;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    DatePickerDialog.OnDateSetListener setListener;
    EditText editTextTextPersonName2;
    EditText editTextTextPostalAddress;
    EditText editTextLanguage;
    EditText editTextNumber;
    Button bookbutton;
     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        editTextTime = (EditText) findViewById(R.id.editTextTime);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("client");
        userID=user.getUid();

        editTextTextPersonName2 = (EditText) findViewById(R.id.editTextTextPersonName2);
        editTextLanguage = (EditText) findViewById(R.id.editTextLanguage);
        editTextTime = (EditText) findViewById(R.id.editTextTime);
        editTextTextPostalAddress = (EditText) findViewById(R.id.editTextTextPostalAddress);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        bookbutton = findViewById(R.id.bookbtn);

        bookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("exam");

                String name = editTextTextPersonName2.getText().toString();
                String language = editTextLanguage.getText().toString();
                String date = dateformat.getText().toString();
                String time = editTextTime.getText().toString();
                String address = editTextTextPostalAddress.getText().toString();
                String amount = editTextNumber.getText().toString();

                if(name.isEmpty())
                {
                    editTextTextPersonName2.setError("Name is required");
                    editTextTextPersonName2.requestFocus();
                }
                if(language.isEmpty())
                {
                    editTextLanguage.setError("Language is required");
                    editTextLanguage.requestFocus();
                }
                if(date.isEmpty())
                {
                    dateformat.setError("date is required");
                    dateformat.requestFocus();
                    return;
                }
                if(time.isEmpty())
                {
                    editTextTime.setError("Time  is required");
                    editTextTime.requestFocus();
                    return;
                }

                if(address.isEmpty())
                {
                    editTextTextPostalAddress.setError("address is required");
                    editTextTextPostalAddress.requestFocus();
                    return;
                }
                if (amount.isEmpty()) {
                    editTextNumber.setError("Amount is required");
                    editTextNumber.requestFocus();
                    return;
                }

                formhelper fh = new formhelper(name,language,date,time,address,amount,"None","0");
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(fh);
                finish();
                Toast.makeText(form.this, "Successfully booked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(form.this, client_profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);




            }
        });
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        form.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                min = minute;
                                String time = hour +":"+ min;
                                //Calendar cal = Calendar.getInstance();
                                //cal.set(0,0,0,hour,min);
                                SimpleDateFormat f24hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date=f24hours.parse(time);
                                    SimpleDateFormat f12hours=new SimpleDateFormat("hh:mm aa");
                                    editTextTime.setText(f12hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },12,0,false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });


        dateformat=(EditText) findViewById(R.id.dateformatID);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateformat.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog( form.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dateformat.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }









}
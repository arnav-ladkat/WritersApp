package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.font.NumericShaper;
import java.io.StringWriter;

public class writer_activity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer_profile);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.myrequests).setOnClickListener(this);
        findViewById(R.id.update1).setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID=user.getUid();
        final TextView textViewName = (TextView) findViewById(R.id.textViewName);
        final TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        final TextView textViewPhone = (TextView) findViewById(R.id.textViewPhone);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if(userprofile!=null){
                    String name = userprofile.name;
                    String email = userprofile.email;
                    String phone = userprofile.phone;

                    textViewName.setText(name);
                    textViewEmail.setText(email);
                    textViewPhone.setText(phone);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(writer_activity.this,"Something wrong happened",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(writer_activity.this,MainActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(writer_activity.this,ExamListActivity.class));
                break;
            case R.id.update1:
                startActivity(new Intent(writer_activity.this,update_writer.class));
                break;
            case R.id.myrequests:
                final String[] clubkey = new String[1];
                DatabaseReference examref = FirebaseDatabase.getInstance().getReference();
                examref.child("exam")
                        .orderByChild("accepted_by")
                        .equalTo(userID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    clubkey[0] = childSnapshot.getKey();}
                                if(clubkey[0]!=null){
                                    Intent intent = new Intent(writer_activity.this, ClientandExamDetails.class);
                                    Bundle mybundle = new Bundle();
                                    String  clubkeyf=clubkey[0];
                                    mybundle.putString("One",clubkeyf.toString());
                                    intent.putExtras(mybundle);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(writer_activity.this,"You haven't accepted Any Exams",Toast.LENGTH_LONG).show();
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });

                break;
        }
    }
}
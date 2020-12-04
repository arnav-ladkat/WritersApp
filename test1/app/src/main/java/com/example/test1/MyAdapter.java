package com.example.test1;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<exam> exams;
    DatabaseReference reference;
    final String[] clubkey = new String[1];
    FirebaseUser user;
    public MyAdapter(Context c,ArrayList<exam> e)
    {
        context=c;
        exams=e;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_exam_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText("Name :"+exams.get(position).getName());
        holder.Language.setText("Language :"+exams.get(position).getLanguage());
        holder.Date.setText("Date :"+exams.get(position).getDate());
        holder.Time.setText("Time :"+exams.get(position).getTime());
        holder.Amount.setText("Amount :"+exams.get(position).getAmount());
        holder.accept.setVisibility(View.VISIBLE);
        holder.address.setVisibility(View.VISIBLE);
        holder.onClick(position);
    }


    @Override
    public int getItemCount() {
        return exams.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,Language,Date,Time,Amount;
        Button accept;
        Button address;


        public MyViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.Name);
            Language = (TextView) itemView.findViewById(R.id.Language);
            Date = (TextView) itemView.findViewById(R.id.Date);
            Time = (TextView) itemView.findViewById(R.id.Time);
            Amount = (TextView) itemView.findViewById(R.id.Amount);
            accept = (Button) itemView.findViewById(R.id.Accept);
            address = (Button) itemView.findViewById(R.id.address);

        }
        public void onClick(final int position)
        {


            accept.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    final int[] flag = {0};
                        reference = FirebaseDatabase.getInstance().getReference();
                        System.out.println(exams);
                        reference.child("exam")
                                .orderByChild("name")
                                .equalTo(exams.get(position).getName())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            clubkey[0] = childSnapshot.getKey();
                                        }
                                        String var=exams.get(position).getBook_status();

                                        if(var!="1") {
                                            flag[0] =1;
                                            /*user = FirebaseAuth.getInstance().getCurrentUser();
                                            reference.child("exam").child(clubkey[0]).child("book_status").setValue("1");
                                            reference.child("exam").child(clubkey[0]).child("accepted_by").setValue(user.getUid().toString());*/
                                            Intent intent = new Intent(context, ClientandExamDetails.class);
                                            Bundle mybundle = new Bundle();
                                            mybundle.putString("One", clubkey[0].toString());
                                            intent.putExtras(mybundle);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            context.startActivity(intent);
                                            //notifyItemChanged(position);


                                        }
                                        else
                                        {
                                            Toast.makeText(context, "This request has already been accepted.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }

                                });


                        ((Activity)context).finish();


                }
            });



            address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"you clicked this button",Toast.LENGTH_SHORT).show();
                    String ad = exams.get(position).getAddress();
                    Intent intent = new Intent(context, MapsActivity.class);
                    Bundle mybundle = new Bundle();
                    mybundle.putString("tmkc",ad);
                    intent.putExtras(mybundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
        }
    }
}


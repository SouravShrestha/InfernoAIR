package com.example.souravshrestha.newsbullets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.souravshrestha.newsbullets.R.drawable.error;

public class TextNews extends AppCompatActivity {

    DatabaseReference myRef;
    RecyclerView textViewNews;
    List<String> bodyList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_news);

        myRef = FirebaseDatabase.getInstance().getReference().child("TextNews");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    Iterable<DataSnapshot> langChildren11 = parentLang.getChildren();
                    for (DataSnapshot parentLang1 : langChildren11) {

                        if(parentLang1.getKey().toString().equalsIgnoreCase("body")){
                            bodyList.add(parentLang1.getValue().toString());
                        }
                        if(parentLang1.getKey().toString().equalsIgnoreCase("title")){
                            titleList.add(parentLang1.getValue().toString());
                        }
                    }
                }

                loadContent();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }


    public void loadContent(){



    }
}

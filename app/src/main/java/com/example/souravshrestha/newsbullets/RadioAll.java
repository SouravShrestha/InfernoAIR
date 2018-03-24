package com.example.souravshrestha.newsbullets;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RadioAll extends AppCompatActivity {


    private DrawerLayout mDraw;
    private NavigationView mNav;
    RecyclerView mRadioList;
    private DatabaseReference mDatabase;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_all);
        mNav = (NavigationView) findViewById(R.id.navBar_radio);
        mDraw =(DrawerLayout) findViewById(R.id.drawLayout_radio);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Stations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRadioList = (RecyclerView)findViewById(R.id.radioAll_rec);
        mRadioList.setHasFixedSize(true);
        mRadioList.setLayoutManager(new GridLayoutManager(this,3));
        loadRadioList();
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        Intent homeGo = new Intent(RadioAll.this,MainActivity.class);
                        startActivity(homeGo);
                        finish();
                        break;
                    case R.id.settings :
                        Toast.makeText(RadioAll.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.aboutUs:
                        Toast.makeText(RadioAll.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                }
                mNav.setCheckedItem(item.getItemId());
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadRadioList() {

        FirebaseRecyclerAdapter<RadioReference,RadioAll.RadioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RadioReference, RadioAll.RadioViewHolder>(
                RadioReference.class,R.layout.radio_card,RadioAll.RadioViewHolder.class,mDatabase.orderByChild("title")
        ) {
            @Override
            protected void populateViewHolder(final RadioAll.RadioViewHolder viewHolder, final RadioReference model, final int position) {
                viewHolder.setDetailsRadio(getApplicationContext(),model.getTitle(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RadioAll.this,model.getUrl().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        mRadioList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RadioViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RadioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetailsRadio(Context con, String title_name, String img1){
            TextView title = (TextView)mView.findViewById(R.id.txt_radio);
            ImageView img = (ImageView)mView.findViewById(R.id.img_radio);
            Glide.with(con).load(img1).into(img);
            title.setText(title_name);
        }

    }


}

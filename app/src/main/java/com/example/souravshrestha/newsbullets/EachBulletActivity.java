package com.example.souravshrestha.newsbullets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;

import nl.changer.audiowife.AudioWife;

public class EachBulletActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView mEachBulletList;
    TextView mTextHead;
    String title,head;
    String dateSelected="";
    Toolbar mActionNav;
    HashSet<String> dropItems = new HashSet<String>();
    Spinner dropDown;
    static String curLang;
    ArrayList<String> validName = new ArrayList<>();
    private DrawerLayout mDraw;
    private NavigationView mNav;
    private ActionBarDrawerToggle mToggle;
    TextView selectDate;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference mData,mData2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_bullet);
        mNav = (NavigationView) findViewById(R.id.navBar1);
        mActionNav = (Toolbar)findViewById(R.id.mNav);
        setSupportActionBar(mActionNav);
        mDraw =(DrawerLayout) findViewById(R.id.drawLayout2);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        selectDate = (TextView)findViewById(R.id.datePick);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = getIntent().getExtras().getString("bulletName");
        curLang = getIntent().getExtras().getString("curLang");
        head = getIntent().getExtras().getString("bulletTitle");
        mTextHead = (TextView)findViewById(R.id.tHeadList);
        dropDown = (Spinner)findViewById(R.id.spin);
        dropDown.setOnItemSelectedListener(this);
        mTextHead.setText(head);
        mData = FirebaseDatabase.getInstance().getReference("NewsBullets");
        mData2 = mData.child(title).child("List");
        mEachBulletList = (RecyclerView)findViewById(R.id.eachBulletListRecycle);
        mEachBulletList.setHasFixedSize(true);
        mEachBulletList.setLayoutManager(new LinearLayoutManager(this));

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EachBulletActivity.this,
                        mDateSetListener,
                        year,month,day);
                datePickerDialog.show();
            }
        });

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        Intent homeGo = new Intent(EachBulletActivity.this,MainActivity.class);
                        startActivity(homeGo);
                        finish();
                        break;
                    case R.id.settings :
                        Toast.makeText(EachBulletActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.aboutUs:
                        Toast.makeText(EachBulletActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                }
                mNav.setCheckedItem(item.getItemId());
                return false;
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String monthName=" ";

                switch (month){
                    case 0:
                        monthName = "January";
                        break;
                    case 1:
                        monthName = "February";
                        break;
                    case 2:
                        monthName = "March";
                        break;
                    case 3:
                        monthName = "April";
                        break;
                    case 4:
                        monthName = "May";
                        break;
                    case 5:
                        monthName = "June";
                        break;
                    case 6:
                        monthName = "July";
                        break;
                    case 7:
                        monthName = "August";
                        break;
                    case 8:
                        monthName = "September";
                        break;
                    case 9:
                        monthName = "October";
                        break;
                    case 10:
                        monthName = "November";
                        break;
                    case 11:
                        monthName = "December";
                        break;
                }
                selectDate.setText(monthName+" "+day+", "+year);
                String finalSelectedDate = String.valueOf(day)+" "+String.valueOf(month+1)+" "+String.valueOf(year);
                dateSelected = finalSelectedDate;
                loadBullet1(dropDown.getSelectedItem().toString(),dateSelected);

            }
        };

        mData2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dropItems.clear();
                Iterable<DataSnapshot> listChild1 = dataSnapshot.getChildren();
                for (DataSnapshot childTwo : listChild1) {
                    dropItems.add(childTwo.child("lang").getValue().toString());
                }

                ArrayList<String> dropItemsList = new ArrayList<>(dropItems);
                Collections.sort(dropItemsList);
                for(int i =0 ;i<dropItemsList.size();i++){
                    String x = dropItemsList.get(i);
                    if(x.equalsIgnoreCase(curLang)){
                        int index = dropItemsList.indexOf(x);
                        dropItemsList.remove(index);
                        dropItemsList.add(0,x);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EachBulletActivity.this, android.R.layout.simple_spinner_item,dropItemsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EachBulletActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBullet1(final String lan,final String dateSelected) {

        if(dateSelected !=null && dateSelected.length()>0) {

            mData2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    validName.clear();
                    Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                    for (DataSnapshot parentLang : langChildren1) {
                        if ((parentLang.child("lang").getValue().toString()).equals(lan) && parentLang.child("date").getValue().toString().equals(dateSelected)) {
                            validName.add(parentLang.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(EachBulletActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            mData2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    validName.clear();
                    Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                    for (DataSnapshot parentLang : langChildren1) {
                        if ((parentLang.child("lang").getValue().toString()).equals(lan)) {
                            validName.add(parentLang.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(EachBulletActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        FirebaseRecyclerAdapter<EachBulletContent,EachBulletActivity.BulletListViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<EachBulletContent,EachBulletActivity.BulletListViewHolder>(
                EachBulletContent.class,R.layout.each_list_content,EachBulletActivity.BulletListViewHolder.class,mData2
        ) {
            @Override
            protected void populateViewHolder(final EachBulletActivity.BulletListViewHolder viewHolder, final EachBulletContent model, int position) {

                if(validName.contains((getRef(position).getKey()))) {
                    validName.remove((getRef(position).getKey()));
                    viewHolder.setDetails(getApplicationContext(),model.getUrl(),EachArchiveActivity.properDate(model.getDate()));
                }else{
                    viewHolder.itemView.setVisibility(View.GONE);
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
            }
        };

        mEachBulletList.setAdapter(firebaseRecyclerAdapter1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        loadBullet1(parent.getItemAtPosition(pos).toString(),this.dateSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class BulletListViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BulletListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(final Context con,final String url,String textDate){
            Button mPlayEach = (Button) mView.findViewById(R.id.eachBulletButton);
            TextView mTextBullet = (TextView)mView.findViewById(R.id.bullet_date);
            mTextBullet.setText(textDate);
            mPlayEach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(con,url,Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}

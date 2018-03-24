package com.example.souravshrestha.newsbullets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class RegionalBulletins extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView mTitle;
    RecyclerView regionalListView;
    Spinner dropDownRegional;
    String dateSelected="";
    TextView selectDate;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    private DrawerLayout mDraw;
    private NavigationView mNav;
    private ActionBarDrawerToggle mToggle;
    String title="";
    String region;
    long count;
    DatabaseReference mDatabaseRegional;
    HashSet<String> dropItemsRegional = new HashSet<String>();
    ArrayList<String> validName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regional_bulletins);

        regionalListView = (RecyclerView)findViewById(R.id.regionalContentView);
        mNav = (NavigationView) findViewById(R.id.navBar3);
        mDraw =(DrawerLayout) findViewById(R.id.regional_bulletins);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        regionalListView.setHasFixedSize(true);
        regionalListView.setLayoutManager(new LinearLayoutManager(this));
        String time = getIntent().getStringExtra("title");
        String x[] = time.split(" ");
        title = x[0];
        selectDate = (TextView)findViewById(R.id.tDateRegional);
        region = getIntent().getStringExtra("region");
        mDatabaseRegional = FirebaseDatabase.getInstance().getReference().child("RegionalBulletins").child(title);
        dropDownRegional = (Spinner)findViewById(R.id.spinnerRegionalList);
        dropDownRegional.setOnItemSelectedListener(this);
        mTitle = (TextView)findViewById(R.id.title_rnd);
        mTitle.setText(region+"\n"+time);

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        Intent homeGo = new Intent(RegionalBulletins.this,MainActivity.class);
                        startActivity(homeGo);
                        finish();
                        break;
                    case R.id.settings :
                        Toast.makeText(RegionalBulletins.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.aboutUs:
                        Toast.makeText(RegionalBulletins.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                }
                mNav.setCheckedItem(item.getItemId());
                return false;
            }
        });

        mDatabaseRegional.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count =  dataSnapshot.getChildrenCount();
                dropItemsRegional.clear();
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    Iterable<DataSnapshot> langChildren11 = parentLang.getChildren();
                    for (DataSnapshot parentLang1 : langChildren11) {
                        if(parentLang1.getKey().toString().equalsIgnoreCase("title"));{
                            if(parentLang1.getValue().toString().equalsIgnoreCase(region)){
                                dropItemsRegional.add(parentLang.child("lang").getValue().toString());
                            }
                        }
                    }
                }

                ArrayList<String> dropItemsListRegional = new ArrayList<>(dropItemsRegional);
                ArrayAdapter<String> adapterRegional = new ArrayAdapter<String>(RegionalBulletins.this, android.R.layout.simple_spinner_item,dropItemsListRegional);
                adapterRegional.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDownRegional.setAdapter(adapterRegional);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RegionalBulletins.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegionalBulletins.this,
                        mDateSetListener,
                        year,month,day);
                datePickerDialog.show();
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
                loadRegionList(dropDownRegional.getSelectedItem().toString(),dateSelected);
            }
        };

    }

    private void loadRegionList(final String lan,final String date) {


        if(dateSelected !=null && dateSelected.length()>0) {
            mDatabaseRegional.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    validName.clear();
                    Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                    for (DataSnapshot parentLang : langChildren1) {
                        if (parentLang.child("title").getValue() != null) {
                            if (parentLang.child("title").getValue().toString().equalsIgnoreCase(region)) {
                                if (parentLang.child("lang").getValue() != null) {
                                    if (parentLang.child("lang").getValue().toString().equalsIgnoreCase(lan)  && parentLang.child("date").getValue().toString().equals(date)) {
                                        validName.add(parentLang.getKey().toString());
                                    }
                                }
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(RegionalBulletins.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            mDatabaseRegional.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    validName.clear();
                    Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                    for (DataSnapshot parentLang : langChildren1) {
                        if (parentLang.child("title").getValue() != null) {
                            if (parentLang.child("title").getValue().toString().equalsIgnoreCase(region)) {
                                if (parentLang.child("lang").getValue() != null) {
                                    if (parentLang.child("lang").getValue().toString().equalsIgnoreCase(lan)) {
                                        validName.add(parentLang.getKey().toString());
                                    }
                                }
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(RegionalBulletins.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        FirebaseRecyclerAdapter<RegionalDetails,RegionalBulletins.RegionalListViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<RegionalDetails,RegionalBulletins.RegionalListViewHolder>(
            RegionalDetails.class,R.layout.each_regional_content,RegionalBulletins.RegionalListViewHolder.class,mDatabaseRegional.limitToFirst((int)count-2)
    ) {
        @Override
        protected void populateViewHolder(final RegionalBulletins.RegionalListViewHolder viewHolder,final RegionalDetails model, int position) {
            if(validName.contains((getRef(position).getKey()))) {
                viewHolder.setDetails(getApplicationContext(),EachArchiveActivity.properDate(model.getDate()),model.getUrl());
            }else{
                viewHolder.itemView.setVisibility(View.GONE);
                viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
            }
        }
    };
        regionalListView.setAdapter(firebaseRecyclerAdapter1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        loadRegionList(parent.getItemAtPosition(pos).toString(),this.dateSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class RegionalListViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RegionalListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(final Context con,final String date,final String url){
            Button mPlayEach = (Button) mView.findViewById(R.id.eachBulletButton);
            TextView dateL = (TextView) mView.findViewById(R.id.tDateRegional);
            dateL.setText(date);
            mPlayEach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(con,url,Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}

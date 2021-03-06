package com.example.souravshrestha.newsbullets;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
    static SharedPreferences prefs=null;
    Toolbar mActionNav;
    static String title="";
    static String time;
    static String region;
    long count;
    DatabaseReference mDatabaseRegional;
    HashSet<String> dropItemsRegional = new HashSet<String>();
    ArrayList<String> validName = new ArrayList<>();
    public static SharedPreferences shared;
    public static ArrayList<String> arrPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regional_bulletins);

        regionalListView = (RecyclerView)findViewById(R.id.regionalContentView);
        mNav = (NavigationView) findViewById(R.id.navBar3);
        mDraw =(DrawerLayout) findViewById(R.id.regional_bulletins);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mActionNav = (Toolbar)findViewById(R.id.mNav);
        setSupportActionBar(mActionNav);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        regionalListView.setHasFixedSize(true);
        regionalListView.setLayoutManager(new LinearLayoutManager(this));
        time = getIntent().getStringExtra("title");
        String x[] = time.split(" ");
        title = x[0];
        selectDate = (TextView)findViewById(R.id.tDateRegional);
        region = getIntent().getStringExtra("region");
        mDatabaseRegional = FirebaseDatabase.getInstance().getReference().child("RegionalBulletins").child(title);
        dropDownRegional = (Spinner)findViewById(R.id.spinnerRegionalList);
        dropDownRegional.setOnItemSelectedListener(this);
        mTitle = (TextView)findViewById(R.id.title_rnd);
        mTitle.setText(region+"\n"+time);


        shared = getSharedPreferences("App_settings", MODE_PRIVATE);
        if(getArrayList("Archive",this)!=null)
            arrPackage = new ArrayList<>(getArrayList("Archive",this));
        else
            arrPackage = new ArrayList<>();

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
                    case R.id.language:
                        break;
                    case R.id.openBrowser:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.newsonair.com/")));
                        break;
                    case R.id.rate:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")));
                        break;
                    case R.id.email:
                        try{
                            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "shrestha.sourav30@gmail.com"));
                            startActivity(intent);
                        }catch(ActivityNotFoundException e){
                        }
                        break;

                    case R.id.aboutUs:
                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(RegionalBulletins.this);
                        dialog.setTitle("Inferno AIR")
                                .setMessage("This Is the National News Broadcast App.\n\nDeveloped By : Team Inferno\n\n \tPlease note that the different channels may take 5 to 20 seconds to start playing, depending on your Internet Speed.")
                                .setColoredCircle(R.color.colorPrimary)
                                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                                .setCancelable(true)
                                .setPositiveButtonText("Take me back to the App")
                                .setPositiveButtonbackgroundColor(R.color.colorPrimary)
                                .setPositiveButtonTextColor(R.color.white).
                                setPositiveButtonClick(new Closure() {
                                    @Override
                                    public void exec() {
                                        dialog.hide();
                                    }
                                });
                        dialog.show();
                }
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

    public static void saveArrayList(ArrayList<String> list, String key,Context con){
        prefs =con.getSharedPreferences("favourites",con.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();
    }

    public static ArrayList<String> getArrayList(String key,Context con){
        SharedPreferences prefs =con.getSharedPreferences("favourites",con.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
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
            final Button mFavorite = (Button)mView.findViewById(R.id.favIconReg);
            if(arrPackage==null) {
                mFavorite.setBackgroundResource(R.drawable.favorite_white);
            }
            else if(!arrPackage.contains(date+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+(region.concat(time)))) {
                mFavorite.setBackgroundResource(R.drawable.favorite_white);
            }
            else {
                mFavorite.setBackgroundResource(R.drawable.favorite_red);
            }

            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getArrayList("Archive",con)==null) {
                        mFavorite.setBackgroundResource(R.drawable.favorite_red);
                        arrPackage.add(date+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+(region.concat(time)));
                    }
                    else if(!getArrayList("Archive",con).contains(date+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+(region.concat(time)))){
                        arrPackage.add((date+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+(region.concat(time))));
                        mFavorite.setBackgroundResource(R.drawable.favorite_red);
                    }
                    else
                    {
                        arrPackage.remove((date+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+(region.concat(time))));
                        mFavorite.setBackgroundResource(R.drawable.favorite_white);
                    }
                    saveArrayList(arrPackage,"Archive",con);
                }
            });
            mPlayEach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {try {
                    MediaPlayerMain.initializeMediaPlayer(url,con);
                    MediaPlayerMain.playIt(url,con);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            });
        }

    }
}

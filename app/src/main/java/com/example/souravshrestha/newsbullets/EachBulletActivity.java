package com.example.souravshrestha.newsbullets;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.bumptech.glide.Glide;
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
import java.util.Collections;
import java.util.HashSet;

public class EachBulletActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView mEachBulletList;
    TextView mTextHead;
    public static String title,head;
    TextView mPlayTitle;
    Button playPause;
    static SharedPreferences prefs=null;
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
    public static SharedPreferences shared;
    public static ArrayList<String> arrPackage;
    private DatabaseReference mData,mData2,mDataLang;
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
        mDataLang = FirebaseDatabase.getInstance().getReference("lang_UI");
        mData2 = mData.child(title).child("List");
        mEachBulletList = (RecyclerView)findViewById(R.id.eachBulletListRecycle);
        mEachBulletList.setHasFixedSize(true);
        mEachBulletList.setLayoutManager(new LinearLayoutManager(this));
        shared = getSharedPreferences("App_settings", MODE_PRIVATE);
        if(getArrayList("Archive",this)!=null)
            arrPackage = new ArrayList<>(getArrayList("Archive",this));
        else
            arrPackage = new ArrayList<>();

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
                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(EachBulletActivity.this);
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
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("bengali")){
            mDataLang.child("Bengali/Morning News").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Toast.makeText(EachBulletActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    mTextHead.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(EachBulletActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

        public void setDetails(final Context con, final String url, final String textDate){
            Button mPlayEach = (Button) mView.findViewById(R.id.eachBulletButton);
            TextView mTextBullet = (TextView)mView.findViewById(R.id.bullet_date);
            mTextBullet.setText(textDate);
            final Button mFavorite = (Button)mView.findViewById(R.id.bFav);
            if(arrPackage==null) {
                mFavorite.setBackgroundResource(R.drawable.favorite_white);
            }
            else if(!arrPackage.contains(textDate+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+head)) {
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
                        arrPackage.add(textDate+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+title);
                    }
                    else if(!getArrayList("Archive",con).contains(textDate+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+title)){
                        arrPackage.add(textDate+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+title);
                        mFavorite.setBackgroundResource(R.drawable.favorite_red);
                    }
                    else
                    {
                        arrPackage.remove((textDate+";"+url+";"+"https://firebasestorage.googleapis.com/v0/b/myair-e9a7d.appspot.com/o/country_wide.jpg?alt=media&token=096cd06a-00d0-45fa-9ff5-5a65b246315d"+";"+title));
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

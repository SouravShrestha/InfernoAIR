package com.example.souravshrestha.newsbullets;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

public class SearchActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    Toolbar mActionNav;
    private DrawerLayout mDraw;
    private NavigationView mNav;
    RecyclerView mBulletList,mRadioList,mArchiveList,mRegionalList;
    private DatabaseReference mDatabase,mDatabaseRadio,mDatabaseRegional,mDatabaseArchive,mDatabaseNewsText;
    RadioButton radioTitle,radioLanguage;
    private ActionBarDrawerToggle mToggle;
    ArrayList<String> validName = new ArrayList<>();
    SegmentedGroup seg;
    EditText mText;
    Button bSearch;
    public static String radioChoice = "";
    ArrayList<String> validNameRnd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mActionNav = (Toolbar) findViewById(R.id.mNav);
        mText = (EditText)findViewById(R.id.editSearch);
        bSearch = (Button)findViewById(R.id.button3);
        setSupportActionBar(mActionNav);
        mNav = (NavigationView) findViewById(R.id.navBar);
        mDraw = (DrawerLayout) findViewById(R.id.drawSearch);
        mToggle = new ActionBarDrawerToggle(this, mDraw, R.string.open, R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        mDatabaseRadio = FirebaseDatabase.getInstance().getReference().child("Stations");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("NewsBullets");
        mDatabaseRegional = FirebaseDatabase.getInstance().getReference().child("RegionalBulletins");
        mDatabaseArchive = FirebaseDatabase.getInstance().getReference().child("Archives");
        mArchiveList = (RecyclerView)findViewById(R.id.archiveView);
        mBulletList = (RecyclerView)findViewById(R.id.bulletsView);
        mBulletList.setHasFixedSize(true);
        mBulletList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mArchiveList.setHasFixedSize(true);
        mArchiveList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        seg = (SegmentedGroup) findViewById(R.id.segmented2);
        radioLanguage = (RadioButton) findViewById(R.id.button1);
        radioTitle = (RadioButton) findViewById(R.id.button2);
        mRadioList = (RecyclerView)findViewById(R.id.radioView_radio);
        mRadioList.setHasFixedSize(true);
        mRadioList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegionalList = (RecyclerView)findViewById(R.id.rndView);
        mRegionalList.setHasFixedSize(true);
        mRegionalList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mNav.setCheckedItem(R.id.home);

        radioTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioChoice = "Title";

            }
        });

        radioLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioChoice = "Language";
            }
        });

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRadioList(radioChoice,mText.getText().toString());
            }
        });

    }

    private void loadRadioList(final String category, final String keyword) {

        if(category.equalsIgnoreCase("Title")){

            FirebaseRecyclerAdapter<RadioReference, MainActivity.RadioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RadioReference, MainActivity.RadioViewHolder>(
                    RadioReference.class, R.layout.radio_card, MainActivity.RadioViewHolder.class, mDatabaseRadio.orderByChild("count")
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.RadioViewHolder viewHolder, final RadioReference model, final int position) {
                    if (model.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        viewHolder.setDetailsRadio(getApplicationContext(), model.getTitle(), model.getImage());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                double counter = Double.parseDouble(model.getCount()) - 1;
                                mDatabaseRadio.child(model.getTitle()).child("count").setValue(String.valueOf(counter));
                                try {
                                    MediaPlayerMain.initializeMediaPlayer(model.getUrl().toString(), getApplicationContext());
                                    MediaPlayerMain.playIt(model.getUrl().toString(), getApplicationContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
            };

            mRadioList.setAdapter(firebaseRecyclerAdapter);

            FirebaseRecyclerAdapter<BullerDetails, MainActivity.BulletViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<BullerDetails, MainActivity.BulletViewHolder>(
                    BullerDetails.class, R.layout.bullets_card, MainActivity.BulletViewHolder.class, mDatabase
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.BulletViewHolder viewHolder, final BullerDetails model, final int position) {
                    if (model.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent eachBullet = new Intent(SearchActivity.this, EachBulletActivity.class);
                                eachBullet.putExtra("bulletName", (getRef(position).getKey()));
                                eachBullet.putExtra("bulletTitle", model.getTitle());
                                startActivity(eachBullet);
                            }
                        });
                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
            };

            mBulletList.setAdapter(firebaseRecyclerAdapter1);

            FirebaseRecyclerAdapter<RegionalDetailsMain, MainActivity.RndViewHolder> firebaseRecyclerAdapterRegional = new FirebaseRecyclerAdapter<RegionalDetailsMain, MainActivity.RndViewHolder>(
                    RegionalDetailsMain.class, R.layout.regional_card, MainActivity.RndViewHolder.class, mDatabaseRegional.orderByChild("title")
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.RndViewHolder viewHolder, final RegionalDetailsMain model, final int position) {
                    if (model.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent in = new Intent(SearchActivity.this, RegionalBulletins.class);
                                in.putExtra("region", MainActivity.currRegion);
                                in.putExtra("title", model.getTitle());
                                startActivity(in);
                            }
                        });
                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
            };

            mRegionalList.setAdapter(firebaseRecyclerAdapterRegional);

            FirebaseRecyclerAdapter<ArchiveReference, MainActivity.ArchiveViewHolder> firebaseRecyclerAdapterArchieve = new FirebaseRecyclerAdapter<ArchiveReference, MainActivity.ArchiveViewHolder>(
                    ArchiveReference.class, R.layout.archive_card, MainActivity.ArchiveViewHolder.class, mDatabaseArchive.orderByChild("count")
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.ArchiveViewHolder viewHolder, final ArchiveReference model, final int position) {
                    if (model.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        viewHolder.setDetailsArchive(getApplicationContext(), model.getTitle(), model.getImage());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent goArchive = new Intent(SearchActivity.this, EachArchiveActivity.class);
                                goArchive.putExtra("title", model.getTitle());
                                startActivity(goArchive);

                            }
                        });
                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
            };

            mArchiveList.setAdapter(firebaseRecyclerAdapterArchieve);

        }

        else if(category.equalsIgnoreCase("Language")){

            FirebaseRecyclerAdapter<RadioReference,MainActivity.RadioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RadioReference, MainActivity.RadioViewHolder>(
                    RadioReference.class,R.layout.radio_card,MainActivity.RadioViewHolder.class,mDatabaseRadio.orderByChild("count")
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.RadioViewHolder viewHolder, final RadioReference model, final int position) {

                    if(model.getLanguage().toLowerCase().contains(keyword.toLowerCase())) {
                        viewHolder.setDetailsRadio(getApplicationContext(),model.getTitle(),model.getImage());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                double counter  = Double.parseDouble(model.getCount()) - 1;
                                mDatabaseRadio.child(model.getTitle()).child("count").setValue(String.valueOf(counter));
                                try {
                                    MediaPlayerMain.initializeMediaPlayer(model.getUrl().toString(),getApplicationContext());
                                    MediaPlayerMain.playIt(model.getUrl().toString(),getApplicationContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    }
                }
            };

            mRadioList.setAdapter(firebaseRecyclerAdapter);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    validName.clear();
                    Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                    for (DataSnapshot parentLang : langChildren1) {
                        int k = 0;
                        DataSnapshot langSnapshot = parentLang.child("Language");
                        Iterable<DataSnapshot> langChildren = langSnapshot.getChildren();
                        for (DataSnapshot lang : langChildren) {
                            if(lang.getValue(String.class).toLowerCase().contains(keyword.toLowerCase())){
                                k = 1;
                            }
                        }
                        if(k==1)
                            validName.add(parentLang.getKey());
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(SearchActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            });

            FirebaseRecyclerAdapter<BullerDetails,MainActivity.BulletViewHolder> firebaseRecyclerAdapterBullet = new FirebaseRecyclerAdapter<BullerDetails, MainActivity.BulletViewHolder>(
                    BullerDetails.class,R.layout.bullets_card,MainActivity.BulletViewHolder.class,mDatabase.orderByChild("count")
            ) {

                @Override
                protected void populateViewHolder(final MainActivity.BulletViewHolder viewHolder, final BullerDetails model, final int position) {
                    if(validName.contains((getRef(position).getKey()))) {
                        validName.remove((getRef(position).getKey()));
                        viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                double counter  = Double.parseDouble(model.getCount())- 1;
                                mDatabase.child(getRef(position).getKey()).child("count").setValue(String.valueOf(counter));
                                Intent eachBullet = new Intent(SearchActivity.this, EachBulletActivity.class);
                                eachBullet.putExtra("curLang",MainActivity.currLanguage);
                                eachBullet.putExtra("bulletName", (getRef(position).getKey()));
                                eachBullet.putExtra("bulletTitle", model.getTitle());
                                startActivity(eachBullet);
                            }
                        });
                    }else{
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    }
                }
            };

            mBulletList.setAdapter(firebaseRecyclerAdapterBullet);

            mDatabaseRegional.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    validNameRnd.clear();
                    Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                    for (DataSnapshot parentLang : langChildren1) {
                        int k = 0;
                        Iterable<DataSnapshot> langChildren = parentLang.getChildren();
                        for (DataSnapshot lang : langChildren) {
                            Iterable<DataSnapshot> langChildren11 = lang.getChildren();
                            for (DataSnapshot lang1 : langChildren11) {
                                if(lang1.getKey().equals("lang") && lang1.getValue(String.class).toLowerCase().contains(keyword.toLowerCase())){
                                    k = 1;
                                }
                            }
                        }
                        if(k==1){
                            validNameRnd.add(parentLang.getKey());
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(SearchActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            });


            FirebaseRecyclerAdapter<RegionalDetailsMain,MainActivity.RndViewHolder> firebaseRecyclerAdapterRegional = new FirebaseRecyclerAdapter<RegionalDetailsMain, MainActivity.RndViewHolder>(
                    RegionalDetailsMain.class,R.layout.regional_card,MainActivity.RndViewHolder.class,mDatabaseRegional.orderByChild("title")
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.RndViewHolder viewHolder, final RegionalDetailsMain model, final int position) {
                    if(validNameRnd.contains((getRef(position).getKey()))) {
                        validNameRnd.remove((getRef(position).getKey()));
                        viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent in = new Intent(SearchActivity.this,RegionalBulletins.class);
                                in.putExtra("region",MainActivity.currRegion);
                                in.putExtra("title",model.getTitle());
                                startActivity(in);
                            }
                        });
                    }else{
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    }
                }
            };

            mRegionalList.setAdapter(firebaseRecyclerAdapterRegional);


            FirebaseRecyclerAdapter<ArchiveReference,MainActivity.ArchiveViewHolder> firebaseRecyclerAdapterArchieve = new FirebaseRecyclerAdapter<ArchiveReference, MainActivity.ArchiveViewHolder>(
                    ArchiveReference.class,R.layout.archive_card,MainActivity.ArchiveViewHolder.class,mDatabaseArchive.orderByChild("count")
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.ArchiveViewHolder viewHolder, final ArchiveReference model, final int position) {
                    if (model.getLang().toLowerCase().contains(keyword.toLowerCase())) {
                        viewHolder.setDetailsArchive(getApplicationContext(), model.getTitle(), model.getImage());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent goArchive = new Intent(SearchActivity.this, EachArchiveActivity.class);
                                goArchive.putExtra("title", model.getTitle());
                                startActivity(goArchive);

                            }
                        });
                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
            };

            mArchiveList.setAdapter(firebaseRecyclerAdapterArchieve);


        }

    }



    void changeView(String x){
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }
}

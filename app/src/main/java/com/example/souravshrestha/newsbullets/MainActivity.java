package com.example.souravshrestha.newsbullets;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Handler;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    RecyclerView mBulletList,mRadioList,mArchiveList,mRegionalList,mTrendingView,mTrendingView2,mTrendingView3,mViewText;
    HashSet<String> dropItems = new HashSet<String>();
    HashSet<String> dropItemsRnd = new HashSet<String>();
    HashSet<String> dropItemsRadio = new HashSet<String>();
    TextView vAllLive;
    TextView vAll;
    Toolbar mActionNav;
    public static TextView vAllArchive,mPlayTitle;
    HashSet<String> dropItemsTextNews = new HashSet<String>();
    Spinner dropDown,dropDownRadioMain,dropDownRnd,dropDownNewsText;
    private DrawerLayout mDraw;
    private NavigationView mNav;
    static String currRegion;
    static String currLanguage;
    public static Button playPause;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference mDatabase,mDatabaseRadio,mDatabaseRegional,mDatabaseArchive,mDatabaseNewsText;
    private LayoutInflater layoutInflater;
    ViewPager viewPager;
    StorageReference myStr;
    CircleIndicator indicator;
    DatabaseReference myRef;
    List<String> listBanners = new ArrayList<>();
    ArrayList<String> radioMainListSpinner = new ArrayList<>();
    ArrayList<String> validName = new ArrayList<>();
    ArrayList<String> validNameRnd = new ArrayList<>();
    ArrayList<String> validNameNews = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    ArrayList<String> clickUrl = new ArrayList<>();
    Handler handler = new Handler();
    Runnable runnable;
    static int currentpage =0;
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionNav = (Toolbar)findViewById(R.id.mNav);
        setSupportActionBar(mActionNav);
        currRegion = "Aurangabad";
        currLanguage = "English";
        mNav = (NavigationView) findViewById(R.id.navBar);
        mDraw =(DrawerLayout) findViewById(R.id.drawLayout);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNav.setCheckedItem(R.id.home);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("NewsBullets");
        mDatabaseRegional = FirebaseDatabase.getInstance().getReference().child("RegionalBulletins");
        mDatabaseArchive = FirebaseDatabase.getInstance().getReference().child("Archives");
        mDatabaseRadio = FirebaseDatabase.getInstance().getReference().child("Stations");
        mBulletList = (RecyclerView)findViewById(R.id.bulletsView);
        mRadioList = (RecyclerView)findViewById(R.id.radioView_radio);
        mArchiveList = (RecyclerView)findViewById(R.id.archiveView);
        vAllLive = (TextView)findViewById(R.id.viewAllClick_radio);
        mRegionalList = (RecyclerView)findViewById(R.id.rndView);
        dropDown = (Spinner)findViewById(R.id.spinner);
        dropDownRnd = (Spinner)findViewById(R.id.spinner_rnd);
        dropDownRadioMain = (Spinner)findViewById(R.id.spinner_FM);
        myStr= FirebaseStorage.getInstance().getReference();
        dropDown.setOnItemSelectedListener(this);
        dropDownRnd.setOnItemSelectedListener(this);
        dropDownRadioMain.setOnItemSelectedListener(this);
        mTrendingView = (RecyclerView)findViewById(R.id.trending_view);
        mTrendingView.setHasFixedSize(true);
        mTrendingView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrendingView2 = (RecyclerView)findViewById(R.id.trending_view2);
        mTrendingView2.setHasFixedSize(true);
        mTrendingView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrendingView3 = (RecyclerView)findViewById(R.id.trending_view3);
        mTrendingView3.setHasFixedSize(true);
        mTrendingView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        vAllArchive = (TextView) findViewById(R.id.viewAllClick_archive);
        vAll = (TextView)findViewById(R.id.viewAllClick);
        myRef = FirebaseDatabase.getInstance().getReference().child("ListBanner");
        mDatabaseNewsText = FirebaseDatabase.getInstance().getReference().child("TextNews");
        mViewText = (RecyclerView)findViewById(R.id.viewTextNews);
        mViewText.setHasFixedSize(true);
        mViewText.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dropDownNewsText = (Spinner)findViewById(R.id.spinnerTextNews);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        dropDownNewsText.setOnItemSelectedListener(this);
        indicator=(CircleIndicator) findViewById(R.id.indicator);
        mBulletList.setHasFixedSize(true);
        mBulletList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRadioList.setHasFixedSize(true);
        mRadioList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mArchiveList.setHasFixedSize(true);
        mArchiveList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRegionalList.setHasFixedSize(true);
        mRegionalList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loadArchiveList();
        loadTrending();
        loadBullet();
        BottomNavigation bottomNavigation=(BottomNavigation)findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                switch (itemId){
                    case R.id.tab_home:

                        break;
                    case R.id.tab_radio:
                        Intent inRadio = new Intent(MainActivity.this,RadioAll.class);
                        startActivity(inRadio);
                        break;
                    case R.id.tab_search:
                        Intent searchIn = new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(searchIn);
                        break;
                    case R.id.tab_twitter:
                        Intent inTweet = new Intent(MainActivity.this,ShowTweet.class);
                        startActivity(inTweet);
                        break;
                    case R.id.tab_aboutUs:
//                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(MainActivity.this);
//                        dialog.setTitle("Inferno AIR")
//                                .setMessage("This Is the National News Broadcast App.\n\nDeveloped By : Team Inferno\n\n \tPlease note that the different channels may take 5 to 20 seconds to start playing, depending on your Internet Speed.")
//                                .setColoredCircle(R.color.colorPrimary)
//                                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
//                                .setCancelable(true)
//                                .setPositiveButtonText("Take me back to the App")
//                                .setPositiveButtonbackgroundColor(R.color.colorPrimary)
//                                .setPositiveButtonTextColor(R.color.white).
//                                setPositiveButtonClick(new Closure() {
//                                    @Override
//                                    public void exec() {
//                                        dialog.hide();
//                                    }
//                                });
//                        dialog.show();
                        Intent inFav = new Intent(MainActivity.this,FavoriteActivity.class);
                        startActivity(inFav);
                        break;
                }
            }
        });

        mDatabaseNewsText.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dropItemsTextNews.clear();
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    Iterable<DataSnapshot> langChildren = parentLang.getChildren();
                    for (DataSnapshot lang : langChildren) {
                        Iterable<DataSnapshot> langChildren11 = lang.getChildren();
                        for (DataSnapshot lang1 : langChildren11) {
                            if (lang1.getKey().equalsIgnoreCase("language"))
                                dropItemsTextNews.add(lang1.getValue().toString());
                        }
                    }
                }
                ArrayList<String> dropItemsListText = new ArrayList<>(dropItemsTextNews);
                Collections.sort(dropItemsListText);
                for (int i = 0; i < dropItemsListText.size(); i++) {
                    String x = dropItemsListText.get(i);
                    if (x.equalsIgnoreCase(currLanguage)) {
                        int index = dropItemsListText.indexOf(x);
                        dropItemsListText.remove(index);
                        dropItemsListText.add(0, x);
                    }
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, dropItemsListText);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropDownNewsText.setAdapter(adapter1);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        vAllArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rd1 = new Intent(MainActivity.this,ArchiveAll.class);
                startActivity(rd1);
            }
        });

        vAllLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rd = new Intent(MainActivity.this,RadioAll.class);
                startActivity(rd);
            }
        });

        mDatabaseRegional.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dropItemsRnd.clear();
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    Iterable<DataSnapshot> langChildren = parentLang.getChildren();
                    for (DataSnapshot lang : langChildren) {
                        Iterable<DataSnapshot> langChildren11 = lang.getChildren();
                        for (DataSnapshot lang1 : langChildren11) {
                            if(lang1.getKey().equalsIgnoreCase("title"))
                                dropItemsRnd.add(lang1.getValue().toString());
                        }
                    }
                }

                ArrayList<String> dropItemsListRnd = new ArrayList<>(dropItemsRnd);
                Collections.sort(dropItemsListRnd);
                for(int i =0 ;i<dropItemsListRnd.size();i++){
                    String x = dropItemsListRnd.get(i);
                    if(x.equalsIgnoreCase(currRegion)){
                        int index = dropItemsListRnd.indexOf(x);
                        dropItemsListRnd.remove(index);
                        dropItemsListRnd.add(0,x);
                    }
                }
                ArrayAdapter<String> adapterRnd = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item,dropItemsListRnd);
                adapterRnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDownRnd.setAdapter(adapterRnd);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });


        mDatabaseRadio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dropItemsRadio.clear();
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    Iterable<DataSnapshot> langChildren = parentLang.getChildren();
                    for (DataSnapshot lang : langChildren) {
                        if(lang.getKey().equalsIgnoreCase("language")) {
                            dropItemsRadio.add(lang.getValue().toString());
                        }
                    }
                }

                ArrayList<String> dropItemsListRadio = new ArrayList<>(dropItemsRadio);
                Collections.sort(dropItemsListRadio);
                for(int i =0 ;i<dropItemsListRadio.size();i++){
                    String x = dropItemsListRadio.get(i);
                    if(x.equalsIgnoreCase(currLanguage)){
                        int index = dropItemsListRadio.indexOf(x);
                        dropItemsListRadio.remove(index);
                        dropItemsListRadio.add(0,x);
                    }
                }
                dropItemsListRadio.add(0,"Select Language");
                ArrayAdapter<String> adapterRnd = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item,dropItemsListRadio);
                adapterRnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDownRadioMain.setAdapter(adapterRnd);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dropItems.clear();
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    DataSnapshot langSnapshot = parentLang.child("Language");
                    Iterable<DataSnapshot> langChildren = langSnapshot.getChildren();
                    for (DataSnapshot lang : langChildren) {
                        dropItems.add(lang.getValue(String.class));
                    }
                }

                ArrayList<String> dropItemsList = new ArrayList<>(dropItems);
                Collections.sort(dropItemsList);
                for(int i =0 ;i<dropItemsList.size();i++){
                    String x = dropItemsList.get(i);
                    if(x.equalsIgnoreCase(currLanguage)){
                        int index = dropItemsList.indexOf(x);
                        dropItemsList.remove(index);
                        dropItemsList.add(0,x);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item,dropItemsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });


        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,imageUrl,clickUrl);

        viewPager.setAdapter(viewPagerAdapter);
        indicator.setViewPager(viewPager);
        viewPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clickUrl.clear();
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    Iterable<DataSnapshot> langChildren12 = parentLang.getChildren();
                    for (DataSnapshot parentLang1 : langChildren12) {
                        if (parentLang1.getKey().equals("image")) {
                            String value = parentLang1.getValue(String.class);
                            imageUrl.add(value);
                            viewPagerAdapter.notifyDataSetChanged();
                        }
                        if (parentLang1.getKey().equals("click")) {
                            String value = parentLang1.getValue(String.class);
                            clickUrl.add(value);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                if(currentpage == imageUrl.size())
                    currentpage = 0;

                viewPager.setCurrentItem(currentpage++);
                handler.postDelayed(runnable, 5000);
            }
        };

        vAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goAll = new Intent(getApplicationContext(),NewsBulletins.class);
                goAll.putExtra("curLangg",currLanguage);
                startActivity(goAll);
            }
        });

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.settings :
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
                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(MainActivity.this);
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
    }

    private void loadTrending(){

        FirebaseRecyclerAdapter<RadioReference,RadioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RadioReference, RadioViewHolder>(
                RadioReference.class,R.layout.radio_card,RadioViewHolder.class,mDatabaseRadio.orderByChild("count").limitToFirst(2)
        ) {
            @Override
            protected void populateViewHolder(final RadioViewHolder viewHolder, final RadioReference model, final int position) {

                viewHolder.setDetailsRadio(getApplicationContext(),model.getTitle(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        double counter  = Double.parseDouble(model.getCount()) - 1;
                        mDatabaseRadio.child(model.getTitle()).child("count").setValue(String.valueOf(counter));
                    }
                });
            }
        };

        mTrendingView.setAdapter(firebaseRecyclerAdapter);

        FirebaseRecyclerAdapter<BullerDetails,BulletViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<BullerDetails, BulletViewHolder>(
                BullerDetails.class,R.layout.bullets_card,BulletViewHolder.class,mDatabase.orderByChild("count").limitToFirst(2)
        ) {
            @Override
            protected void populateViewHolder(final BulletViewHolder viewHolder, final BullerDetails model, final int position) {
                viewHolder.setDetails(getApplicationContext(),model.getImage(),model.getTitle());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        double counter  = Double.parseDouble(model.getCount()) - 1;
                        mDatabase.child(getRef(position).getKey()).child("count").setValue(String.valueOf(counter));
                        Intent eachBullet = new Intent(MainActivity.this, EachBulletActivity.class);
                        eachBullet.putExtra("bulletName",(getRef(position).getKey()));
                        eachBullet.putExtra("bulletTitle",model.getTitle());
                        startActivity(eachBullet);
                    }
                });
            }
        };

        mTrendingView2.setAdapter(firebaseRecyclerAdapter1);

        FirebaseRecyclerAdapter<ArchiveReference,ArchiveViewHolder> firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<ArchiveReference, ArchiveViewHolder>(
                ArchiveReference.class,R.layout.archive_card,ArchiveViewHolder.class,mDatabaseArchive.orderByChild("count").limitToFirst(2)
        ) {
            @Override
            protected void populateViewHolder(final ArchiveViewHolder viewHolder, final ArchiveReference model, final int position) {
                viewHolder.setDetailsArchive(getApplicationContext(),model.getTitle(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent goArchive = new Intent(MainActivity.this,EachArchiveActivity.class);
                        goArchive.putExtra("title",model.getTitle());
                        startActivity(goArchive);

                    }
                });
            }
        };

        mTrendingView3.setAdapter(firebaseRecyclerAdapter2);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        switch (parent.getId()) {
            case R.id.spinner_FM:
                loadRadioList(parent.getItemAtPosition(pos).toString());
                break;
            case R.id.spinner:
                currLanguage = parent.getItemAtPosition(pos).toString();
                loadBullet1(parent.getItemAtPosition(pos).toString());
                break;
            case R.id.spinner_rnd:
                loadRegionalList(parent.getItemAtPosition(pos).toString());
                break;
            case R.id.spinnerTextNews:
                loadTextNewsList(parent.getItemAtPosition(pos).toString());
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void loadBullet() {

        FirebaseRecyclerAdapter<BullerDetails,BulletViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BullerDetails, BulletViewHolder>(
                BullerDetails.class,R.layout.bullets_card,BulletViewHolder.class,mDatabase.limitToLast(4)
        ) {
            @Override
            protected void populateViewHolder(final BulletViewHolder viewHolder, final BullerDetails model, final int position) {
                viewHolder.setDetails(getApplicationContext(),model.getImage(),model.getTitle());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eachBullet = new Intent(MainActivity.this,EachBulletActivity.class);
                        eachBullet.putExtra("bulletName",(getRef(position).getKey()));
                        eachBullet.putExtra("bulletTitle",model.getTitle());
                        startActivity(eachBullet);
                    }
                });
            }
        };

        mBulletList.setAdapter(firebaseRecyclerAdapter);
    }

    private void loadRegionalList(final String region) {

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
                            if(lang1.getKey().equals("title") && lang1.getValue(String.class).equals(region)){
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
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerAdapter<RegionalDetailsMain,RndViewHolder> firebaseRecyclerAdapterRegional = new FirebaseRecyclerAdapter<RegionalDetailsMain, RndViewHolder>(
                RegionalDetailsMain.class,R.layout.regional_card,RndViewHolder.class,mDatabaseRegional.orderByChild("title")
        ) {
            @Override
            protected void populateViewHolder(final RndViewHolder viewHolder, final RegionalDetailsMain model, final int position) {
                if(validNameRnd.contains((getRef(position).getKey()))) {
                    validNameRnd.remove((getRef(position).getKey()));
                    viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent in = new Intent(MainActivity.this,RegionalBulletins.class);
                            in.putExtra("region",region);
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
    }



    private void loadBullet1(final String lan) {

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
                        if(lang.getValue(String.class).equals(lan)){
                            k = 1;
                        }
                    }
                    if(k==1)
                        validName.add(parentLang.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseRecyclerAdapter<BullerDetails,BulletViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BullerDetails, BulletViewHolder>(
                BullerDetails.class,R.layout.bullets_card,BulletViewHolder.class,mDatabase.orderByChild("count")
        ) {
            @Override
            protected void populateViewHolder(final BulletViewHolder viewHolder, final BullerDetails model, final int position) {
                if(validName.contains((getRef(position).getKey()))) {
                    validName.remove((getRef(position).getKey()));
                    viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            double counter  = Double.parseDouble(model.getCount()) - 1;
                            mDatabase.child(getRef(position).getKey()).child("count").setValue(String.valueOf(counter));
                            Intent eachBullet = new Intent(MainActivity.this, EachBulletActivity.class);
                            eachBullet.putExtra("curLang",currLanguage);
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

        mBulletList.setAdapter(firebaseRecyclerAdapter);
    }



    private void loadRadioList(final String languageFM) {

        if(languageFM.equalsIgnoreCase("Select Language")) {
            FirebaseRecyclerAdapter<RadioReference, RadioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RadioReference, RadioViewHolder>(
                    RadioReference.class, R.layout.radio_card, RadioViewHolder.class, mDatabaseRadio.orderByChild("count").limitToFirst(5)
            ) {
                @Override
                protected void populateViewHolder(final RadioViewHolder viewHolder, final RadioReference model, final int position) {
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
                }
            };
            mRadioList.setAdapter(firebaseRecyclerAdapter);
        }
        else
        {
            FirebaseRecyclerAdapter<RadioReference, RadioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RadioReference, RadioViewHolder>(
                    RadioReference.class, R.layout.radio_card, RadioViewHolder.class, mDatabaseRadio.orderByChild("count")
            ) {
                @Override
                protected void populateViewHolder(final RadioViewHolder viewHolder, final RadioReference model, final int position) {

                    if(model.getLanguage().equalsIgnoreCase(languageFM))
                    {
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
                    }else{
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                    }
                }
            };
            mRadioList.setAdapter(firebaseRecyclerAdapter);
        }
    }

    public static class RadioViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RadioViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetailsRadio(Context con,String title_name,String img1){
            TextView title = (TextView)mView.findViewById(R.id.txt_radio);
            ImageView img = (ImageView)mView.findViewById(R.id.img_radio);
            Glide.with(con).load(img1).into(img);
            title.setText(title_name);
        }

    }


    private void loadArchiveList() {

        FirebaseRecyclerAdapter<ArchiveReference,ArchiveViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ArchiveReference, ArchiveViewHolder>(
                ArchiveReference.class,R.layout.archive_card,ArchiveViewHolder.class,mDatabaseArchive.orderByChild("count")
        ) {
            @Override
            protected void populateViewHolder(final ArchiveViewHolder viewHolder, final ArchiveReference model, final int position) {
                viewHolder.setDetailsArchive(getApplicationContext(),model.getTitle(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent goArchive = new Intent(MainActivity.this,EachArchiveActivity.class);
                        goArchive.putExtra("title",model.getTitle());
                        startActivity(goArchive);

                    }
                });
            }
        };

        mArchiveList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ArchiveViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ArchiveViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetailsArchive(Context con,String title_name,String img_src){
            TextView title = (TextView)mView.findViewById(R.id.txt_archive);
            ImageView arc_image_main = (ImageView)mView.findViewById(R.id.img_archive);
            Glide.with(con).load(img_src).into(arc_image_main);
            title.setText(title_name);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        runnable.run();
    }

    private void loadTextNewsList(final String language) {

        mDatabaseNewsText.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                validNameNews.clear();
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    Iterable<DataSnapshot> langChildren = parentLang.getChildren();
                    for (DataSnapshot lang : langChildren) {
                        int k = 0;
                        Iterable<DataSnapshot> langChildren11 = lang.getChildren();
                        for (DataSnapshot lang1 : langChildren11) {
                            if(lang1.getKey().equals("language") && lang1.getValue(String.class).equals(language)){
                                k = 1;
                            }
                        }
                        if(k==1){
//                            validNameNews.add(parentLang.child("title").getValue().toString());
                              validNameNews.add(parentLang.child("title").getValue().toString());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerAdapter<TextNewsDetails,TextNewsViewHolder> firebaseRecyclerAdapterRegional = new FirebaseRecyclerAdapter<TextNewsDetails, MainActivity.TextNewsViewHolder>(
                TextNewsDetails.class,R.layout.text_news_card,TextNewsViewHolder.class,mDatabaseNewsText
        ) {
            @Override
            protected void populateViewHolder(final TextNewsViewHolder viewHolder, final TextNewsDetails model, final int position) {
                if(validNameNews.contains((getRef(position).getKey()))) {
                    validNameNews.remove((getRef(position).getKey()));
                    viewHolder.setDetailsTextNews(getApplicationContext(), model.getTitle(), model.getImage());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent goText = new Intent(MainActivity.this,EachTextNews.class);
                            goText.putExtra("title",model.getTitle());
                            goText.putExtra("language",language);
                            startActivity(goText);
                        }
                    });
                }else{
                    viewHolder.itemView.setVisibility(View.GONE);
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
            }
        };
        mViewText.setAdapter(firebaseRecyclerAdapterRegional);
    }

    public static class TextNewsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextNewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetailsTextNews(Context con,String title_name,String img_src){
            TextView title = (TextView)mView.findViewById(R.id.txt_txtNews);
            ImageView arc_image_main = (ImageView)mView.findViewById(R.id.img_txtNews);
            Glide.with(con).load(img_src).into(arc_image_main);
            title.setText(title_name);
        }

    }


    public static class BulletViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BulletViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context con,String img_name, String title_name){
            ImageView mImg = (ImageView) mView.findViewById(R.id.img);
            TextView title = (TextView)mView.findViewById(R.id.txt);
            Glide.with(con).load(img_name).into(mImg);
            title.setText(title_name);
        }

    }

    public static class RndViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RndViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context con,String img_name, String title_name){
            ImageView mImg = (ImageView) mView.findViewById(R.id.img_rnd);
            Glide.with(con).load(img_name).into(mImg);
        }

    }
}

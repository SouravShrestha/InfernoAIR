package com.example.souravshrestha.newsbullets;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

public class ArchiveAll extends AppCompatActivity {


    private DrawerLayout mDraw;
    private NavigationView mNav;
    RecyclerView mArchiveList;
    Toolbar mActionNav;
    private DatabaseReference mDatabase;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_all);
        mNav = (NavigationView) findViewById(R.id.navBar_arc);
        mActionNav = (Toolbar)findViewById(R.id.mNav);
        setSupportActionBar(mActionNav);
        mDraw =(DrawerLayout) findViewById(R.id.drawLayout_archive);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Archives");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mArchiveList = (RecyclerView)findViewById(R.id.arcAll);
        mArchiveList.setHasFixedSize(true);
        mArchiveList.setLayoutManager(new GridLayoutManager(this,3));
        loadRadioList();
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        Intent homeGo = new Intent(ArchiveAll.this,MainActivity.class);
                        startActivity(homeGo);
                        finish();
                        break;
                    case R.id.settings :
                        Toast.makeText(ArchiveAll.this,item.getTitle(),Toast.LENGTH_SHORT).show();
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
                    case R.id.aboutUs:
                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(ArchiveAll.this);
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

        BottomNavigation bottomNavigation=(BottomNavigation)findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                switch (itemId){
                    case R.id.tab_home:
                        break;
                    case R.id.tab_radio:
                        Intent inRadio = new Intent(ArchiveAll.this,RadioAll.class);
                        startActivity(inRadio);
                        break;
                    case R.id.tab_search:
                        Intent searchIn = new Intent(ArchiveAll.this,SearchActivity.class);
                        startActivity(searchIn);
                        break;
                    case R.id.tab_twitter:
                        Intent inTweet = new Intent(ArchiveAll.this,ShowTweet.class);
                        startActivity(inTweet);
                        break;
                    case R.id.tab_aboutUs:
                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(ArchiveAll.this);
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
                        break;
                }
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

        FirebaseRecyclerAdapter<ArchiveReference,ArchiveAll.ArchiveViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ArchiveReference, ArchiveAll.ArchiveViewHolder>(
                ArchiveReference.class,R.layout.archive_card,ArchiveAll.ArchiveViewHolder.class,mDatabase
        ) {
            @Override
            protected void populateViewHolder(final ArchiveAll.ArchiveViewHolder viewHolder, final ArchiveReference model, final int position) {
                viewHolder.setDetailsRadio(getApplicationContext(),model.getTitle(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent goArchive = new Intent(ArchiveAll.this,EachArchiveActivity.class);
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

        public void setDetailsRadio(Context con, String title_name, String img1){
            TextView title = (TextView)mView.findViewById(R.id.txt_archive);
            ImageView img = (ImageView)mView.findViewById(R.id.img_archive);
            Glide.with(con).load(img1).into(img);
            title.setText(title_name);
        }

    }
}

package com.example.souravshrestha.newsbullets;

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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EachArchiveActivity extends AppCompatActivity {

    String title;
    TextView mTitle;
    private DrawerLayout mDraw;
    private NavigationView mNav;
    Toolbar mActionNav;
    RecyclerView archiveView;
    DatabaseReference mDatabaseArchive;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_archive);
        mDraw =(DrawerLayout) findViewById(R.id.draw_archive);
        mActionNav = (Toolbar)findViewById(R.id.mNav);
        setSupportActionBar(mActionNav);
        archiveView = (RecyclerView)findViewById(R.id.viewEachArchive);
        archiveView.setHasFixedSize(true);
        archiveView.setLayoutManager(new LinearLayoutManager(this));
        mNav = (NavigationView) findViewById(R.id.navBar4);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitle = (TextView)findViewById(R.id.textView3);
        title = getIntent().getStringExtra("title");
        mDatabaseArchive = FirebaseDatabase.getInstance().getReference().child("Archives").child(title);
        mTitle.setText(title);
        loadArchives();

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        Intent homeGo = new Intent(EachArchiveActivity.this,MainActivity.class);
                        startActivity(homeGo);
                        finish();
                        break;
                    case R.id.settings :
                        Toast.makeText(EachArchiveActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.aboutUs:
                        Toast.makeText(EachArchiveActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                }
                mNav.setCheckedItem(item.getItemId());
                return false;
            }
        });

    }

    private void loadArchives() {

        FirebaseRecyclerAdapter<ArchiveReference,EachArchiveActivity.ArchiveViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<ArchiveReference,EachArchiveActivity.ArchiveViewHolder>(
                ArchiveReference.class,R.layout.archive_list_content,EachArchiveActivity.ArchiveViewHolder.class,mDatabaseArchive.child("List").orderByChild("title")
        ) {
            @Override
            protected void populateViewHolder(final EachArchiveActivity.ArchiveViewHolder viewHolder,final ArchiveReference model, int position) {
                viewHolder.setDetails(getApplicationContext(), model.getImage(),model.getTitle(),properDate(model.getDate()), model.getUrl());
            }
        };
        archiveView.setAdapter(firebaseRecyclerAdapter1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ArchiveViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ArchiveViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(final Context con,final String img, final String title, final String date, final String url){
            TextView dateL = (TextView) mView.findViewById(R.id.arc_date);
            TextView titleArc = (TextView)mView.findViewById(R.id.txt_arc_title);
            CircleImageView imgArc = (CircleImageView)mView.findViewById(R.id.img_arc_img);
            Glide.with(con).load(img).into(imgArc);
            dateL.setText(date);
            titleArc.setText(title);
            titleArc.setOnClickListener(new View.OnClickListener() {
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

    public static String properDate(String x){

        String y[] = x.split(" ");
        String day = y[0];
        String month = y[1];
        String year = y[2];
        String monthName="";
        switch (Integer.parseInt(month)){
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;
        }
        return (day+" "+monthName+", "+year);

    }

}

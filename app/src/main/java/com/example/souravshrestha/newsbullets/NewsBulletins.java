package com.example.souravshrestha.newsbullets;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsBulletins extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView mBulletList;
    private DatabaseReference mDatabase;
    Spinner dropDown;
    static String curLang;
    ArrayList<String> validName = new ArrayList<>();
    HashSet<String> dropItems = new HashSet<String>();
    private DrawerLayout mDraw;
    private NavigationView mNav;
    Toolbar mActionNav;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_bulletins);
        mNav = (NavigationView) findViewById(R.id.navBar2);
        mDraw =(DrawerLayout) findViewById(R.id.drawLayout1);
        mActionNav = (Toolbar)findViewById(R.id.mNav);
        setSupportActionBar(mActionNav);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("NewsBullets");
        mBulletList = (RecyclerView)findViewById(R.id.recAll);
        curLang = getIntent().getExtras().getString("curLangg");
        mBulletList.setHasFixedSize(true);
        dropDown = (Spinner)findViewById(R.id.spin3);
        dropDown.setOnItemSelectedListener(this);
        mBulletList.setLayoutManager(new LinearLayoutManager(this));
        loadBulletList();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                dropItemsList.add(0,"Select Your Language");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewsBulletins.this, android.R.layout.simple_spinner_item,dropItemsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(NewsBulletins.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home :
                        Intent homeGo = new Intent(NewsBulletins.this,MainActivity.class);
                        startActivity(homeGo);
                        finish();
                        break;
                    case R.id.settings :
                        Toast.makeText(NewsBulletins.this,item.getTitle(),Toast.LENGTH_SHORT).show();
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
                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(NewsBulletins.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBulletList() {

        final FirebaseRecyclerAdapter<BullerDetails,NewsBulletins.BulletViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BullerDetails, NewsBulletins.BulletViewHolder>(
                BullerDetails.class,R.layout.bullet_all,NewsBulletins.BulletViewHolder.class,mDatabase
        ) {

            @Override
            protected void populateViewHolder(final NewsBulletins.BulletViewHolder viewHolder, final BullerDetails model, final int position) {
                    viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent eachBullet = new Intent(NewsBulletins.this, EachBulletActivity.class);
                            eachBullet.putExtra("bulletName", (getRef(position).getKey()));
                            eachBullet.putExtra("bulletTitle", model.getTitle());
                            startActivity(eachBullet);
                        }
                    });
            }
        };
        mBulletList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        if(parent.getItemAtPosition(pos).toString().equalsIgnoreCase("Select Your Language"))
            loadBulletList();
        else
            loadBullet1(parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void loadBullet1(final String lan) {

        validName.clear();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                for (DataSnapshot parentLang : langChildren1) {
                    int k = 0;
                    DataSnapshot langSnapshot = parentLang.child("Language");
                    Iterable<DataSnapshot> langChildren = langSnapshot.getChildren();
                    for (DataSnapshot lang : langChildren) {
//                        dropItems.add(lang.getValue(String.class));
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
                Toast.makeText(NewsBulletins.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        final FirebaseRecyclerAdapter<BullerDetails,NewsBulletins.BulletViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BullerDetails, NewsBulletins.BulletViewHolder>(
                BullerDetails.class,R.layout.bullet_all,NewsBulletins.BulletViewHolder.class,mDatabase
        ) {

            @Override
            protected void populateViewHolder(final NewsBulletins.BulletViewHolder viewHolder, final BullerDetails model, final int position) {
                if(validName.contains((getRef(position).getKey()))) {
                    validName.remove((getRef(position).getKey()));
                    viewHolder.setDetails(getApplicationContext(), model.getImage(), model.getTitle());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent eachBullet = new Intent(NewsBulletins.this, EachBulletActivity.class);
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

    public static class BulletViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BulletViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context con,String img_name, String title_name){
            CircleImageView mImg = (CircleImageView) mView.findViewById(R.id.bulletImgAll);
            TextView title = (TextView)mView.findViewById(R.id.bulletAllTitle);
            Glide.with(con).load(img_name).into(mImg);
            title.setText(title_name);
        }


    }
}

package com.example.souravshrestha.newsbullets;

import android.app.DatePickerDialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class EachTextNews extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView mEachBulletList;
    TextView mTextHead;
    String title;
    TextView mPlayTitle;
    Button playPause;
    Toolbar mActionNav;
    HashSet<String> dropItems = new HashSet<String>();
    Spinner dropDown;
    static String defaultLanguage;
    ArrayList<String> validName = new ArrayList<>();
    private DrawerLayout mDraw;
    private NavigationView mNav;
    private ActionBarDrawerToggle mToggle;
    TextView selectDate;
    long count;
    private DatabaseReference mData, mData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_text_news);
        mNav = (NavigationView) findViewById(R.id.navBar1);
        playPause = (Button) findViewById(R.id.bPlayPause);
        mPlayTitle = (TextView) findViewById(R.id.playTitle);
        mActionNav = (Toolbar) findViewById(R.id.mNav);
        setSupportActionBar(mActionNav);
        mDraw = (DrawerLayout) findViewById(R.id.drawLayout2);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        selectDate = (TextView) findViewById(R.id.datePick);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = getIntent().getExtras().getString("title");
        defaultLanguage = getIntent().getExtras().getString("language");
        mTextHead = (TextView) findViewById(R.id.tHeadList);
        dropDown = (Spinner) findViewById(R.id.spin);
        dropDown.setOnItemSelectedListener(this);
        mData = FirebaseDatabase.getInstance().getReference("TextNews");
        mData2 = mData.child(title);
        mEachBulletList = (RecyclerView) findViewById(R.id.eachBulletListRecycle);
        mEachBulletList.setHasFixedSize(true);
        mEachBulletList.setLayoutManager(new LinearLayoutManager(this));

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent homeGo = new Intent(EachTextNews.this, MainActivity.class);
                        startActivity(homeGo);
                        finish();
                        break;
                    case R.id.settings:
                        Toast.makeText(EachTextNews.this, item.getTitle(), Toast.LENGTH_SHORT).show();
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
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "shrestha.sourav30@gmail.com"));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                        }
                        break;

                    case R.id.aboutUs:
                        final AwesomeInfoDialog dialog = new AwesomeInfoDialog(EachTextNews.this);
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

        mData2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dropItems.clear();
                count =  dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> listChild1 = dataSnapshot.getChildren();
                for (DataSnapshot childTwo : listChild1) {
                    if (!childTwo.getKey().equalsIgnoreCase("title") && !childTwo.getKey().equalsIgnoreCase("image"))
                        dropItems.add(childTwo.child("language").getValue().toString());
                }

                ArrayList<String> dropItemsList = new ArrayList<>(dropItems);
                Collections.sort(dropItemsList);
                for (int i = 0; i < dropItemsList.size(); i++) {
                    String x = dropItemsList.get(i);
                    if (x.equalsIgnoreCase(defaultLanguage)) {
                        int index = dropItemsList.indexOf(x);
                        dropItemsList.remove(index);
                        dropItemsList.add(0, x);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EachTextNews.this, android.R.layout.simple_spinner_item, dropItemsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EachTextNews.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadTextNews(final String lan) {
        {
            mData2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    validName.clear();
                    Iterable<DataSnapshot> langChildren1 = dataSnapshot.getChildren();
                    for (DataSnapshot parentLang : langChildren1) {
                        if (!parentLang.getKey().equalsIgnoreCase("title") && !parentLang.getKey().equalsIgnoreCase("image")) {
                            if ((parentLang.child("language").getValue().toString()).equals(lan)) {
                                validName.add(parentLang.getKey());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

            FirebaseRecyclerAdapter<EachNewsContentMain, EachTextNews.TextNewsViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<EachNewsContentMain, EachTextNews.TextNewsViewHolder>(
                    EachNewsContentMain.class, R.layout.each_text_news_content, EachTextNews.TextNewsViewHolder.class,mData2.limitToFirst((int)count-2)
            ) {
                @Override
                protected void populateViewHolder(final EachTextNews.TextNewsViewHolder viewHolder, final EachNewsContentMain model, int position) {
                    if (validName.contains((getRef(position).getKey()))) {
                        validName.remove((getRef(position).getKey()));
                        viewHolder.setDetails(EachTextNews.this, model.getHead(), model.getBody(), EachArchiveActivity.properDate(model.getDate()));
                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
            };

            mEachBulletList.setAdapter(firebaseRecyclerAdapter1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        loadTextNews(parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class TextNewsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TextNewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(final Context con, final String head, final String body, String textDate) {
            TextView mHead = (TextView) mView.findViewById(R.id.headNews);
            TextView mDate = (TextView) mView.findViewById(R.id.news_date);
            mHead.setText(head);
            mDate.setText(textDate);
            LinearLayout leachContent = (LinearLayout)mView.findViewById(R.id.linearEachBulletList);
            leachContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent startNews = new Intent(con,EachTextNewsContentActivity.class);
                    startNews.putExtra("title",head);
                    startNews.putExtra("body",body);
                    con.startActivity(startNews);
                }
            });
        }

    }
}

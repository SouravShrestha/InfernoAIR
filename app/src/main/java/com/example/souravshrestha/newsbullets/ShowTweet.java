package com.example.souravshrestha.newsbullets;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.wang.avi.AVLoadingIndicatorView;

public class ShowTweet extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    TweetTimelineRecyclerViewAdapter adapter;
    private DrawerLayout mDraw;
    AVLoadingIndicatorView mAvi;
    private NavigationView mNav;
    Toolbar mActionNav;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tweet);
        mAvi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        mActionNav = (Toolbar)findViewById(R.id.mNav);
        mNav = (NavigationView) findViewById(R.id.navBar);
        setSupportActionBar(mActionNav);
        mDraw =(DrawerLayout) findViewById(R.id.drawerTweet);
        mToggle = new ActionBarDrawerToggle(this,mDraw,R.string.open,R.string.close);
        mDraw.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNav.setCheckedItem(R.id.home);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("3oTBxHdVLsI5ckdSZS6iFPTTZ", "ynr5k6UG5KbxyAWiMfgUVHzJ6fl9dPZFxBSU4g3M9ZREqSMtmL"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        UserTimeline userTimeline = new UserTimeline.Builder().screenName("airnewsalerts").build();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final TweetTimelineRecyclerViewAdapter adapter =
                new TweetTimelineRecyclerViewAdapter.Builder(this)
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();

        recyclerView.setAdapter(adapter);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAvi.hide();
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        //implement refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (adapter == null)
                    return;
                swipeRefreshLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
//                        Toast.makeText(ShowTweet.this, "refresh tweets.", Toast.LENGTH_SHORT).show();

                        Intent inTweet = new Intent(ShowTweet.this,ShowTweet.class);
                        startActivity(inTweet);
                        finish();
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void failure(TwitterException exception) {

                        Toast.makeText(ShowTweet.this, "Failed to refresh tweets.", Toast.LENGTH_SHORT).show();
                    }
                });
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

}

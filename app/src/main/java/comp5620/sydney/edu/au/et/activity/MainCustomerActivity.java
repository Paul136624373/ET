package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.MyViewPagerAdapter;

public class MainCustomerActivity extends Activity {

    private TextView mTextMessage;

    // For forum function
    private ViewPager mViewPager;
    private TabLayout mTab;
    private MyViewPagerAdapter mAdapter;
    private List<ListView> mListView;
    private List<String> titleList;
    private ListView postView;
    private ListView groupView;
    private ListView MessageView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // floating action button for group and forum
            ConstraintLayout createGroup = findViewById(R.id.create_group);
            ConstraintLayout createPost = findViewById(R.id.create_post);

            switch (item.getItemId()) {
                case R.id.navigation_customer_group:
                    createGroup.setVisibility(View.VISIBLE);
                    createPost.setVisibility(View.INVISIBLE);

                    mTextMessage.setText("Group");
                    // Initialize the view of Post and Message
                    initGroupView();

                    // Initialize the tab of forum
                    initGroupTab();
                    return true;
                case R.id.navigation_customer_forum:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.VISIBLE);

                    mTextMessage.setText("Forum");
                    // Initialize the view of Post and Message
                    initForumView();
                    // Initialize the tab of forum
                    initForumTab();

                    return true;
                case R.id.navigation_customer_friend:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.INVISIBLE);

                    mTextMessage.setText("Friends");
                    return true;
                case R.id.navigation_customer_profile:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.INVISIBLE);

                    mTextMessage.setText("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Start of group function

        FloatingActionButton fab_group = (FloatingActionButton) findViewById(R.id.fab_group);
        fab_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainCustomerActivity.this,NewGroupActivity.class));

            }
        });

        // End of group function

        // Start of forum function

        FloatingActionButton fab_post = (FloatingActionButton) findViewById(R.id.fab_post);
        fab_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(MainCustomerActivity.this,NewPostActivity.class));

            }
        });

        // End of forum function
    }

    // Initialize the tab of forum interface
    public void initForumTab(){
        mViewPager = (ViewPager) findViewById(R.id.page);

        titleList = new ArrayList<>();
        titleList.add("Post");
        titleList.add("Message");

        mTab = (TabLayout) findViewById(R.id.tab);
        mTab.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < titleList.size(); i++) {
            mTab.addTab(mTab.newTab().setText(titleList.get(i)));
        }

        mTab.setupWithViewPager(mViewPager);

        mAdapter = new MyViewPagerAdapter(mListView,titleList);

        mViewPager.setAdapter(mAdapter);
    }

    // Initialize the tab of activity interface
    public void initGroupTab(){
        mViewPager = (ViewPager) findViewById(R.id.page);

        titleList = new ArrayList<>();
        titleList.add("Activity");
        titleList.add("Message");

        mTab = (TabLayout) findViewById(R.id.tab);
        mTab.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < titleList.size(); i++) {
            mTab.addTab(mTab.newTab().setText(titleList.get(i)));
        }

        mTab.setupWithViewPager(mViewPager);

        mAdapter = new MyViewPagerAdapter(mListView,titleList);

        mViewPager.setAdapter(mAdapter);
    }

    // Initialize the view of Post and Message
    public void initForumView(){
        mListView = new ArrayList<>();
        postView = new ListView(this);
        MessageView = new ListView(this);
        MessageView.setPadding(40,20,20, 40);
        MessageView.setDivider(new ColorDrawable(Color.GRAY));
        MessageView.setDividerHeight(2);

        BottomNavigationView theNavigation = findViewById(R.id.navigation);

        mListView.add(postView);
        mListView.add(MessageView);

    }

    // Initialize the view of Activity and Message
    public void initGroupView(){
        mListView = new ArrayList<>();
        groupView = new ListView(this);
        MessageView = new ListView(this);
        MessageView.setPadding(40,20,20, 40);
        MessageView.setDivider(new ColorDrawable(Color.GRAY));
        MessageView.setDividerHeight(2);

        BottomNavigationView theNavigation = findViewById(R.id.navigation);

        mListView.add(groupView);
        mListView.add(MessageView);

    }

}

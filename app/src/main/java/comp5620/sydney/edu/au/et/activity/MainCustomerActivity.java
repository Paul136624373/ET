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

public class MainCustomerActivity extends Activity {

    private TextView topBar_text;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // floating action button for group and forum
            ConstraintLayout createGroup = findViewById(R.id.create_group);
            ConstraintLayout createPost = findViewById(R.id.create_post);

            topBar_text = (TextView)findViewById(R.id.topBar_text);

            switch (item.getItemId()) {
                case R.id.navigation_customer_group:
                    createGroup.setVisibility(View.VISIBLE);
                    createPost.setVisibility(View.INVISIBLE);
                    topBar_text.setText("View Group");
                    return true;

                case R.id.navigation_customer_forum:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.VISIBLE);
                    topBar_text.setText("View Post");
                    return true;

                case R.id.navigation_customer_friend:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.INVISIBLE);
                    topBar_text.setText("My Friends");
                    return true;

                case R.id.navigation_customer_profile:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.INVISIBLE);
                    topBar_text.setText("Profile");
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

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

}

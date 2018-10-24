package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.ShowGroupAdapter;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Restaurant;

import static android.support.constraint.Constraints.TAG;

public class MainCustomerActivity extends Activity {

    private TextView topBar_text;

    private Customer currentCustomer;

    // For group
    private ShowGroupAdapter showGroupAdapter;
    private List<Group> allGroups;
    private List<Group> showGroups;
    private List<Group> joinedGroups;
    private List<Group> ownedGroups;
    private List<Restaurant> allRestaurants;

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

        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");
        allGroups = (List<Group>) getIntent().getSerializableExtra("allGroups");
        showGroups = (List<Group>) getIntent().getSerializableExtra("showGroups");
        allRestaurants = (List<Restaurant>) getIntent().getSerializableExtra("allRestaurants");
        joinedGroups = new ArrayList<>();
        ownedGroups = new ArrayList<>();
        // Show group view
        initGroupView();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Realtime updating
        readGroupsFromServer();
        readRestaurantsFromServer();

        // Start of group function

        FloatingActionButton fab_group = (FloatingActionButton) findViewById(R.id.fab_group);
        fab_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainCustomerActivity.this,NewGroupActivity.class);
                intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                startActivity(intent);

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


    //------------------------------------------------------------------- Database reading ----------------------------------------------------------------------

    // Get all restaurant accounts from server
    private void readRestaurantsFromServer()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference date = mReference.child("restaurants");

        // Read from the database
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                allRestaurants.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Restaurant oneRestaurant = postSnapshot.getValue(Restaurant.class);

                    allRestaurants.add(oneRestaurant);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    // Get all groups from server
    private void readGroupsFromServer()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference date = mReference.child("groups");

        // Read from the database
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                allGroups.clear();
                showGroups.clear();
                joinedGroups.clear();
                ownedGroups.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Group oneGroup = postSnapshot.getValue(Group.class);
                    allGroups.add(oneGroup);

                    if(oneGroup.getOwner().equals(currentCustomer.getUsername()))
                    {
                        ownedGroups.add(oneGroup);
                        continue;
                    }

                    int currentNumber = oneGroup.members.size();
                    boolean exist = false;
                    // Get the members' information
                    for (Map<String, String> member : oneGroup.members.values()) {
                        for (Map.Entry<String, String> field : member.entrySet()) {
                            if(field.getValue().equals(currentCustomer.getUsername()))
                            {
                                exist = true;
                                break;
                            }
                        }
                        if(exist)
                        {
                            break;
                        }
                    }
                    if(!exist)
                    {
                        if(currentNumber < Integer.parseInt(oneGroup.getNumberOfPeople()) && oneGroup.getType().equals("Public")) {
                            showGroups.add(oneGroup);
                        }
                    }
                    else
                    {
                        joinedGroups.add(oneGroup);
                    }
                }
                showGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    //------------------------------------------------------------------- End of Database reading ----------------------------------------------------------------------

    // Initialize the view of Group
    public void initGroupView(){
        ListView group_lv = findViewById(R.id.displayAllGroup);
        showGroupAdapter = new ShowGroupAdapter(MainCustomerActivity.this,0, showGroups, currentCustomer);
        group_lv.setAdapter(showGroupAdapter);
    }
}

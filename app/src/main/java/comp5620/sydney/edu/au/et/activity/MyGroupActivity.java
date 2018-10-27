package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.MyGroupsAdapter;
import comp5620.sydney.edu.au.et.adapter.MyPostsAdapter;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Friend;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Menu;
import comp5620.sydney.edu.au.et.model.Post;
import comp5620.sydney.edu.au.et.model.RestaurantComment;

import static android.support.constraint.Constraints.TAG;

public class MyGroupActivity extends Activity {

    private ListView myGroup_lv;
    private MyGroupsAdapter myGroupsAdapter;
    private List<Group> myGroups;
    private List<Group> ownedGroups;
    private List<Group> joinedGroups;
    private List<Menu> allMenus;
    private View btn_back;
    private Customer currentCustomer;
    private List<RestaurantComment> allRestaurantComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        btn_back = findViewById(R.id.btn_back);

        allRestaurantComments = new ArrayList<>();
        ownedGroups =  (ArrayList<Group>) getIntent().getSerializableExtra("ownedGroups");
        joinedGroups = (ArrayList<Group>) getIntent().getSerializableExtra("joinedGroups");
        allMenus = (ArrayList<Menu>) getIntent().getSerializableExtra("allMenus");
        allRestaurantComments = (ArrayList<RestaurantComment>) getIntent().getSerializableExtra("allRestaurantComments");
        myGroups = new ArrayList<Group>();
        myGroups.addAll(ownedGroups);
        myGroups.addAll(joinedGroups);
        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");

        initMyGroups();
        readCommentsFromServer();
        readGroupsFromServer();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initMyGroups() {
        myGroup_lv = findViewById(R.id.myGroup_lv);

        // Read data from database and create the views of post and message
        myGroupsAdapter = new MyGroupsAdapter(MyGroupActivity.this,0,myGroups, ownedGroups, joinedGroups, currentCustomer, allRestaurantComments);

        // Connect the listView and the adapter
        myGroup_lv.setAdapter(myGroupsAdapter);

        myGroupsAdapter.setOnItemClickListener(new MyGroupsAdapter.onItemClickListener() {

            @Override
            public void onConfirmClick(Group theGroup) {
                Intent intent = new Intent(MyGroupActivity.this,RecommendActivity.class);
                intent.putExtra("theGroup", (Serializable) theGroup);
                intent.putExtra("allMenus", (Serializable) allMenus);
                startActivity(intent);
            }

            @Override
            public void onCommentClick(Group theGroup) {
                Intent intent = new Intent(MyGroupActivity.this,RestaurantCommentActivity.class);
                intent.putExtra("theGroup", (Serializable) theGroup);
                intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                startActivity(intent);
            }
        });
    }

    // Get all comments from server
    private void readCommentsFromServer()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference date = mReference.child("comments");

        // Read from the database
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                allRestaurantComments.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    RestaurantComment oneRestaurantComment = postSnapshot.getValue(RestaurantComment.class);

                    allRestaurantComments.add(oneRestaurantComment);
                }
                if(myGroupsAdapter != null){
                    myGroupsAdapter.notifyDataSetChanged();
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
                myGroups.clear();
                joinedGroups.clear();
                ownedGroups.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Group oneGroup = postSnapshot.getValue(Group.class);

                    if(oneGroup.getOwner().equals(currentCustomer.getUsername()))
                    {
                        ownedGroups.add(oneGroup);
                        continue;
                    }

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
                    if(exist)
                    {
                        joinedGroups.add(oneGroup);
                    }
                }
                myGroups.addAll(ownedGroups);
                myGroups.addAll(joinedGroups);
                if(myGroupsAdapter != null){
                    myGroupsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}

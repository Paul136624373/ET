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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.MyGroupsAdapter;
import comp5620.sydney.edu.au.et.adapter.MyPostsAdapter;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Friend;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Post;

import static android.support.constraint.Constraints.TAG;

public class MyGroupActivity extends Activity {

    private ListView myGroup_lv;
    private MyGroupsAdapter myGroupsAdapter;
    private List<Group> myGroups;
    private List<Group> ownedGroups;
    private List<Group> joinedGroups;
    private View btn_back;
    private Customer currentCustomer;

    private Button chooseRestaurant_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        btn_back = findViewById(R.id.btn_back);

        ownedGroups =  (ArrayList<Group>) getIntent().getSerializableExtra("ownedGroups");
        joinedGroups = (ArrayList<Group>) getIntent().getSerializableExtra("joinedGroups");
        myGroups = new ArrayList<Group>();
        myGroups.addAll(ownedGroups);
        myGroups.addAll(joinedGroups);
        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");

        initMyGroups();

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
        myGroupsAdapter = new MyGroupsAdapter(MyGroupActivity.this,0,myGroups, ownedGroups, joinedGroups, currentCustomer);

        // Connect the listView and the adapter
        myGroup_lv.setAdapter(myGroupsAdapter);

        myGroupsAdapter.setOnItemClickListener(new MyGroupsAdapter.onItemClickListener() {

            @Override
            public void onConfirmClick(Group theGroup) {
                chooseRestaurant(theGroup);
            }

            @Override
            public void onCommentClick(Group theGroup) {

            }
        });
    }

    public void chooseRestaurant(Group theGroup)
    {


    }
}

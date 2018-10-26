package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
import comp5620.sydney.edu.au.et.adapter.AllPostsAdapter;
import comp5620.sydney.edu.au.et.adapter.MyFriendsAdapter;
import comp5620.sydney.edu.au.et.adapter.ShowGroupAdapter;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Friend;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Menu;
import comp5620.sydney.edu.au.et.model.Post;
import comp5620.sydney.edu.au.et.model.Restaurant;
import comp5620.sydney.edu.au.et.tools.MarshmallowPermission;

import static android.support.constraint.Constraints.TAG;

public class MainCustomerActivity extends Activity {

    private TextView topBar_text;
    private ListView displayAllGroup;
    private ListView displayAllPost;
    private ListView displayAllFriend;
    private ImageButton add_friend;
    private Button addFriend_dialog;

    private Customer currentCustomer;
    private MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);

    // For group
    private ShowGroupAdapter showGroupAdapter;
    private List<Group> allGroups;
    private List<Group> showGroups;
    private List<Group> joinedGroups;
    private List<Group> ownedGroups;
    private List<Customer> allCustomers;

    // For forum
    private List<Post> allPosts;
    private List<Post> myPosts;
    private AllPostsAdapter allPostsAdapter;

    // For friend
    private List<Friend> myFriends;
    private MyFriendsAdapter myFriendsAdapter;

    // For profile related function
    private List<Menu> allMenus;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // floating action button for group and forum
            ConstraintLayout createGroup = findViewById(R.id.create_group);
            ConstraintLayout createPost = findViewById(R.id.create_post);
            ConstraintLayout customer_profile = findViewById(R.id.customer_profile);

            topBar_text = (TextView)findViewById(R.id.topBar_text);
            displayAllGroup = (ListView)findViewById(R.id.displayAllGroup);
            displayAllPost = (ListView)findViewById(R.id.displayAllPost);
            displayAllFriend = (ListView)findViewById(R.id.displayAllFriend);

            add_friend = (ImageButton)findViewById(R.id.add_friend);
            add_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    show();
                }
            });

            switch (item.getItemId()) {
                case R.id.navigation_customer_group:
                    createGroup.setVisibility(View.VISIBLE);
                    createPost.setVisibility(View.INVISIBLE);
                    customer_profile.setVisibility(View.INVISIBLE);
                    displayAllFriend.setVisibility(View.INVISIBLE);
                    displayAllGroup.setVisibility(View.VISIBLE);
                    displayAllPost.setVisibility(View.INVISIBLE);

                    add_friend.setVisibility(View.INVISIBLE);

                    topBar_text.setText("View Group");
                    return true;

                case R.id.navigation_customer_forum:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.VISIBLE);
                    customer_profile.setVisibility(View.INVISIBLE);
                    displayAllFriend.setVisibility(View.INVISIBLE);
                    displayAllGroup.setVisibility(View.INVISIBLE);
                    displayAllPost.setVisibility(View.VISIBLE);
                    add_friend.setVisibility(View.INVISIBLE);

                    initForumView();

                    topBar_text.setText("View Post");
                    return true;

                case R.id.navigation_customer_friend:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.INVISIBLE);
                    customer_profile.setVisibility(View.INVISIBLE);
                    displayAllFriend.setVisibility(View.VISIBLE);
                    displayAllGroup.setVisibility(View.INVISIBLE);
                    displayAllPost.setVisibility(View.INVISIBLE);
                    add_friend.setVisibility(View.VISIBLE);

                    initFriendView();

                    topBar_text.setText("My Friends");
                    return true;

                case R.id.navigation_customer_profile:
                    createGroup.setVisibility(View.INVISIBLE);
                    createPost.setVisibility(View.INVISIBLE);
                    customer_profile.setVisibility(View.VISIBLE);
                    displayAllFriend.setVisibility(View.INVISIBLE);
                    displayAllGroup.setVisibility(View.INVISIBLE);
                    displayAllPost.setVisibility(View.INVISIBLE);
                    add_friend.setVisibility(View.INVISIBLE);

                    initProfileView();

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
        allPosts = (List<Post>) getIntent().getSerializableExtra("allPosts");
        myPosts = (List<Post>) getIntent().getSerializableExtra("myPosts");
        allCustomers = (List<Customer>) getIntent().getSerializableExtra("allCustomers");
        myFriends = (List<Friend>) getIntent().getSerializableExtra("myFriends");
        joinedGroups = new ArrayList<>();
        ownedGroups = new ArrayList<>();
        allMenus = new ArrayList<>();
        if(myFriends == null)
        {
            myFriends = new ArrayList<>();
        }
        if(myPosts == null)
        {
            myPosts = new ArrayList<>();
        }
        if(allCustomers == null)
        {
            allCustomers = new ArrayList<>();
        }

        // Initialize adapter
        // Show group view
        initGroupView();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Realtime updating
        readGroupsFromServer();
        readCustomersFromServer();
        readPostsFromServer();
        readFriendsFromServer();

        // Start of group function

        FloatingActionButton fab_group = (FloatingActionButton) findViewById(R.id.fab_group);
        fab_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainCustomerActivity.this,NewGroupActivity.class);
                intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                intent.putExtra("allCustomers", (Serializable) allCustomers);
                intent.putExtra("myFriends", (Serializable) myFriends);
                startActivity(intent);

            }
        });

        // End of group function

        // Start of forum function

        FloatingActionButton fab_post = (FloatingActionButton) findViewById(R.id.fab_post);
        fab_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshmallowPermission.checkPermissionForCamera()
                        || !marshmallowPermission.checkPermissionForExternalStorage()) {
                    marshmallowPermission.requestPermissionForCamera();
                }  else {
                    Intent intent = new Intent(MainCustomerActivity.this,NewPostActivity.class);
                    intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                    startActivity(intent);
                }

            }
        });

        // End of forum function

        // Start of profile related function
        Button btn_my_pos = (Button) findViewById(R.id.btn_my_post);
        btn_my_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCustomerActivity.this,MyPostActivity.class);
                intent.putExtra("myPosts", (Serializable) myPosts);
                intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                startActivity(intent);
            }
        });

        Button btn_my_group = (Button) findViewById(R.id.btn_my_group);
        btn_my_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCustomerActivity.this,MyGroupActivity.class);
                intent.putExtra("ownedGroups", (Serializable) ownedGroups);
                intent.putExtra("joinedGroups", (Serializable) joinedGroups);
                intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                startActivity(intent);
            }
        });
    }


    //------------------------------------------------------------------- Database reading ----------------------------------------------------------------------

    // Get all customer accounts from server
    private void readCustomersFromServer()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference date = mReference.child("customers");

        // Read from the database
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                allCustomers.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Customer oneCustomer = postSnapshot.getValue(Customer.class);

                    allCustomers.add(oneCustomer);
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
                        String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                        String eatingTime = oneGroup.getEatingTime();
                        eatingTime = eatingTime.replace("/", "");
                        eatingTime = eatingTime.replace(" ", "");
                        eatingTime = eatingTime.replace(":", "");
                        if(currentNumber < Integer.parseInt(oneGroup.getNumberOfPeople()) && Long.parseLong(eatingTime) > Long.parseLong(currentTime)) {
                            if(oneGroup.getType().equals("Public")) {
                                showGroups.add(oneGroup);
                            }
                            else
                            {
                                // If the user is invited to the group
                                boolean beInvited = false;
                                for (String key : oneGroup.invites.keySet()) {
                                    if(key.equals(currentCustomer.getUsername()))
                                    {
                                        showGroups.add(oneGroup);
                                        break;
                                    }
                                }
                            }
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

    // Get all posts from server
    private void readPostsFromServer()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference date = mReference.child("posts");

        // Read from the database
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                allPosts.clear();
                myPosts.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String id = postSnapshot.getKey();
                    String username = postSnapshot.getValue(Post.class).author;
                    String time = postSnapshot.getValue(Post.class).time;
                    String body = postSnapshot.getValue(Post.class).body;
                    Map<String, String> thumbups = postSnapshot.getValue(Post.class).thumbups;
                    Map<String, Map<String, String>> comments = postSnapshot.getValue(Post.class).comments;

                    Post newPost = new Post(id, username, time, body, thumbups, comments);

                    allPosts.add(0, newPost);

                    if(newPost.author.equals(currentCustomer.getUsername()))
                    {
                        myPosts.add(0, newPost);
                    }
                }
                if(allPostsAdapter != null) {
                    allPostsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    // Get all friends from server
    private void readFriendsFromServer()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference date = mReference.child("friends");

        // Read from the database
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myFriends.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Friend oneFriend = postSnapshot.getValue(Friend.class);

                    if(oneFriend.getFirstUsername().equals(currentCustomer.getUsername()) || oneFriend.getSecondUsername().equals(currentCustomer.getUsername()))
                    {
                        myFriends.add(oneFriend);
                    }
                }
                if(myFriendsAdapter != null) {
                    myFriendsAdapter.notifyDataSetChanged();
                }
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

    // Initialize the view of forum
    public void initForumView(){
        ListView forum_lv = findViewById(R.id.displayAllPost);
        allPostsAdapter = new AllPostsAdapter(MainCustomerActivity.this,0, allPosts, currentCustomer);

        allPostsAdapter.setOnItemClickListener(new AllPostsAdapter.onItemClickListener() {
            @Override
            public void onViewClick(Post thePost) {
                Intent intent = new Intent(MainCustomerActivity.this, ViewPostActivity.class);
                intent.putExtra("thePost", (Serializable) thePost);
                intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                startActivity(intent);
            }

            @Override
            public void onCommentClick(Post thePost) {
                Intent intent = new Intent(MainCustomerActivity.this,NewCommentActivity.class);
                intent.putExtra("thePost", (Serializable) thePost);
                intent.putExtra("currentCustomer", (Serializable) currentCustomer);
                startActivity(intent);
            }

            @Override
            public void onThumpupClick(Post thePost) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // If the user have thumped up this post
                if(thePost.thumbups.containsKey(currentCustomer.getUsername()) && thePost.thumbups.get(currentCustomer.getUsername()) != "")
                {
                    thePost.thumbups.put(currentCustomer.getUsername(), "");
                    Map<String, Object> postValues = thePost.toMap();

                    Map<String, Object> childUpdates = new LinkedHashMap<>();
                    childUpdates.put("/posts/" +  thePost.id, postValues);
                    mDatabase.updateChildren(childUpdates);

                    // Change the color of thump up
                    allPostsAdapter.notifyDataSetChanged();
                }
                else
                {
                    String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
                    thePost.thumbups.put(currentCustomer.getUsername(), time);
                    Map<String, Object> postValues = thePost.toMap();

                    Map<String, Object> childUpdates = new LinkedHashMap<>();
                    childUpdates.put("/posts/" +  thePost.id, postValues);
                    mDatabase.updateChildren(childUpdates);

                    // Change the color of thump up
                    allPostsAdapter.notifyDataSetChanged();
                }

            }
        });

        forum_lv.setAdapter(allPostsAdapter);
    }

    // Initialize the view of Friend
    public void initFriendView(){
        ListView friend_lv = findViewById(R.id.displayAllFriend);
        myFriendsAdapter = new MyFriendsAdapter(MainCustomerActivity.this,0, myFriends, currentCustomer, allCustomers);
        friend_lv.setAdapter(myFriendsAdapter);
    }

    // Initialize the view of Profile
    public void initProfileView(){
        TextView username_tv = findViewById(R.id.username_tv);
        TextView phone_tv = findViewById(R.id.phone_tv);
        TextView surname_tv = findViewById(R.id.surname_tv);
        TextView givenname_tv = findViewById(R.id.givenname_tv);
        TextView address_tv = findViewById(R.id.address_tv);
        TextView gender_tv = findViewById(R.id.gender_tv);

        username_tv.setText(currentCustomer.getUsername());
        phone_tv.setText(currentCustomer.getPhoneNumber());
        surname_tv.setText(currentCustomer.getSurname());
        givenname_tv.setText(currentCustomer.getGivenName());
        address_tv.setText(currentCustomer.getAddress());
        gender_tv.setText(currentCustomer.getGender());
    }


    public void show()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Add a friend");
        dialog.setCancelable(true);
        final View layout = View.inflate(this, R.layout.add_friend,null);
        dialog.setContentView(layout);
        dialog.show();


        addFriend_dialog = (Button) dialog.findViewById(R.id.addFriend);

        addFriend_dialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //diaplay.setText(dish.getText().toString());
                dialog.dismiss();

                EditText addFriend_et = (EditText) layout.findViewById(R.id.edUsername);
                String friendUsername = addFriend_et.getText().toString();

                boolean exist = false;
                for(Customer oneCustomer : allCustomers)
                {
                    if(oneCustomer.getUsername().equals(friendUsername))
                    {
                        exist = true;
                        break;
                    }
                }

                boolean alreadyFriend = false;
                for(Friend oneFriend : myFriends)
                {
                    if(friendUsername.equals(oneFriend.getFirstUsername()))
                    {
                        if(currentCustomer.getUsername().equals(oneFriend.getSecondUsername()))
                        {
                            alreadyFriend = true;
                            break;
                        }
                    }
                    if(friendUsername.equals(oneFriend.getSecondUsername()))
                    {
                        if(currentCustomer.getUsername().equals(oneFriend.getFirstUsername()))
                        {
                            alreadyFriend = true;
                            break;
                        }
                    }
                }

                if(!exist)
                {
                    Toast toast=Toast.makeText(getApplicationContext(), "The user is not found.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(alreadyFriend)
                {
                    Toast toast=Toast.makeText(getApplicationContext(), "You are already friends.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                    // Store new restaurant information into the database
                    Friend newFriend = new Friend();
                    String groupKey = mDatabase.child("friends").push().getKey();
                    newFriend.setFirstUsername(currentCustomer.getUsername());
                    newFriend.setSecondUsername(friendUsername);
                    Map<String, Object> friendValues = newFriend.toMap();
                    Map<String, Object> childUpdates = new LinkedHashMap<>();
                    childUpdates.put("/friends/" +  groupKey, friendValues);
                    mDatabase.updateChildren(childUpdates);
                }
            }
        });

    }

}

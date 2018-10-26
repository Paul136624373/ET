package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.MyFriendsAdapter;
import comp5620.sydney.edu.au.et.adapter.MyMenuAdapter;
import comp5620.sydney.edu.au.et.adapter.ReservationAdapter;
import comp5620.sydney.edu.au.et.adapter.RestaurantCommentAdapter;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Friend;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Menu;
import comp5620.sydney.edu.au.et.model.Restaurant;
import comp5620.sydney.edu.au.et.model.RestaurantComment;

import static android.support.constraint.Constraints.TAG;

public class MainRestaurantActivity extends Activity {

    private TextView topBar_text;
    private ListView displayAllReservation;
    private ListView displayAllComment;
    private ListView displayAllMenu;
    private ImageButton add_menu;
    private Button addMenu_dialog;

    private Restaurant currentRestaurant;
    private List<Group> myGroups;
    private Menu myMenu;
    private List<Map<String, String>> myDishes;
    private ReservationAdapter reservationAdapter;
    private MyMenuAdapter myMenuAdapter;
    private List<RestaurantComment> myRestaurantComments;
    private RestaurantCommentAdapter restaurantCommentAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            ConstraintLayout restaurant_profile = findViewById(R.id.restaurant_profile);
            topBar_text = (TextView)findViewById(R.id.topBar_text);
            displayAllComment = (ListView)findViewById(R.id.displayAllComment);
            displayAllMenu = (ListView)findViewById(R.id.displayAllMenu);
            displayAllReservation = (ListView)findViewById(R.id.displayAllReservation);

            add_menu = (ImageButton)findViewById(R.id.btn_add_menu);
            add_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddMenu();
                }
            });

            switch (item.getItemId()) {
                case R.id.navigation_restaurant_info:
                    restaurant_profile.setVisibility(View.INVISIBLE);
                    displayAllReservation.setVisibility(View.VISIBLE);
                    displayAllComment.setVisibility(View.INVISIBLE);
                    displayAllMenu.setVisibility(View.INVISIBLE);
                    add_menu.setVisibility(View.INVISIBLE);

                    topBar_text.setText("Reservation");
                    return true;

                case R.id.navigation_restaurant_comment:
                    restaurant_profile.setVisibility(View.INVISIBLE);
                    displayAllReservation.setVisibility(View.INVISIBLE);
                    displayAllComment.setVisibility(View.VISIBLE);
                    displayAllMenu.setVisibility(View.INVISIBLE);
                    add_menu.setVisibility(View.INVISIBLE);

                    initCommentView();

                    topBar_text.setText("Received comments");
                    return true;

                case R.id.navigation_restaurant_menu:
                    restaurant_profile.setVisibility(View.INVISIBLE);
                    displayAllReservation.setVisibility(View.INVISIBLE);
                    displayAllComment.setVisibility(View.INVISIBLE);
                    displayAllMenu.setVisibility(View.VISIBLE);
                    add_menu.setVisibility(View.VISIBLE);

                    initMenuView();

                    topBar_text.setText("Menu");
                    return true;

                case R.id.navigation_restaurant_profile:
                    restaurant_profile.setVisibility(View.VISIBLE);
                    displayAllReservation.setVisibility(View.INVISIBLE);
                    displayAllComment.setVisibility(View.INVISIBLE);
                    displayAllMenu.setVisibility(View.INVISIBLE);
                    add_menu.setVisibility(View.INVISIBLE);

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
        setContentView(R.layout.activity_main_restaurant);

        currentRestaurant = (Restaurant) getIntent().getSerializableExtra("currentRestaurant");
        myGroups = (List<Group>) getIntent().getSerializableExtra("myGroups");
        myMenu = (Menu) getIntent().getSerializableExtra("myMenu");
        myRestaurantComments = (List<RestaurantComment>) getIntent().getSerializableExtra("myRestaurantComments");
        if(myGroups == null) {
            myGroups = new ArrayList<>();
        }
        myDishes = new ArrayList<>();

        for (Map<String, String> value : myMenu.dishes.values()) {
            myDishes.add(value);
        }

        initReservationView();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Read data from server
        readMenusFromServer();
        readGroupsFromServer();
        readCommentsFromServer();

        Button btn_logout = findViewById(R.id.logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainRestaurantActivity.this);
                builder.setTitle("Logout")
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MainRestaurantActivity.this,LoginActivity.class);
                                startActivity(intent);

                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog and nothing will be deleted.
                            }
                        });

                builder.create().show();

            }
        });
    }

    public void showAddMenu()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("ADD Dish");
        dialog.setCancelable(true);
        final View layout = View.inflate(this, R.layout.add_menu,null);
        dialog.setContentView(layout);
        dialog.show();


        addMenu_dialog = (Button) dialog.findViewById(R.id.addDish);

        addMenu_dialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //diaplay.setText(dish.getText().toString());
                dialog.dismiss();

                EditText edDish_et = (EditText) layout.findViewById(R.id.edDish);
                EditText edPrice_et = (EditText) layout.findViewById(R.id.edPrice);
                String dishName = edDish_et.getText().toString();
                String dishPrice = edPrice_et.getText().toString();
                RadioGroup dish_rg = (RadioGroup)layout.findViewById(R.id.dish_rg);
                RadioButton flavour_rb = (RadioButton)layout.findViewById(dish_rg.getCheckedRadioButtonId());
                String dishFlavour = flavour_rb.getText().toString();

                if(dishName.equals("") || dishPrice.equals(""))
                {
                    Toast toast=Toast.makeText(getApplicationContext(), "The dish information is not complete.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                boolean exist = false;
                for(Map<String, String> oneDish : myDishes)
                {
                    for (Map.Entry<String, String> entry : oneDish.entrySet()) {
                        if(entry.getKey().equals("name")){
                            if(entry.getValue().equals(dishName))
                            {
                                exist = true;
                            }
                        }
                    }
                }
                if(exist)
                {
                    Toast toast=Toast.makeText(getApplicationContext(), "The dish is already exist.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                    // Store new menu information into the database
                    Menu newMenu = myMenu;
                    String dishKey = mDatabase.child("menus").child(newMenu.getMenuID()).child("dishes").push().getKey();
                    Map<String, String> detail = new LinkedHashMap<>();
                    detail.put("name", dishName);
                    detail.put("price", "$" + dishPrice);
                    detail.put("flavour", dishFlavour);
                    newMenu.dishes.put(dishKey, detail);
                    Map<String, Object> menuValues = newMenu.toMap();
                    Map<String, Object> childUpdates = new LinkedHashMap<>();
                    childUpdates.put("/menus/" +  newMenu.getMenuID(), menuValues);
                    mDatabase.updateChildren(childUpdates);
                }
            }
        });

    }

    // Initialize the view of Reservation
    public void initReservationView(){
        ListView reservation_lv = findViewById(R.id.displayAllReservation);
        reservationAdapter = new ReservationAdapter(MainRestaurantActivity.this,0, myGroups);
        reservation_lv.setAdapter(reservationAdapter);

    }

    // Initialize the view of Comments
    public void initCommentView(){
        ListView comment_lv = findViewById(R.id.displayAllComment);
        restaurantCommentAdapter = new RestaurantCommentAdapter(MainRestaurantActivity.this,0, myRestaurantComments);
        comment_lv.setAdapter(restaurantCommentAdapter);

    }

    // Initialize the view of Friend
    public void initMenuView(){
        ListView menu_lv = findViewById(R.id.displayAllMenu);
        myMenuAdapter = new MyMenuAdapter(MainRestaurantActivity.this,0, myDishes);
        menu_lv.setAdapter(myMenuAdapter);
    }

    // Initialize the view of Profile
    public void initProfileView(){
        TextView displayUser_tv = findViewById(R.id.displayUser);
        TextView displayName_tv = findViewById(R.id.displayName);
        TextView displayAddress_tv = findViewById(R.id.displayAddress);

        displayUser_tv.setText(currentRestaurant.getUsername());
        displayName_tv.setText(currentRestaurant.getRestaurantName());
        displayAddress_tv.setText(currentRestaurant.getAddress());
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
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Group oneGroup = postSnapshot.getValue(Group.class);

                    if(oneGroup.getRestaurantName() != null) {
                        if (oneGroup.getRestaurantName().equals(currentRestaurant.getRestaurantName())) {
                            myGroups.add(oneGroup);
                        }
                    }
                }
                if(reservationAdapter != null){
                    reservationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    // Get all menus from server
    private void readMenusFromServer()
    {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference date = mReference.child("menus");

        // Read from the database
        date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myDishes.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Menu oneMenu = postSnapshot.getValue(Menu.class);
                    if(oneMenu.getRestaurantName().equals(currentRestaurant.getRestaurantName()))
                    {
                        myMenu = oneMenu;
                        for (Map<String, String> value : myMenu.dishes.values()) {
                            myDishes.add(value);
                        }
                        break;
                    }
                }
                if(myMenuAdapter != null){
                    myMenuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
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
                myRestaurantComments.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    RestaurantComment oneRestaurantComment = postSnapshot.getValue(RestaurantComment.class);

                    if(oneRestaurantComment.getRestaurantName().equals(currentRestaurant.getRestaurantName())) {
                        myRestaurantComments.add(oneRestaurantComment);
                    }
                }
                if(restaurantCommentAdapter != null){
                    restaurantCommentAdapter.notifyDataSetChanged();
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

package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import java.util.regex.Pattern;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Post;
import comp5620.sydney.edu.au.et.model.Restaurant;

import static android.support.constraint.Constraints.TAG;

public class RegisterCustomerActivity extends Activity {

    private List<Customer> allCustomers;
    private List<Restaurant> allRestaurants;
    private Customer newCustomer;
    private List<Group> allGroups;
    private List<Group> showGroups;
    private List<Post> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        allCustomers =  (ArrayList<Customer>) getIntent().getSerializableExtra("allCustomers");
        allRestaurants =  (ArrayList<Restaurant>) getIntent().getSerializableExtra("allRestaurants");
        allGroups = new ArrayList<>();
        showGroups = new ArrayList<>();
        allPosts = new ArrayList<>();

        Button saveCu = (Button) findViewById(R.id.saveCu);
        saveCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText username_et = findViewById(R.id.edUsername);
                EditText password_et = findViewById(R.id.edPassword);
                EditText surname_et = findViewById(R.id.edSurname);
                EditText givenName_et = findViewById(R.id.edGivenname);
                EditText phone_et = findViewById(R.id.edPhone);
                EditText address_et = findViewById(R.id.edAddress);
                RadioGroup gender_rg = (RadioGroup)findViewById(R.id.radioGroup);


                String username = username_et.getText().toString();
                String password = password_et.getText().toString();
                String surname = surname_et.getText().toString();
                String givenName = givenName_et.getText().toString();
                String phone = phone_et.getText().toString();
                String address = address_et.getText().toString();
                RadioButton radioButton = (RadioButton)findViewById(gender_rg.getCheckedRadioButtonId());
                String gender = radioButton.getText().toString();

                if(isEmpty("Username", username)) {
                    return;
                }
                if(isEmpty("Password", password)) {
                    return;
                }
                if(isEmpty("Surname", surname)) {
                    return;
                }
                if(isEmpty("Given name", givenName)) {
                    return;
                }
                if(isEmpty("Phone", phone)) {
                    return;
                }
                if(isEmpty("Address", address)) {
                    return;
                }

                if(username.length() < 2)
                {
                    // The username is not good
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCustomerActivity.this);
                    builder.setTitle("Bad Username")
                            .setMessage("Sorry, Username must contain more than two characters.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Back to the register page
                                }
                            });

                    builder.create().show();
                    return;
                }

                String pattern = "^\\d{10}$";

                boolean isValidPhone = Pattern.matches(pattern, phone);

                if(!isValidPhone)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCustomerActivity.this);
                    builder.setTitle("Phone number is wrong")
                            .setMessage("Wrong phone number format. Can only contains 10 digits.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Back to the register page
                                }
                            });

                    builder.create().show();
                    return;
                }

                // Check username in customer and restaurant account
                for(Customer customer : allCustomers)
                {
                    if(username.equals(customer.getUsername()))
                    {
                        // The username is already exist
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCustomerActivity.this);
                        builder.setTitle("Username already exist")
                                .setMessage("Sorry, the username is already exist.")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Back to the register page
                                    }
                                });

                        builder.create().show();
                        return;
                    }
                }

                for(Restaurant restaurant : allRestaurants)
                {
                    if(username.equals(restaurant.getUsername()))
                    {
                        // The username is already exist
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCustomerActivity.this);
                        builder.setTitle("Username already exist")
                                .setMessage("Sorry, the username is already exist.")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Back to the register page
                                    }
                                });

                        builder.create().show();
                        return;
                    }
                }
                // End of checking username

                // Validate password
                if(!isValidPassword(password))
                {
                    // The password is invalid
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCustomerActivity.this);
                    builder.setTitle("Invalid password")
                            .setMessage("Sorry, password can only contain letters, digits and underlines.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Back to the register page
                                }
                            });

                    builder.create().show();
                    return;
                }

                for(Group oneGroup : allGroups)
                {
                    int currentNumber = oneGroup.members.size();
                    String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                    String eatingTime = oneGroup.getEatingTime();
                    eatingTime = eatingTime.replace("/", "");
                    eatingTime = eatingTime.replace(" ", "");
                    eatingTime = eatingTime.replace(":", "");
                    if(currentNumber < Integer.parseInt(oneGroup.getNumberOfPeople()) && Long.parseLong(eatingTime) > Long.parseLong(currentTime)) {
                        if(oneGroup.getType().equals("Public")) {
                            showGroups.add(oneGroup);
                        }
                        // The new user cannot be invited before in logic
                    }
                }

                newCustomer = new Customer(username, password, surname, givenName, phone, address, gender);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // Store new restaurant information into the database
                String customerKey = mDatabase.child("customers").push().getKey();
                Map<String, Object> customerValues = newCustomer.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/customers/" +  customerKey, customerValues);
                mDatabase.updateChildren(childUpdates);

                Intent intent = new Intent(RegisterCustomerActivity.this,MainCustomerActivity.class);
                intent.putExtra("allGroups", (Serializable) allGroups);
                intent.putExtra("showGroups", (Serializable) showGroups);
                intent.putExtra("allPosts", (Serializable) allPosts);
                intent.putExtra("currentCustomer", (Serializable) newCustomer);
                startActivity(intent);

                finish();
            }
        });

        Button btn_back = findViewById(R.id.bt_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCustomerActivity.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

        readGroupsFromServer();
        readPostsFromServer();
    }

    // Validate password, can only contain letters, digits and underline
    public boolean isValidPassword(String password)
    {
        boolean valid = true;
        String pattern = "\\w+";
        if(!Pattern.matches(pattern, password))
        {
            valid = false;
        }

        return valid;
    }

    // Show dialog on the screen if any field is empty
    public boolean isEmpty(String field, String value)
    {
        boolean empty = false;
        if(value.equals(""))
        {
            empty = true;
            // The field is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCustomerActivity.this);
            builder.setTitle("'" + field + "' is empty")
                    .setMessage("Sorry, '" + field + "' cannot be empty")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Back to the register page
                        }
                    });

            builder.create().show();
        }

        return empty;
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
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Group oneGroup = postSnapshot.getValue(Group.class);

                    allGroups.add(oneGroup);
                }
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
        DatabaseReference data = mReference.child("posts");

        // Read from the database
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                allPosts.clear();
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

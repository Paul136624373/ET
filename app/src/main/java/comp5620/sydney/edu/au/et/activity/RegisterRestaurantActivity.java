package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Menu;
import comp5620.sydney.edu.au.et.model.Restaurant;

import static android.support.constraint.Constraints.TAG;

public class RegisterRestaurantActivity extends Activity {

    private Button add;
    private List<Customer> allCustomers;
    private List<Restaurant> allRestaurants;
    private Restaurant newRestaurant;
    private Menu newMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);

        allCustomers =  (ArrayList<Customer>) getIntent().getSerializableExtra("allCustomers");
        allRestaurants =  (ArrayList<Restaurant>) getIntent().getSerializableExtra("allRestaurants");

        Button addMenu = (Button) findViewById(R.id.addMenu);
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        Button saveRe = (Button) findViewById(R.id.saveRe);
        saveRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText username_et = findViewById(R.id.edUsername);
                EditText password_et = findViewById(R.id.edPassword);
                EditText restaurantName_et = findViewById(R.id.edrestaurantname);
                EditText address_et = findViewById(R.id.edAddress);

                String username = username_et.getText().toString();
                String password = password_et.getText().toString();
                String restaurantName = restaurantName_et.getText().toString();
                String address = address_et.getText().toString();

                if(isEmpty("Username", username)) {
                    return;
                }
                if(isEmpty("Password", password)) {
                    return;
                }
                if(isEmpty("Restaurant Name", restaurantName)) {
                    return;
                }
                if(isEmpty("Address", address)) {
                    return;
                }

                // Check username in customer and restaurant account
                for(Customer customer : allCustomers)
                {
                    if(username.equals(customer.getUsername()))
                    {
                        // The username is already exist
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterRestaurantActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterRestaurantActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterRestaurantActivity.this);
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

                newRestaurant = new Restaurant(username, password, restaurantName, address);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // If the restaurant add any dish into the menu
                if(newMenu != null)
                {
                    newMenu.setRestaurantName(restaurantName);

                    // Store new menu information into the database
                    String restaurantKey = mDatabase.child("menus").push().getKey();
                    Map<String, Object> menuValues = newMenu.toMap();
                    Map<String, Object> childUpdates = new LinkedHashMap<>();
                    childUpdates.put("/menus/" +  restaurantKey, menuValues);
                    mDatabase.updateChildren(childUpdates);
                }

                // Store new restaurant information into the database
                String restaurantKey = mDatabase.child("restaurants").push().getKey();
                Map<String, Object> restaurantValues = newRestaurant.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/restaurants/" +  restaurantKey, restaurantValues);
                mDatabase.updateChildren(childUpdates);

                Intent intent = new Intent(RegisterRestaurantActivity.this,MainRestaurantActivity.class);
                intent.putExtra("currentRestaurant", (Serializable) newRestaurant);
                intent.putExtra("newMenu", (Serializable) newMenu);
                startActivity(intent);

                finish();
            }
        });

        Button btn_back = findViewById(R.id.bt_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterRestaurantActivity.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    public void show()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("ADD DISH");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_menu);
        dialog.show();


        add = (Button) dialog.findViewById(R.id.addDish);

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //diaplay.setText(dish.getText().toString());
                dialog.dismiss();
            }
        });

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
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterRestaurantActivity.this);
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
}

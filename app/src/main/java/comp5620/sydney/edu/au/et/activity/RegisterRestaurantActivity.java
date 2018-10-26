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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.MyMenuAdapter;
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
    private Button addMenu_dialog;
    private List<Map<String, String>> myDishes;
    private String menuID;
    private ListView menuDisplay_iv;
    private MyMenuAdapter myMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);

        allCustomers =  (ArrayList<Customer>) getIntent().getSerializableExtra("allCustomers");
        allRestaurants =  (ArrayList<Restaurant>) getIntent().getSerializableExtra("allRestaurants");
        newMenu = new Menu();
        myDishes = new ArrayList<>();
        menuID = "";

        menuDisplay_iv = (ListView) findViewById(R.id.menuDisplay);
        myMenuAdapter = new MyMenuAdapter(RegisterRestaurantActivity.this,0, myDishes);
        menuDisplay_iv.setAdapter(myMenuAdapter);

        Button addMenu = (Button) findViewById(R.id.addMenu);
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddMenu();
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

                if(username.length() < 2)
                {
                    // The username is not good
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterRestaurantActivity.this);
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
                    if(restaurantName.equals(restaurant.getRestaurantName()))
                    {
                        // The restaurant name is already exist
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterRestaurantActivity.this);
                        builder.setTitle("Restaurant name already exist")
                                .setMessage("Sorry, the restaurant name is already exist.")
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

                newRestaurant = new Restaurant(username, password, address, restaurantName);

                newMenu.setRestaurantName(restaurantName);
                newMenu.setRestaurantAddress(address);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // Store new menu information into the database
                if(menuID.equals("")) {
                    menuID = mDatabase.child("menus").push().getKey();
                }
                newMenu.setMenuID(menuID);
                Map<String, Object> menuValues = newMenu.toMap();
                Map<String, Object> childUpdates_menu = new LinkedHashMap<>();
                childUpdates_menu.put("/menus/" +  menuID, menuValues);
                mDatabase.updateChildren(childUpdates_menu);

                // Store new restaurant information into the database
                String restaurantKey = mDatabase.child("restaurants").push().getKey();
                Map<String, Object> restaurantValues = newRestaurant.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/restaurants/" +  restaurantKey, restaurantValues);
                mDatabase.updateChildren(childUpdates);

                Intent intent = new Intent(RegisterRestaurantActivity.this,MainRestaurantActivity.class);
                intent.putExtra("currentRestaurant", (Serializable) newRestaurant);
                intent.putExtra("myMenu", (Serializable) newMenu);
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

                    menuID = mDatabase.child("menus").push().getKey();
                    String dishKey = mDatabase.child("menus").child(menuID).child("dishes").push().getKey();
                    Map<String, String> detail = new LinkedHashMap<>();
                    detail.put("name", dishName);
                    detail.put("price", "$" + dishPrice);
                    detail.put("flavour", dishFlavour);
                    newMenu.dishes.put(dishKey, detail);
                    newMenu.setMenuID(menuID);
                    myDishes.add(detail);
                    myMenuAdapter.notifyDataSetChanged();
                }
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

package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import comp5620.sydney.edu.au.et.model.Restaurant;

public class RegisterCustomerActivity extends Activity {

    private List<Customer> allCustomers;
    private List<Restaurant> allRestaurants;
    private Customer newCustomer;
    private String gender;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        allCustomers =  (ArrayList<Customer>) getIntent().getSerializableExtra("allCustomers");
        allRestaurants =  (ArrayList<Restaurant>) getIntent().getSerializableExtra("allRestaurants");

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
                radioButton = (RadioButton)findViewById(gender_rg.getCheckedRadioButtonId());
                gender = radioButton.getText().toString();

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

                String pattern = "^\\d{10}$";

                boolean isValidPhone = Pattern.matches(pattern, phone);

                if(!isValidPhone)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCustomerActivity.this);
                    builder.setTitle("Phone number is wrong")
                            .setMessage("Wrong phone number format.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Back to the register page
                                }
                            });

                    builder.create().show();
                    return;
                }

                gender_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radioButton = (RadioButton)findViewById(group.getCheckedRadioButtonId());
                        gender = radioButton.getText().toString();
                    }
                });

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

                newCustomer = new Customer(username, password, surname, givenName, phone, address, gender);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // Store new restaurant information into the database
                String customerKey = mDatabase.child("customers").push().getKey();
                Map<String, Object> customerValues = newCustomer.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/customers/" +  customerKey, customerValues);
                mDatabase.updateChildren(childUpdates);

                Intent intent = new Intent(RegisterCustomerActivity.this,MainCustomerActivity.class);
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
}

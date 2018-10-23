package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Restaurant;

public class RegisterActivity extends Activity {

    private List<Customer> allCustomers;
    private List<Restaurant> allRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        allCustomers =  (ArrayList<Customer>) getIntent().getSerializableExtra("allCustomers");
        allRestaurants =  (ArrayList<Restaurant>) getIntent().getSerializableExtra("allRestaurants");

        Button registerRe = (Button) findViewById(R.id.newR);
        registerRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,RegisterRestaurantActivity.class);
                intent.putExtra("allCustomers", (Serializable) allCustomers);
                intent.putExtra("allRestaurants", (Serializable) allRestaurants);
                startActivity(intent);

                finish();
            }
        });

        Button registerCu = (Button) findViewById(R.id.newC);
        registerCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,RegisterCustomerActivity.class);
                intent.putExtra("allCustomers", (Serializable) allCustomers);
                intent.putExtra("allRestaurants", (Serializable) allRestaurants);
                startActivity(intent);

                finish();
            }
        });

        ImageButton btn_back = findViewById(R.id.bt_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}

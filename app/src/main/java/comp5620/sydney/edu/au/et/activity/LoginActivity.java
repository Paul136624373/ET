package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import comp5620.sydney.edu.au.et.R;

public class LoginActivity extends Activity {

    private RadioGroup rg;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rg = (RadioGroup)findViewById(R.id.radioGroup);
        type= "customer";

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radio = rg.getCheckedRadioButtonId();

                if(radio == R.id.rbLoin_restaurant)
                {
                    type = "restaurant";
                }
                else if(radio == R.id.rbLogin_customer)
                {
                    type = "customer";
                }
            }
        });

        Button login = (Button) findViewById(R.id.btLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(type.equals("customer"))
                    startActivity(new Intent(LoginActivity.this,MainCustomerActivity.class));
                else if (type.equals("restaurant"))
                    startActivity(new Intent(LoginActivity.this,MainRestaurantActivity.class));
            }
        });

        Button register = (Button) findViewById(R.id.btRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }
}

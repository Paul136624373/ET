package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comp5620.sydney.edu.au.et.R;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerRe = (Button) findViewById(R.id.newR);
        registerRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,RegisterRestaurantActivity.class));
            }
        });

        Button registerCu = (Button) findViewById(R.id.newC);
        registerCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,RegisterCustomerActivity.class));
            }
        });
    }
}

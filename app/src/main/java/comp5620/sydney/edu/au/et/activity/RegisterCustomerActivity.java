package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comp5620.sydney.edu.au.et.R;

public class RegisterCustomerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        Button saveCu = (Button) findViewById(R.id.saveCu);
        saveCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterCustomerActivity.this,MainCustomerActivity.class));

            }
        });
    }
}

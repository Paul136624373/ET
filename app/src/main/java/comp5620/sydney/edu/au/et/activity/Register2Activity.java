package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comp5620.sydney.edu.au.et.R;

public class Register2Activity extends Activity {

    //private TextView display;
/*
    private EditText dish;


    private ListView hh;
*/
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

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

            startActivity(new Intent(Register2Activity.this,Main2Activity.class));

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
        //dish = (EditText) dialog.findViewById(R.id.edDish);
       // diaplay = (ListView)findViewById(R.id.menuDisplay);

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //diaplay.setText(dish.getText().toString());
                dialog.dismiss();
            }
        });

    }


}

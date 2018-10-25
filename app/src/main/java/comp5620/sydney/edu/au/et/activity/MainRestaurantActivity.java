package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import comp5620.sydney.edu.au.et.R;

public class MainRestaurantActivity extends Activity {

    private TextView topBar_text;
    private ListView displayAllReservation;
    private ListView displayAllComment;
    private ListView displayAllMenu;
    private ImageButton add_menu;
    private Button addMenu_dialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            ConstraintLayout restaurant_profile = findViewById(R.id.restaurant_profile);
            topBar_text = (TextView)findViewById(R.id.topBar_text);
            displayAllComment = (ListView)findViewById(R.id.displayAllComment);
            displayAllMenu = (ListView)findViewById(R.id.displayAllMenu);
            displayAllReservation = (ListView)findViewById(R.id.displayAllMenu);

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

                    topBar_text.setText("Received comments");
                    return true;

                case R.id.navigation_restaurant_menu:
                    restaurant_profile.setVisibility(View.INVISIBLE);
                    displayAllReservation.setVisibility(View.INVISIBLE);
                    displayAllComment.setVisibility(View.INVISIBLE);
                    displayAllMenu.setVisibility(View.VISIBLE);
                    add_menu.setVisibility(View.VISIBLE);

                    topBar_text.setText("Menu");
                    return true;

                case R.id.navigation_restaurant_profile:
                    restaurant_profile.setVisibility(View.VISIBLE);
                    displayAllReservation.setVisibility(View.INVISIBLE);
                    displayAllComment.setVisibility(View.INVISIBLE);
                    displayAllMenu.setVisibility(View.INVISIBLE);
                    add_menu.setVisibility(View.INVISIBLE);

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void showAddMenu()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("ADD Dish");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_menu);
        dialog.show();


        addMenu_dialog = (Button) dialog.findViewById(R.id.addDish);

        addMenu_dialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //diaplay.setText(dish.getText().toString());
                dialog.dismiss();
            }
        });

    }

}

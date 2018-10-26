package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.RestaurantComment;

public class RestaurantCommentActivity extends Activity {

    private ImageButton btn_back;
    private  Button btn_submit;
    private Group theGroup;
    private Customer currentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_comment);

        theGroup =  (Group) getIntent().getSerializableExtra("theGroup");
        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit = findViewById(R.id.btn_submit);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // Get the id of the comment for the post
                String key = mDatabase.child("comments").push().getKey();

                EditText content = findViewById(R.id.editText);

                String comment_content = content.getText().toString();

                String creation_time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

                RestaurantComment newRestaurantComment = new RestaurantComment();
                newRestaurantComment.setCreationTime(creation_time);
                newRestaurantComment.setRestaurantName(theGroup.getRestaurantName());
                newRestaurantComment.setAuthor(currentCustomer.getUsername());
                newRestaurantComment.setContent(comment_content);
                newRestaurantComment.setGroupID(theGroup.getGroupID());

                Map<String, Object> restaurantCommentValues = newRestaurantComment.toMap();

                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/comments/" +  key, restaurantCommentValues);
                mDatabase.updateChildren(childUpdates);

                finish();
            }
        });
    }
}

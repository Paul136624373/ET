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
import java.util.List;
import java.util.Map;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Post;

public class NewCommentActivity extends Activity {

    private ImageButton btn_back;
    private ImageButton btn_save;
    private Post thePost;
    private Customer currentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);

        thePost =  (Post) getIntent().getSerializableExtra("thePost");
        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");

        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.bt_save);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // Get the id of the comment for the post
                String key = mDatabase.child("posts").child(thePost.id).child("comments").push().getKey();

                Map<String, String> comments_detail = new LinkedHashMap<>();

                EditText content = findViewById(R.id.editText);

                String comment_content = content.getText().toString();

                String creation_time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

                comments_detail.put("name", currentCustomer.getUsername());
                comments_detail.put("content", comment_content);
                comments_detail.put("time", creation_time);

                thePost.comments.put(key, comments_detail);

                Map<String, Object> postValues = thePost.toMap();

                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/posts/" +  thePost.id, postValues);
                mDatabase.updateChildren(childUpdates);

                Intent intent = new Intent();
                intent.putExtra("thePost", (Serializable) thePost);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}

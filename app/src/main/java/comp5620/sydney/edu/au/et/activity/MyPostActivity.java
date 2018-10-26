package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.MyPostsAdapter;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Post;

import static android.support.constraint.Constraints.TAG;

public class MyPostActivity extends Activity {

    private ListView myPost_lv;
    private MyPostsAdapter mPostAdapter;
    private List<Post> myPosts;
    private View btn_back;
    private Post deletePost;
    private Customer currentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        btn_back = findViewById(R.id.btn_back);

        myPosts =  (ArrayList<Post>) getIntent().getSerializableExtra("myPosts");
        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");

        initMyPost();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initMyPost() {
        myPost_lv = findViewById(R.id.myPost_lv);

        // Read data from database and create the views of post and message
        mPostAdapter = new MyPostsAdapter(MyPostActivity.this,0,myPosts);

        // Connect the listView and the adapter
        myPost_lv.setAdapter(mPostAdapter);

        mPostAdapter.setOnItemClickListener(new MyPostsAdapter.onItemClickListener() {
            @Override
            public void onDeleteClick(Post thePost) {
                deletePost = thePost;
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPostActivity.this);
                builder.setTitle("Delete the post")
                        .setMessage("Do you want to delete this post?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference data = mReference.child("posts").child(deletePost.id);

                                Log.w(TAG, "Delete post id" + deletePost.id);

                                data.removeValue();
                                myPosts.remove(deletePost);
                                mPostAdapter.notifyDataSetChanged();
                                deletePost = null;
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog and nothing will be deleted.
                                deletePost = null;
                            }
                        });

                builder.create().show();
            }

            @Override
            public void onViewClick(Post thePost) {
                Intent intent = new Intent(MyPostActivity.this, ViewPostActivity.class);
                intent.putExtra("thePost", (Serializable) thePost);
                intent.putExtra("currentCustomer",(Serializable) currentCustomer);
                startActivity(intent);
            }
        });
    }
}

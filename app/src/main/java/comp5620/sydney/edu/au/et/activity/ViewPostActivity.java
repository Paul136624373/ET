package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.adapter.CommentAdapter;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Post;


public class ViewPostActivity extends Activity {

    private Post thePost;
    private ImageButton btn_back;
    private LinearLayout.LayoutParams param;
    private CommentAdapter commentAdapter;
    private List<Map<String, String>> allComments;
    private ListView comments_list;
    private Customer currentCustomer;
    public final int CREATE_COMMENT_REQUEST_CODE = 649;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        thePost =  (Post) getIntent().getSerializableExtra("thePost");
        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");

        TextView username_tv = findViewById(R.id.username);
        TextView creationTime_tv = findViewById(R.id.creation_time);
        ImageView photo_iv = (ImageView) findViewById(R.id.user_photo);

        photo_iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_photo));

        username_tv.setText(thePost.author);
        creationTime_tv.setText(thePost.time);

        LinearLayout linear = (LinearLayout) findViewById(R.id.post_detail);
        param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        String content = thePost.body;
        // Even index is text, odd index is picture
        String[] contents = content.split("<ANIMG>");

        for(int i = 0; i < contents.length; i++)
        {
            // For text
            if(i % 2 == 0)
            {
                TextView newTextView = new TextView(ViewPostActivity.this);
                newTextView.setPadding(20, 10, 20, 10);
                newTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                linear.addView(newTextView, param);

                newTextView.setText(contents[i]);
            }
            else
            {
                final ImageView newImage = new ImageView(ViewPostActivity.this);
                newImage.setPadding(20, 10, 20, 10);
                linear.addView(newImage, param);

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference theImgRef = storageRef.child(contents[i]);
                final long ONE_MEGABYTE = 10000 * 10000;

                theImgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data is returns, use this as needed
                        newImage.setImageBitmap(Bytes2Bitmap(bytes));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        }

        // For thump up part
        String thumpupUsers = "";

        for (Map.Entry<String, String> entry : thePost.thumbups.entrySet()) {
            // List all users who thumps up this post
            if(entry.getValue() != "")
            {
                thumpupUsers = thumpupUsers + entry.getKey() + ", ";
            }
        }

        if(!thumpupUsers.equals("")) {
            thumpupUsers = thumpupUsers.substring(0, thumpupUsers.length() - 2);

            thumpupUsers = thumpupUsers + "\nhas thumbed up this post";
        }
        else {
            thumpupUsers = thumpupUsers + "Nobody has thumbed up this post.";
        }

        TextView thumpup_users = findViewById(R.id.thumpup_users);
        thumpup_users.setText(thumpupUsers);

        // For comments part
        allComments = new ArrayList<>();
        for (Map<String, String> value : thePost.comments.values()) {
            allComments.add(value);
        }

        comments_list = (ListView) findViewById(R.id.comments_list);
        sortComments(allComments);

        commentAdapter = new CommentAdapter(ViewPostActivity.this,0,allComments);
        comments_list.setAdapter(commentAdapter);

        comments_list.setFocusable(false);
        comments_list.setDivider(new ColorDrawable(Color.TRANSPARENT));
        comments_list.setDividerHeight(18);

        setListViewHeightBasedOnChildren(comments_list);
//        if(thePost.comments.values().size() == 0)
//        {
//            TextView tips = new TextView(ViewPost.this);
//
//            LinearLayout linear_comment = (LinearLayout) findViewById(R.id.post_comments);
//
//            tips.setPadding(20, 10, 20, 10);
//            tips.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//            linear_comment.addView(tips, param);
//
//            tips.setText("Nobody has replied to this post yet.");
//        }

        Button comment_btn = (Button) findViewById(R.id.bt_comment);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPostActivity.this,NewCommentActivity.class);
                intent.putExtra("thePost", (Serializable) thePost);
                intent.putExtra("currentCustomer",(Serializable) currentCustomer);
                startActivityForResult(intent, CREATE_COMMENT_REQUEST_CODE);
            }
        });
    }

    // Transfer byte[] to bitmao
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_COMMENT_REQUEST_CODE) {
            thePost = (Post) data.getSerializableExtra("thePost");

            allComments.clear();
            for (Map<String, String> value : thePost.comments.values()) {
                allComments.add(value);
            }

            sortComments(allComments);
            setListViewHeightBasedOnChildren(comments_list);
            commentAdapter.notifyDataSetChanged();
        }
    }

    // Solve the problem of ListView in ScrollView
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+listView.getPaddingTop()+listView.getPaddingBottom() + 50;
        listView.setLayoutParams(params);
    }

    // Old post will be showed at first
    public void sortComments(List<Map<String, String>> allComments)
    {
        for(int i = 0; i < allComments.size(); i++) {
            for(int j = 0; j < allComments.size() - i - 1; j++)
            {
                Map<String, String> temp = new LinkedHashMap<>();
                String firstTime_string = allComments.get(j).get("time");
                firstTime_string = firstTime_string.replace("/", "");
                firstTime_string = firstTime_string.replace(" ", "");
                firstTime_string = firstTime_string.replace(":", "");
                double firstTime = Double.parseDouble(firstTime_string);

                String secondTime_string = allComments.get(j + 1).get("time");
                secondTime_string = secondTime_string.replace("/", "");
                secondTime_string = secondTime_string.replace(" ", "");
                secondTime_string = secondTime_string.replace(":", "");
                double secondTime = Double.parseDouble(secondTime_string);

                if (firstTime > secondTime) {
                    temp = allComments.get(j);
                    allComments.set(j, allComments.get(j + 1));
                    allComments.set(j + 1, temp);
                }
            }
        }
    }

}


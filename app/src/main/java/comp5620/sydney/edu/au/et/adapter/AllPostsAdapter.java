package comp5620.sydney.edu.au.et.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Post;


public class AllPostsAdapter extends ArrayAdapter<Post> {

    private Context context;
    private List<Post> postItem;
    private Customer currentCustomer;

    public AllPostsAdapter(Context context, int resourceId, List<Post> objects, Customer currentCustomer) {
        super(context, resourceId, objects);

        this.context = context;
        this.postItem = objects;
        this.currentCustomer = currentCustomer;
    }

    @Override
    public int getCount() {
        return postItem.size();
    }

    @Override
    public Post getItem(int position) {
        return postItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Post onePost = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_post_display, parent, false);
        }

        ImageView photo_iv = (ImageView) convertView.findViewById(R.id.user_photo);
        TextView username_tv = (TextView) convertView.findViewById(R.id.username);
        TextView creation_time_tv = (TextView) convertView.findViewById(R.id.creation_time);
        TextView content_tv = (TextView) convertView.findViewById(R.id.post_content);
        ImageView content_image_iv = (ImageView) convertView.findViewById(R.id.content_image);


        photo_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user_photo));
        username_tv.setText(onePost.author);
        creation_time_tv.setText(onePost.time);

        String all_content = onePost.body;
        // Even index is text, odd index is picture
        String[] contents = all_content.split("<ANIMG>");

        // If the first part of text contains less than 20 letters
        if(contents[0].length() < 50)
        {
            content_tv.setText(contents[0]);
        }
        else
        {
            content_tv.setText(contents[0].substring(0, 50) + "...");
        }

        // If there is at least one picture
        if(contents.length > 1)
        {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference theImgRef = storageRef.child(contents[1]);

            Glide.with(this.getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(theImgRef)
                    .into(content_image_iv);
        }
        else
        {
            content_image_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.noimage));
        }

        LinearLayout post_content_ll = (LinearLayout) convertView.findViewById(R.id.post_content_ll);
        LinearLayout content_image_ll = (LinearLayout) convertView.findViewById(R.id.content_image_ll);
        Button comment_btn = (Button) convertView.findViewById(R.id.bt_comment);
        Button thumpup_btn = (Button) convertView.findViewById(R.id.bt_thumpup);

        if(onePost.thumbups.containsKey(currentCustomer.getUsername()) && !onePost.thumbups.get(currentCustomer.getUsername()).equals("")) {
            Drawable thumbup_red=context.getResources().getDrawable(R.drawable.ic_like_after);

            thumbup_red.setBounds(0, 0, thumbup_red.getMinimumWidth(), thumbup_red.getMinimumHeight());

            thumpup_btn.setCompoundDrawables(thumbup_red, null, null, null);

            thumpup_btn.setTextColor(Color.RED);
        }
        else {
            Drawable thumbup_white=context.getResources().getDrawable(R.drawable.ic_like_before);

            thumbup_white.setBounds(0, 0, thumbup_white.getMinimumWidth(), thumbup_white.getMinimumHeight());

            thumpup_btn.setCompoundDrawables(thumbup_white, null, null, null);

            thumpup_btn.setTextColor(Color.BLACK);
        }

        post_content_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onViewClick(getItem(position));
            }
        });

        content_image_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onViewClick(getItem(position));
            }
        });

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onCommentClick(getItem(position));
            }
        });

        thumpup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onThumpupClick(getItem(position));
            }
        });

        return convertView;
    }

    // Set listener for each post
    public interface onItemClickListener {
        void onCommentClick(Post thePost);
        void onThumpupClick(Post thePost);
        void onViewClick(Post thePost);
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener mOnItemDeleteListener) {
        this.mOnItemClickListener = mOnItemDeleteListener;
    }
}
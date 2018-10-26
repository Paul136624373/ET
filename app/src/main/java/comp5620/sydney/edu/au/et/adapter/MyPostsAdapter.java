package comp5620.sydney.edu.au.et.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import comp5620.sydney.edu.au.et.model.Post;

import static android.support.constraint.Constraints.TAG;

public class MyPostsAdapter extends ArrayAdapter<Post> {

    private Context context;
    private List<Post> postItem;

    public MyPostsAdapter(Context context,int resourceId, List<Post> objects) {
        super(context, resourceId, objects);

        this.context = context;
        this.postItem = objects;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_post, parent, false);
        }

        ImageView photo_iv = (ImageView) convertView.findViewById(R.id.user_photo);
        TextView username_tv = (TextView) convertView.findViewById(R.id.username);
        TextView creation_time_tv = (TextView) convertView.findViewById(R.id.creation_time);
        TextView content_tv = (TextView) convertView.findViewById(R.id.post_content);
        ImageView content_image_iv = (ImageView) convertView.findViewById(R.id.content_image);
        Button delete_post_btn = (Button) convertView.findViewById(R.id.bt_delete);
        delete_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onDeleteClick(getItem(position));
            }
        });

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
        Button view_post_btn = (Button) convertView.findViewById(R.id.bt_view);

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

        view_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onViewClick(getItem(position));
            }
        });

        return convertView;
    }

    public interface onItemClickListener {
        void onDeleteClick(Post thePost);
        void onViewClick(Post thePost);
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener mOnItemDeleteListener) {
        this.mOnItemClickListener = mOnItemDeleteListener;
    }
}
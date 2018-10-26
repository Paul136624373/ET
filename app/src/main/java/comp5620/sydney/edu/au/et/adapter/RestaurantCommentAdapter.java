package comp5620.sydney.edu.au.et.adapter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Restaurant;
import comp5620.sydney.edu.au.et.model.RestaurantComment;


public class RestaurantCommentAdapter extends ArrayAdapter<RestaurantComment> {

    private Context context;
    private List<RestaurantComment> commentItem;

    public RestaurantCommentAdapter(Context context, int resourceId, List<RestaurantComment> objects) {
        super(context, resourceId, objects);

        this.context = context;
        this.commentItem = objects;
    }

    @Override
    public int getCount() {
        return commentItem.size();
    }

    @Override
    public RestaurantComment getItem(int position) {
        return commentItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RestaurantComment oneRestaurantComment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_comment, parent, false);
        }

        TextView user_tv = (TextView) convertView.findViewById(R.id.user);
        TextView comment_tv = (TextView) convertView.findViewById(R.id.comment);

        user_tv.setText(oneRestaurantComment.getAuthor());
        comment_tv.setText(oneRestaurantComment.getContent());

        return convertView;
    }
}
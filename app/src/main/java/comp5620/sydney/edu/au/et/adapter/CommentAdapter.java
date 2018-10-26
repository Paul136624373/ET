package comp5620.sydney.edu.au.et.adapter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import static android.support.constraint.Constraints.TAG;

public class CommentAdapter extends ArrayAdapter<Map<String, String>> {

    private Context context;
    private List<Map<String, String>> all_comments;

    public CommentAdapter(Context context,int resourceId, List<Map<String, String>> objects) {
        super(context, resourceId, objects);

        this.context = context;
        this.all_comments = objects;
    }

    @Override
    public int getCount() {
        return all_comments.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return all_comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Map<String, String> oneComment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_comments, parent, false);
        }

        TextView comment_name = (TextView) convertView.findViewById(R.id.comment_name);
        TextView comment_creation_time = (TextView) convertView.findViewById(R.id.comment_creation_time);
        TextView comment_content = (TextView) convertView.findViewById(R.id.comment_content);

        comment_name.setText(oneComment.get("name"));
        comment_creation_time.setText(oneComment.get("time"));
        comment_content.setText(oneComment.get("content"));

        return convertView;
    }
}
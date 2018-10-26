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

public class MyMenuAdapter extends ArrayAdapter<Map<String, String>> {

    private Context context;
    private List<Map<String, String>> allDishes;

    public MyMenuAdapter(Context context,int resourceId, List<Map<String, String>> objects) {
        super(context, resourceId, objects);

        this.context = context;
        this.allDishes = objects;
    }

    @Override
    public int getCount() {
        return allDishes.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return allDishes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Map<String, String> oneDish = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_menu, parent, false);
        }

        TextView dishname_tv = (TextView) convertView.findViewById(R.id.dishname);
        TextView dishprice_tv = (TextView) convertView.findViewById(R.id.dishprice);
        TextView dishflavor_tv = (TextView) convertView.findViewById(R.id.dishflavor);

        dishname_tv.setText(oneDish.get("name"));
        dishprice_tv.setText(oneDish.get("price"));
        dishflavor_tv.setText(oneDish.get("flavour"));

        return convertView;
    }
}
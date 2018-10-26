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


public class ReservationAdapter extends ArrayAdapter<Group> {

    private Context context;
    private List<Group> groupItem;

    public ReservationAdapter(Context context, int resourceId, List<Group> objects) {
        super(context, resourceId, objects);

        this.context = context;
        this.groupItem = objects;
    }

    @Override
    public int getCount() {
        return groupItem.size();
    }

    @Override
    public Group getItem(int position) {
        return groupItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Group oneGroup = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_reservation_display, parent, false);
        }

        TextView username_tv = (TextView) convertView.findViewById(R.id.username);
        TextView phone_tv = (TextView) convertView.findViewById(R.id.phone);
        TextView number_of_people_tv = (TextView) convertView.findViewById(R.id.number_of_people);
        TextView eating_time_tv = (TextView) convertView.findViewById(R.id.eating_time);

        username_tv.setText(oneGroup.getOwner());
        phone_tv.setText(oneGroup.getOwnerPhone());
        number_of_people_tv.setText(oneGroup.getNumberOfPeople());
        eating_time_tv.setText(oneGroup.getEatingTime());

        return convertView;
    }
}
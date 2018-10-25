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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Group;


public class ShowGroupAdapter extends ArrayAdapter<Group> {

    private Context context;
    private List<Group> groupItem;
    private Customer currentCustomer;

    public ShowGroupAdapter(Context context, int resourceId, List<Group> objects, Customer currentCustomer) {
        super(context, resourceId, objects);

        this.context = context;
        this.groupItem = objects;
        this.currentCustomer = currentCustomer;
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
        String contact = "";

        // Get the members' information
        for (Map<String, String> member : oneGroup.members.values()) {
            String username = "";
            String phone = "";
            for (Map.Entry<String, String> field : member.entrySet()) {
                if(field.getKey().equals("username"))
                {
                    username = field.getValue();
                }
                if(field.getKey().equals("phone"))
                {
                    phone = field.getValue();
                }
            }
            contact = contact + username + ": " + phone + "\n";
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_display_group, parent, false);
        }

        TextView number_of_people_tv = (TextView) convertView.findViewById(R.id.number_of_people);
        TextView time_for_eating_tv = (TextView) convertView.findViewById(R.id.time_for_eating);
        TextView contact_tv = (TextView) convertView.findViewById(R.id.contact);
        TextView show_time_tv = (TextView) convertView.findViewById(R.id.show_time);
        Button join_btn = (Button) convertView.findViewById(R.id.btJoin);

        String currentPeople = oneGroup.members.size() + "";
        String maxPeople = oneGroup.getNumberOfPeople();
        String numberOfPeople = currentPeople + "/" + maxPeople;
        String timeOfEating = oneGroup.getEatingTime();
        String creationTime = oneGroup.getCreationTime();

        number_of_people_tv.setText(numberOfPeople);
        time_for_eating_tv.setText(timeOfEating);
        contact_tv.setText(contact);
        show_time_tv.setText(creationTime);

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Join a group
                Group theGroup = getItem(position);

                final Dialog dialog = new Dialog(context);
                dialog.setTitle("Choose Flavour");
                dialog.setCancelable(true);
                final View layout = View.inflate(context, R.layout.ask_flavor,null);
                dialog.setContentView(layout);
                dialog.show();


                Button add = (Button) dialog.findViewById(R.id.chooseFlavour);

                add.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        //diaplay.setText(dish.getText().toString());
                        dialog.dismiss();

                        Group theGroup = getItem(position);

                        RadioGroup flavour_rg = (RadioGroup)layout.findViewById(R.id.flavor_rg);
                        RadioButton flavour_rb = (RadioButton)layout.findViewById(flavour_rg.getCheckedRadioButtonId());
                        String flavour = flavour_rb.getText().toString();

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        // Add a new member into the group
                        String memberKey = mDatabase.child("groups").child(theGroup.getGroupID()).child("members").push().getKey();

                        Map<String, String> members_detail = new LinkedHashMap<>();

                        members_detail.put("username", currentCustomer.getUsername());
                        members_detail.put("phone", currentCustomer.getPhoneNumber());
                        members_detail.put("flavour", flavour);

                        theGroup.members.put(memberKey, members_detail);

                        Map<String, Object> theGroupValues = theGroup.toMap();

                        Map<String, Object> theChildUpdates = new LinkedHashMap<>();
                        theChildUpdates.put("/groups/" +  theGroup.getGroupID(), theGroupValues);
                        mDatabase.updateChildren(theChildUpdates);

                    }
                });
            }
        });


        return convertView;
    }
}
package comp5620.sydney.edu.au.et.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Post;
import comp5620.sydney.edu.au.et.model.RestaurantComment;

import static android.support.constraint.Constraints.TAG;

public class MyGroupsAdapter extends ArrayAdapter<Group> {

    private Context context;
    private List<Group> groupItem;
    private List<Group> ownedGroups;
    private List<Group> joinedGroups;
    private Customer currentCustomer;
    private List<RestaurantComment> allRestaurantComments;

    public MyGroupsAdapter(Context context, int resourceId, List<Group> myGroups, List<Group> ownedGroups, List<Group> joinedGroups, Customer currentCustomer, List<RestaurantComment> allRestaurantComments) {
        super(context, resourceId, myGroups);

        this.context = context;
        this.groupItem = myGroups;
        this.ownedGroups = ownedGroups;
        this.joinedGroups = joinedGroups;
        this.currentCustomer = currentCustomer;
        this.allRestaurantComments = allRestaurantComments;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_my_group, parent, false);
        }


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
        contact = contact.trim();

        LinearLayout bordor_ll = (LinearLayout) convertView.findViewById(R.id.border);
        TextView number_of_people_tv = (TextView) convertView.findViewById(R.id.number_of_people);
        TextView owner_tv = (TextView) convertView.findViewById(R.id.owner);
        TextView time_for_eating_tv = (TextView) convertView.findViewById(R.id.time_for_eating);
        TextView contact_tv = (TextView) convertView.findViewById(R.id.contact);
        TextView restaurant_name_tv = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView location_tv = (TextView) convertView.findViewById(R.id.location);
        TextView show_time_tv = (TextView) convertView.findViewById(R.id.show_time);
        Button btConfirm = (Button) convertView.findViewById(R.id.btConfirm);
        Button btComment = (Button) convertView.findViewById(R.id.btComment);

        String currentPeople = oneGroup.members.size() + "";
        String maxPeople = oneGroup.getNumberOfPeople();
        String numberOfPeople = currentPeople + "/" + maxPeople;
        String timeOfEating = oneGroup.getEatingTime();
        String creationTime = oneGroup.getCreationTime();

        owner_tv.setText(oneGroup.getOwner());
        number_of_people_tv.setText(numberOfPeople);
        time_for_eating_tv.setText(timeOfEating);
        contact_tv.setText(contact);
        show_time_tv.setText(creationTime);

        if(oneGroup.getOwner().equals(currentCustomer.getUsername()))
        {
            bordor_ll.setBackground(context.getResources().getDrawable(R.drawable.my_group_border));
        }
        else
        {
            bordor_ll.setBackground(context.getResources().getDrawable(R.drawable.joined_group_border));
        }

        // If the owner has choose one restaurant
        if(oneGroup.getRestaurantName() != null)
        {
            btConfirm.setVisibility(View.GONE);
            btComment.setVisibility(View.GONE);
            restaurant_name_tv.setText(oneGroup.getRestaurantName());
            location_tv.setText(oneGroup.getRestaurantAddress());

            String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            String eatingTime = oneGroup.getEatingTime();
            eatingTime = eatingTime.replace("/", "");
            eatingTime = eatingTime.replace(" ", "");
            eatingTime = eatingTime.replace(":", "");

            boolean alreadyComment = false;
            for(RestaurantComment oneComment : allRestaurantComments)
            {
                // If the customer has comment this restaurant as a member in this group
                if(oneComment.getAuthor().equals(currentCustomer.getUsername()) && oneGroup.getGroupID().equals(oneComment.getGroupID()))
                {
                    alreadyComment = true;
                }
            }

            if(Long.parseLong(eatingTime) < Long.parseLong(currentTime) && !alreadyComment) {
                btComment.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            btComment.setVisibility(View.GONE);
            restaurant_name_tv.setText("No restaurant chosen");
            location_tv.setText("No restaurant chosen");

            String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            String eatingTime = oneGroup.getEatingTime();
            eatingTime = eatingTime.replace("/", "");
            eatingTime = eatingTime.replace(" ", "");
            eatingTime = eatingTime.replace(":", "");

            if(oneGroup.getOwner().equals(currentCustomer.getUsername()) && Long.parseLong(eatingTime) > Long.parseLong(currentTime))
            {
                btConfirm.setVisibility(View.VISIBLE);
            }
            else
            {
                btConfirm.setVisibility(View.GONE);
            }
        }


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onConfirmClick(getItem(position));
            }
        });

        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onCommentClick(getItem(position));
            }
        });

        return convertView;
    }

    public interface onItemClickListener {
        void onConfirmClick(Group theGroup);
        void onCommentClick(Group theGroup);
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
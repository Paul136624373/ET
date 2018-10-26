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
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Friend;

import static android.support.constraint.Constraints.TAG;

public class InviteFriendAdapter extends ArrayAdapter<Friend> {

    private Context context;
    private Customer currentCustomer;
    private List<Friend> allFriends;
    private List<Customer> allCustomers;

    public InviteFriendAdapter(Context context, int resourceId, List<Friend> myFriends, Customer currentCustomer, List<Customer> allCustomers) {
        super(context, resourceId, myFriends);

        this.context = context;
        this.currentCustomer = currentCustomer;
        this.allFriends = myFriends;
        this.allCustomers = allCustomers;
    }

    @Override
    public int getCount() {
        return allFriends.size();
    }

    @Override
    public Friend getItem(int position) {
        return allFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Friend oneFriend = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_friend_display, parent, false);
        }

        ImageView friend_iv = (ImageView) convertView.findViewById(R.id.friend_iv);
        TextView username_tv = (TextView) convertView.findViewById(R.id.username_tv);
        TextView phone_tv = (TextView) convertView.findViewById(R.id.phone_tv);

        if(oneFriend.getFirstUsername().equals(currentCustomer.getUsername()))
        {
            for( Customer oneCustomer : allCustomers)
            {
                if(oneFriend.getSecondUsername().equals(oneCustomer.getUsername()))
                {
                    if(oneCustomer.getGender().equals("Male"))
                    {
                        friend_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_friend_male));
                    }
                    else
                    {
                        friend_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_friend_female));
                    }
                    username_tv.setText(oneCustomer.getUsername());
                    phone_tv.setText(oneCustomer.getPhoneNumber());
                }
            }
        }
        else if(oneFriend.getSecondUsername().equals(currentCustomer.getUsername()))
        {
            for( Customer oneCustomer : allCustomers)
            {
                if(oneFriend.getFirstUsername().equals(oneCustomer.getUsername()))
                {
                    if(oneCustomer.getGender().equals("Male"))
                    {
                        friend_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_friend_male));
                    }
                    else
                    {
                        friend_iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_friend_female));
                    }
                    username_tv.setText(oneCustomer.getUsername());
                    phone_tv.setText(oneCustomer.getPhoneNumber());
                }
            }
        }

        return convertView;
    }
}
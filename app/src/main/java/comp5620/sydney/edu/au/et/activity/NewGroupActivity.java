package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.util.ListUpdateCallback;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Customer;
import comp5620.sydney.edu.au.et.model.Group;

public class NewGroupActivity extends Activity {

    private RadioGroup rg;
    private LinearLayout inviteButton;
    private ListView friendDisplay;
    private Group newGroup;
    private Customer currentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        currentCustomer = (Customer) getIntent().getSerializableExtra("currentCustomer");
        newGroup = new Group();

        rg = (RadioGroup)findViewById(R.id.rgType);
        inviteButton = (LinearLayout)findViewById(R.id.invite);
        friendDisplay = (ListView)findViewById(R.id.friendDisplay);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radio = rg.getCheckedRadioButtonId();

                if(radio == R.id.rbPublic)
                {
                    inviteButton.setVisibility(View.INVISIBLE);
                    friendDisplay.setVisibility(View.INVISIBLE);
                }
                else if(radio == R.id.rbPrivate)
                {
                    inviteButton.setVisibility(View.VISIBLE);
                    friendDisplay.setVisibility(View.VISIBLE);
                }
            }
        });

        Button inviteFriend = (Button) findViewById(R.id.inviteFriend);
        inviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        ImageButton saveGroup = (ImageButton) findViewById(R.id.group_save);
        saveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText numberOfPeople_et = findViewById(R.id.edNumber);
                EditText eatingTime_et = findViewById(R.id.edTime);
                RadioGroup flavour_rg = (RadioGroup)findViewById(R.id.rgFlavor);
                RadioGroup type_rg = (RadioGroup)findViewById(R.id.rgType);

                String numberOfPeople = numberOfPeople_et.getText().toString();
                String eatingTime = eatingTime_et.getText().toString();

                RadioButton flavour_rb = (RadioButton)findViewById(flavour_rg.getCheckedRadioButtonId());
                String flavour = flavour_rb.getText().toString();
                RadioButton type_rb = (RadioButton)findViewById(type_rg.getCheckedRadioButtonId());
                String type = type_rb.getText().toString();


                if(isEmpty("Number of people", numberOfPeople)) {
                    return;
                }
                if(isEmpty("Time of eating", eatingTime)) {
                    return;
                }

                // Validate number of people
                String pattern = "[0-9]+";

                boolean isValidNumber = Pattern.matches(pattern, numberOfPeople);

                if(!isValidNumber || numberOfPeople.length() > 2)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewGroupActivity.this);
                    builder.setTitle("Number of people is wrong")
                            .setMessage("Number of people can only contains 2 digits.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Back to the register page
                                }
                            });

                    builder.create().show();
                    return;
                }

                // Validate time for eating
                if(!isValidDate(eatingTime))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewGroupActivity.this);
                    builder.setTitle("Time for eating is wrong")
                            .setMessage("Time for eating must in yyyy/MM/dd HH:mm format.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Back to the register page
                                }
                            });

                    builder.create().show();
                    return;
                }

                // If it is a private group
                if(type.equals("Private"))
                {
                    if(newGroup.getInvites().size() == 0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewGroupActivity.this);
                        builder.setTitle("Please invite friends")
                                .setMessage("You need to invite at least one friend.")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Back to the register page
                                    }
                                });

                        builder.create().show();
                        return;
                    }
                }

                newGroup.setOwner(currentCustomer.getUsername());
                newGroup.setCreationTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
                newGroup.setEatingTime(eatingTime);
                newGroup.setNumberOfPeople(numberOfPeople);
                newGroup.setType(type);

                // Add a new member into the group
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                // Store new restaurant information into the database
                String groupKey = mDatabase.child("groups").push().getKey();
                newGroup.setGroupID(groupKey);
                Map<String, Object> groupValues = newGroup.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/groups/" +  groupKey, groupValues);
                mDatabase.updateChildren(childUpdates);

                String memberKey = mDatabase.child("groups").child(groupKey).child("members").push().getKey();

                Map<String, String> members_detail = new LinkedHashMap<>();

                members_detail.put("username", currentCustomer.getUsername());
                members_detail.put("phoneNumber", currentCustomer.getPhoneNumber());
                members_detail.put("flavour", flavour);

                newGroup.members.put(memberKey, members_detail);

                Map<String, Object> newGroupValues = newGroup.toMap();

                Map<String, Object> newChildUpdates = new LinkedHashMap<>();
                newChildUpdates.put("/groups/" +  groupKey, newGroupValues);
                mDatabase.updateChildren(newChildUpdates);


                finish();
            }
        });

        ImageButton btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void show()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("INVITE FRIEND");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.invite_friend);
        dialog.show();

    }

    // Show dialog on the screen if any field is empty
    public boolean isEmpty(String field, String value)
    {
        boolean empty = false;
        if(value.equals(""))
        {
            empty = true;
            // The field is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(NewGroupActivity.this);
            builder.setTitle("'" + field + "' is empty")
                    .setMessage("Sorry, '" + field + "' cannot be empty")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Back to the register page
                        }
                    });

            builder.create().show();
        }

        return empty;
    }

    // Validate date
    private static boolean isValidDate(String str) {
        boolean convertSuccess = true;

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

}

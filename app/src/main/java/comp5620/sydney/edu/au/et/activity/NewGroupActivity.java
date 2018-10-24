package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.util.ListUpdateCallback;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import comp5620.sydney.edu.au.et.R;
public class NewGroupActivity extends Activity {

    private RadioGroup rg;
    private LinearLayout inviteButton;
    private ListView friendDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

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

                startActivity(new Intent(NewGroupActivity.this,MainCustomerActivity.class));

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

}

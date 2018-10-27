package comp5620.sydney.edu.au.et.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import comp5620.sydney.edu.au.et.R;
import comp5620.sydney.edu.au.et.model.Group;
import comp5620.sydney.edu.au.et.model.Menu;

public class RecommendActivity extends Activity {

    private View btn_back;
    private List<Menu> threeBestMenus;
    private List<Integer> threeScores;
    private List<Menu> allMenus;
    private Group theGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        theGroup = (Group) getIntent().getSerializableExtra("theGroup");
        allMenus = (ArrayList<Menu>) getIntent().getSerializableExtra("allMenus");
        threeBestMenus = new ArrayList<>();
        threeScores = new ArrayList<>();

        algorithmForRecommendation();

        showScore();

        LinearLayout first_ll = findViewById(R.id.first_ll);
        LinearLayout second_ll = findViewById(R.id.second_ll);
        LinearLayout third_ll = findViewById(R.id.third_ll);

        first_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Group newGroup = theGroup;
                newGroup.setRestaurantName(threeBestMenus.get(0).getRestaurantName());
                newGroup.setRestaurantAddress(threeBestMenus.get(0).getRestaurantAddress());
                Map<String, Object> groupValues = newGroup.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/groups/" +  newGroup.getGroupID(), groupValues);
                mDatabase.updateChildren(childUpdates);

                finish();
            }
        });

        second_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Group newGroup = theGroup;
                newGroup.setRestaurantName(threeBestMenus.get(1).getRestaurantName());
                newGroup.setRestaurantAddress(threeBestMenus.get(1).getRestaurantAddress());
                Map<String, Object> groupValues = newGroup.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/groups/" +  newGroup.getGroupID(), groupValues);
                mDatabase.updateChildren(childUpdates);

                finish();
            }
        });

        third_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Group newGroup = theGroup;
                newGroup.setRestaurantName(threeBestMenus.get(2).getRestaurantName());
                newGroup.setRestaurantAddress(threeBestMenus.get(2).getRestaurantAddress());
                Map<String, Object> groupValues = newGroup.toMap();
                Map<String, Object> childUpdates = new LinkedHashMap<>();
                childUpdates.put("/groups/" +  newGroup.getGroupID(), groupValues);
                mDatabase.updateChildren(childUpdates);

                finish();
            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     *
         This is the recommendation algorithm for this application. High coupling because of complex classes. It will be modified in the future.
     *
     */
    public void algorithmForRecommendation() {
        Double cardinalNumber = theGroup.members.size() * 2.0;           // As one people cannot plus 2 score for one menu(Restaurant)
        Double scoreForOneStart = cardinalNumber / 5;
        // Count the people in the group
        int spicyPeople = 0;
        int acidPeople = 0;
        int sweetPeople = 0;
        int saltyPeople = 0;

        // Classify the people and count the number of each flavour
        for (Map<String, String> oneMember : theGroup.members.values()) {
            for (Map.Entry<String, String> oneField : oneMember.entrySet()) {
                if(oneField.getKey().equals("flavour"))
                {
                    if(oneField.getValue().equals("Spicy"))
                    {
                        spicyPeople ++;
                    }
                    else if(oneField.getValue().equals("Acid"))
                    {
                        acidPeople ++;
                    }
                    else if(oneField.getValue().equals("Sweet"))
                    {
                        sweetPeople ++;
                    }
                    else if(oneField.getValue().equals("Salty"))
                    {
                        saltyPeople ++;
                    }
                }
            }
        }

        // Use TreeMap for sorting
        TreeMap<String, Double> restaurantScore = new TreeMap<>();

        for(Menu oneMenu : allMenus) {
            int spicyDish = 0;
            int acidDish = 0;
            int sweetDish = 0;
            int saltyDish = 0;

            // Classify the dishes and count the number of each flavour
            for (Map<String, String> oneDish : oneMenu.dishes.values()) {
                for (Map.Entry<String, String> oneField : oneDish.entrySet()) {
                    if(oneField.getKey().equals("flavour"))
                    {
                        if(oneField.getValue().equals("Spicy"))
                        {
                            spicyDish ++;
                        }
                        else if(oneField.getValue().equals("Acid"))
                        {
                            acidDish ++;
                        }
                        else if(oneField.getValue().equals("Sweet"))
                        {
                            sweetDish++;
                        }
                        else if(oneField.getValue().equals("Salty"))
                        {
                            saltyDish++;
                        }
                    }
                }
            }


            // Calculate the score of this restaurant for this group
            double totalScore = 0;
            if(spicyPeople != 0) {
                double initScore = 1;
                for (int i = 0; i < spicyDish; i++)
                {
                    totalScore = totalScore + spicyPeople * initScore;
                    initScore = initScore / 2;
                }
            }
            if(acidPeople != 0) {
                double initScore = 1;
                for (int i = 0; i < acidDish; i++)
                {
                    totalScore = totalScore + acidPeople * initScore;
                    initScore = initScore / 2;
                }
            }
            if(sweetPeople != 0) {
                double initScore = 1;
                for (int i = 0; i < sweetDish; i++)
                {
                    totalScore = totalScore + sweetPeople * initScore;
                    initScore = initScore / 2;
                }
            }
            if(saltyPeople != 0) {
                double initScore = 1;
                for (int i = 0; i < saltyDish; i++)
                {
                    totalScore = totalScore + saltyPeople * initScore;
                    initScore = initScore / 2;
                }
            }

            restaurantScore.put(oneMenu.getRestaurantName(), totalScore);
        }

        // Sort the restaurant by score (Ascending)
        List<Map.Entry<String,Double>> scoreArrayList = new ArrayList<Map.Entry<String,Double>>(restaurantScore.entrySet());
        Collections.sort(scoreArrayList,new Comparator<Map.Entry<String,Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        // Record up to 3 restaurants and scores
        for(int i = scoreArrayList.size() - 1; i >= 0; i--)
        {
            for(Menu oneMenu : allMenus) {
                if (scoreArrayList.get(i).getKey().equals(oneMenu.getRestaurantName())) {
                    threeBestMenus.add(oneMenu);
                    Double starsInDouble = scoreArrayList.get(i).getValue() / scoreForOneStart;
                    Integer starsInInterger = (int) Math.round(starsInDouble);
                    threeScores.add(starsInInterger);
                }
            }

            if(threeBestMenus.size() == 3)
            {
                break;
            }
        }

    }

    public void showScore() {
        TextView first_restaurant_name_tv = findViewById(R.id.first_restaurant_name);
        TextView first_location_tv = findViewById(R.id.first_location);
        TextView second_restaurant_name_tv = findViewById(R.id.second_restaurant_name);
        TextView second_location_tv = findViewById(R.id.second_location);
        TextView third_restaurant_name_tv = findViewById(R.id.third_restaurant_name);
        TextView third_location_tv = findViewById(R.id.third_location);

        first_restaurant_name_tv.setText(threeBestMenus.get(0).getRestaurantName());
        first_location_tv.setText(threeBestMenus.get(0).getRestaurantAddress());
        second_restaurant_name_tv.setText(threeBestMenus.get(1).getRestaurantName());
        second_location_tv.setText(threeBestMenus.get(1).getRestaurantAddress());
        third_restaurant_name_tv.setText(threeBestMenus.get(2).getRestaurantName());
        third_location_tv.setText(threeBestMenus.get(2).getRestaurantAddress());

        ImageView first_star1 = findViewById(R.id.first_star1);
        ImageView first_star2 = findViewById(R.id.first_star2);
        ImageView first_star3 = findViewById(R.id.first_star3);
        ImageView first_star4 = findViewById(R.id.first_star4);
        ImageView first_star5 = findViewById(R.id.first_star5);
        ImageView second_star1 = findViewById(R.id.second_star1);
        ImageView second_star2 = findViewById(R.id.second_star2);
        ImageView second_star3 = findViewById(R.id.second_star3);
        ImageView second_star4 = findViewById(R.id.second_star4);
        ImageView second_star5 = findViewById(R.id.second_star5);
        ImageView third_star1 = findViewById(R.id.third_star1);
        ImageView third_star2 = findViewById(R.id.third_star2);
        ImageView third_star3 = findViewById(R.id.third_star3);
        ImageView third_star4 = findViewById(R.id.third_star4);
        ImageView third_star5 = findViewById(R.id.third_star5);


        for(int i=0; i<3; i++)
        {
            if(threeScores.get(i) == 1) {
                if(i==0) {
                    first_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==1){
                    second_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==2){
                    third_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
            }
            else if(threeScores.get(i) == 2){
                if(i==0) {
                    first_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==1){
                    second_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==2){
                    third_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
            }
            else if(threeScores.get(i) == 3){
                if(i==0) {
                    first_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==1){
                    second_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==2){
                    third_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
            }
            else if(threeScores.get(i) == 4){
                if(i==0) {
                    first_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==1){
                    second_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==2){
                    third_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
            }
            else if(threeScores.get(i) == 5){
                if(i==0) {
                    first_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    first_star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==1){
                    second_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    second_star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
                else if(i==2){
                    third_star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                    third_star5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_after));
                }
            }
        }
    }
}

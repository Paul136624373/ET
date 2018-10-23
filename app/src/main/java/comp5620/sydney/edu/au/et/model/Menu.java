package comp5620.sydney.edu.au.et.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Menu implements Serializable {

    private String restaurantName;
    private Map<String, Map<String, String>> dishes = new LinkedHashMap<>();

    public Menu() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Menu(String restaurantName, Map<String, Map<String, String>> dishes) {
        this.restaurantName = restaurantName;
        this.dishes = dishes;
    }

    @Exclude
    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("restaurantName", restaurantName);
        result.put("dishes", dishes);

        return result;
    }


}

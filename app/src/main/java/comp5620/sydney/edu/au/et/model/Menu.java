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

    private String menuID;
    private String restaurantName;
    private String restaurantAddress;
    public Map<String, Map<String, String>> dishes = new LinkedHashMap<>();

    public Menu() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Menu(String restaurantName,String restaurantAddress, Map<String, Map<String, String>> dishes) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.dishes = dishes;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Map<String, Map<String, String>> getDishes() {
        return dishes;
    }

    public void setDishes(Map<String, Map<String, String>> dishes) {
        this.dishes = dishes;
    }

    @Exclude
    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("menuID", menuID);
        result.put("restaurantName", restaurantName);
        result.put("restaurantAddress", restaurantAddress);
        result.put("dishes", dishes);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(menuID, menu.menuID) &&
                Objects.equals(restaurantName, menu.restaurantName) &&
                Objects.equals(restaurantAddress, menu.restaurantAddress) &&
                Objects.equals(dishes, menu.dishes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(menuID, restaurantName, restaurantAddress, dishes);
    }
}

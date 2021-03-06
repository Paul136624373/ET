package comp5620.sydney.edu.au.et.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Group implements Serializable {

    private String groupID;
    private String owner;
    private String ownerPhone;
    private String eatingTime;
    private String creationTime;
    private String numberOfPeople;
    private String restaurantName;
    private String restaurantAddress;

    // Store username, phone, flavour. The owner should be added in.
    public Map<String, Map<String, String>> members = new LinkedHashMap<>();

    private String type;
    // Map<username, hasAcceptedInvite>
    public Map<String, Boolean> invites = new LinkedHashMap<>();


    public Group() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEatingTime() {
        return eatingTime;
    }

    public void setEatingTime(String eatingTime) {
        this.eatingTime = eatingTime;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Boolean> getInvites() {
        return invites;
    }

    public void setInvites(Map<String, Boolean> invites) {
        this.invites = invites;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    @Exclude
    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("groupID", groupID);
        result.put("ownerPhone", ownerPhone);
        result.put("owner", owner);
        result.put("eatingTime", eatingTime);
        result.put("creationTime", creationTime);
        result.put("numberOfPeople", numberOfPeople);
        result.put("members", members);
        result.put("type", type);
        result.put("invites", invites);
        result.put("restaurantName", restaurantName);
        result.put("restaurantAddress", restaurantAddress);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(groupID, group.groupID) &&
                Objects.equals(owner, group.owner) &&
                Objects.equals(ownerPhone, group.ownerPhone) &&
                Objects.equals(eatingTime, group.eatingTime) &&
                Objects.equals(creationTime, group.creationTime) &&
                Objects.equals(numberOfPeople, group.numberOfPeople) &&
                Objects.equals(restaurantName, group.restaurantName) &&
                Objects.equals(restaurantAddress, group.restaurantAddress) &&
                Objects.equals(members, group.members) &&
                Objects.equals(type, group.type) &&
                Objects.equals(invites, group.invites);
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupID, owner, ownerPhone, eatingTime, creationTime, numberOfPeople, restaurantName, restaurantAddress, members, type, invites);
    }
}

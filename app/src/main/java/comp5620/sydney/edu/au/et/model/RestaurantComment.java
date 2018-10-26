package comp5620.sydney.edu.au.et.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class RestaurantComment implements Serializable {

    private String restaurantName;
    private String creationTime;
    private String author;
    private String content;
    private String groupID;

    public RestaurantComment() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @Exclude
    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("restaurantName", restaurantName);
        result.put("creationTime", creationTime);
        result.put("author", author);
        result.put("content", content);
        result.put("groupID", groupID);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantComment that = (RestaurantComment) o;
        return Objects.equals(restaurantName, that.restaurantName) &&
                Objects.equals(creationTime, that.creationTime) &&
                Objects.equals(author, that.author) &&
                Objects.equals(content, that.content) &&
                Objects.equals(groupID, that.groupID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(restaurantName, creationTime, author, content, groupID);
    }
}

package comp5620.sydney.edu.au.et.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Friend implements Serializable {

    private String firstUsername;
    private String secondUsername;

    public Friend() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public String getFirstUsername() {
        return firstUsername;
    }

    public void setFirstUsername(String firstUsername) {
        this.firstUsername = firstUsername;
    }

    public String getSecondUsername() {
        return secondUsername;
    }

    public void setSecondUsername(String secondUsername) {
        this.secondUsername = secondUsername;
    }

    @Exclude
    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("firstUsername", firstUsername);
        result.put("secondUsername", secondUsername);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return Objects.equals(firstUsername, friend.firstUsername) &&
                Objects.equals(secondUsername, friend.secondUsername);
    }

    @Override
    public int hashCode() {

        return Objects.hash(firstUsername, secondUsername);
    }
}

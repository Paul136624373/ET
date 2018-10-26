package comp5620.sydney.edu.au.et.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Post implements Serializable {
    public String id;
    public String author;
    public String time;
    public String body;
    public Map<String, String> thumbups = new LinkedHashMap<>();
    // First String for id
    // Use Map<String, Map<String, String>> for the case when one user writes more than one comments for a post
    public Map<String, Map<String, String>> comments = new LinkedHashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String id, String author, String time, String body) {
        this.id = id;
        this.author = author;
        this.time = time;
        this.body = body;
    }

    public Post(String id, String author, String time, String body, Map<String, String> thumbups, Map<String, Map<String, String>> comments) {
        this.id = id;
        this.author = author;
        this.time = time;
        this.body = body;
        this.thumbups = thumbups;
        this.comments = comments;
    }

    @Exclude
    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("author", author);
        result.put("time", time);
        result.put("body", body);
        result.put("thumbups", thumbups);
        result.put("comments", comments);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(author, post.author) &&
                Objects.equals(time, post.time) &&
                Objects.equals(body, post.body) &&
                Objects.equals(thumbups, post.thumbups) &&
                Objects.equals(comments, post.comments);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, author, time, body, thumbups, comments);
    }
}

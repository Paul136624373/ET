package comp5620.sydney.edu.au.et.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Customer implements Serializable {

    private String username;
    private String password;
    private String surname;
    private String givenName;
    private String phoneNumber;
    private String address;
    private String gender;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Customer(String username, String password, String surname, String givenName, String phoneNumber, String address, String gender) {
        this.username = username;
        this.password = password;
        this.surname = surname;
        this.givenName = givenName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Exclude
    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("username", username);
        result.put("password", password);
        result.put("surname", surname);
        result.put("givenName", givenName);
        result.put("phoneNumber", phoneNumber);
        result.put("address", address);
        result.put("gender", gender);

        return result;
    }

}

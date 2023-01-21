package com.example.discoverpic.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.discoverpic.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {
    @PrimaryKey()
    @NonNull
    public String id = "";
    public String name = "";
    public String imgUrl = "";
    public String city = "";
    public String country = "";
    public String userId = "";
    public Long lastUpdated;

    public Post() {
    }

    public Post(String id, String name, String imgUrl, String city, String country, String userId) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.city = city;
        this.country = country;
        this.userId = userId;
    }

    static final String ID = "id";
    static final String NAME = "name";
    static final String IMG = "img";
    static final String CITY = "city";
    static final String COUNTRY = "country";
    static final String USERID = "userId";
    static final String COLLECTION = "posts";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "posts_local_last_update";

    static String POST_ID = "1";

    public static Post fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String name = (String) json.get(NAME);
        String img = (String) json.get(IMG);
        String city = (String) json.get(CITY);
        String country = (String) json.get(COUNTRY);
        String userId = (String) json.get(USERID);
        Post post = new Post(id, name, img, city, country, userId);

        try {
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            post.setLastUpdated(time.getSeconds());
        } catch (Exception e) {

        }
        return post;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED, time);
        editor.commit();
    }

    public static void setPostId() {
        POST_ID = POST_ID + 1;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(IMG, getImgUrl());
        json.put(CITY, getCity());
        json.put(COUNTRY, getCountry());
        json.put(USERID, getUserId());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getUserId() {
        return userId;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }
}

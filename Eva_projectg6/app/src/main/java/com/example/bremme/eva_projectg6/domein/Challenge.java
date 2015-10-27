package com.example.bremme.eva_projectg6.domein;

import android.util.EventLogTags;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BREMME on 20/10/15.
 */
public class Challenge {
    private String id;
    private String name;
    private String description;
    private Difficulty difficulty;
    private URL[] imageUrls;
    public Challenge(String id, String name, URL[] url) {
       setId(id);
       setName(name);
       // setCategory(category);
        setImageUrl(url);
    }
    public Challenge(String id, String name, String description ,String[] url) {
        setId(id);
        setName(name);
        setImageUrlByString(url);
    }

    public Challenge(String id, String name, String description ,Difficulty difficulty ,URL[] url){
        setId(id);
        setName(name);
        setDescription(description);
        setImageUrl(url);
        setDifficulty(difficulty);
    }

    public URL[] getImageUrl() {
        return imageUrls;
    }

    public void setImageUrl(URL[] imageUrl) {
        this.imageUrls = imageUrl;
    }
    public void setImageUrlByString(String[] url)
    {
        try {
            imageUrls = new URL[url.length];
            for(int i = 0;i<url.length;i++)
                imageUrls[i] = new URL(url[i]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public String getDescription(){return description;}

    public void setDescription(String description){this.description = description;}

    public Difficulty getDifficulty(){return difficulty;}
    public void setDifficulty(Difficulty difficulty){this.difficulty = difficulty;}



}

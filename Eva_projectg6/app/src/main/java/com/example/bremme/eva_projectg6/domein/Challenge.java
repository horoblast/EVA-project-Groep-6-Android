package com.example.bremme.eva_projectg6.domein;

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
    private URL imageUrl;
    public Challenge(String id, String name, URL url) {
       setId(id);
       setName(name);
       // setCategory(category);
        setImageUrl(url);
    }
    public Challenge(String id, String name, String description ,String url) {
        setId(id);
        setName(name);
        setImageUrlByString(url);
    }

    public Challenge(String id, String name, String description ,Difficulty difficulty ,String url){
        setId(id);
        setName(name);
        setDescription(description);
        setImageUrlByString(url);
        setDifficulty(difficulty);
    }
    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setImageUrlByString(String url)
    {
        try {
            imageUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public URL getUrl(){
        return imageUrl;
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

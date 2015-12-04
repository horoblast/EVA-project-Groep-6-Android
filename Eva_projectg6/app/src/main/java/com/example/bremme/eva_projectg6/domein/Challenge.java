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
    private String urlImage;
    private boolean childFriendly;
    private boolean studentFriendly;
    public Challenge(String id, String name, URL url) {
       setId(id);
       setName(name);
       // setCategory(category);
    }
    public Challenge(String id, String name, String description ,String url) {
        setId(id);
        setName(name);
        setUrlImage(url);
    }

    public Challenge(String id, String name, String description ,Difficulty difficulty ,String url) {
        setId(id);
        setName(name);
        setDescription(description);
        setDifficulty(difficulty);
        setUrlImage(url);
    }

    public Challenge(String id,String name,String description, Difficulty difficulty, String imageUrl, boolean childFriendly, boolean studentFriendly) {
        this(id,name,description,difficulty,imageUrl);
        setChildFriendly(childFriendly);
        setStudentFriendly(studentFriendly);
    }

    public boolean isChildFriendly() {
        return childFriendly;
    }

    public void setChildFriendly(boolean childFriendly) {
        this.childFriendly = childFriendly;
    }

    public boolean isStudentFriendly() {
        return studentFriendly;
    }

    public void setStudentFriendly(boolean studentFriendly) {
        this.studentFriendly = studentFriendly;
    }


    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

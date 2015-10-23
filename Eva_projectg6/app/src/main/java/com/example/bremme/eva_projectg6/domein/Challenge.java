package com.example.bremme.eva_projectg6.domein;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BREMME on 20/10/15.
 */
public class Challenge {
    private String id;
    private Status category;
    private String name;
    private URL[] imageUrls;
    public Challenge(String id, String name, Status category,URL[] url) {
       setId(id);
       setName(name);
        setCategory(category);
        setImageUrl(url);
    }
    public Challenge(String id, String name, String category,String[] url) {
        setId(id);
        setName(name);
        setStatusByString(category);
        setImageUrlByString(url);
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

    public void setName(String name) {
        this.name = name;
    }

    public Status getCategory() {
        return category;
    }

    public void setCategory(Status category) {
        this.category = category;
    }
    public void setStatusByString(String status)
    {
        //todo default instellen
        try{
            setCategory(Status.valueOf(status));
        }catch(Exception e)
        {
            setCategory(Status.Single);
        }

    }
}

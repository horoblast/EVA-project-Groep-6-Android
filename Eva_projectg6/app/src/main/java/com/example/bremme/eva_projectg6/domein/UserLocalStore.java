package com.example.bremme.eva_projectg6.domein;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BREMME on 16/10/15.
 */
public class UserLocalStore {
    public static final String SP_NAME="user_details";
    private String token;
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context)
    {
    userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }
    public void storeUserData(User user)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("Firstname",user.getFirstname());
        spEditor.putString("Lastname",user.getLastname());
        spEditor.putString("Username",user.getUsername());
        spEditor.putString("Password",user.getPassword());
        spEditor.putString("Email",user.getEmail());
        spEditor.putString("GebDatum",user.getGebDatum());
        spEditor.putString("Gender",user.getGender().toString());
        spEditor.putString("Status",user.getStatus().toString());
        spEditor.commit();
    }
    public User getLoggedInUser()
    {
        User user = new User(userLocalDatabase.getString("Firstname",""),
                userLocalDatabase.getString("Lastname",""),
                userLocalDatabase.getString("Email",""),userLocalDatabase.getString("GebDatum",""),
                Gender.valueOf(userLocalDatabase.getString("Gender",""))
                ,Status.valueOf(userLocalDatabase.getString("Status","")),userLocalDatabase.getString("Password","")
                ,userLocalDatabase.getString("Username",""),userLocalDatabase.getBoolean("isDoingChallenges",false));
        return user;
    }
    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn",loggedIn);
        spEditor.commit();
    }
    public void clearUserData()
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public void setToken(String token)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("Token",token);
        spEditor.commit();
    }
    public String getToken()
    {
        return userLocalDatabase.getString("Token","NOT FOUND");
    }
}

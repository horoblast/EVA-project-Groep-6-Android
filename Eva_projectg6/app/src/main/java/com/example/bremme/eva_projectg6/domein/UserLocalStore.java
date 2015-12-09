package com.example.bremme.eva_projectg6.domein;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
    public String getUsername()
    {
        return userLocalDatabase.getString("Username","");
    }
    public String getUserId()
    {
        return userLocalDatabase.getString("Id","");
    }
    public String getEmail(){return userLocalDatabase.getString("Email","");}
    public String getDifficulty(){return userLocalDatabase.getString("Difficulty","");}
    public Boolean getStudent(){return userLocalDatabase.getBoolean("IsStudent", false);}
    public Boolean getHasChildren(){return userLocalDatabase.getBoolean("HasChildren", false);}
    public Boolean getIsDoingChallenges(){return userLocalDatabase.getBoolean("IsDoingChallenges",false);}
    public void storeUserData(User user)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("Firstname",user.getFirstname());
        spEditor.putString("Lastname",user.getLastname());
        spEditor.putString("Username",user.getUsername());
        spEditor.putString("Password",user.getPassword());
        spEditor.putString("Email",user.getEmail());
        spEditor.putString("GebDatum",user.getGebDatum());
        spEditor.putString("Gender", user.getGender().toString());
        spEditor.putString("Difficulty", user.getDif().toString());
        spEditor.putString("Id",user.getUserId());
        spEditor.putBoolean("HasChildren", user.HasChilderen());
        spEditor.putBoolean("IsStudent",user.isStudent());
        spEditor.putBoolean("IsDoingChallenges", user.isDoingChallenges());
        spEditor.putStringSet("SuggestionIds", user.getSuggestionIds());
        spEditor.putStringSet("CompletedIds",user.getCompletedIds());
        spEditor.commit();
    }
    public User getLoggedInUser()
    {
        User user = new User(userLocalDatabase.getString("Firstname",""),
                userLocalDatabase.getString("Lastname",""),
                userLocalDatabase.getString("Email",""),
                userLocalDatabase.getString("GebDatum",""),
                Gender.valueOf(userLocalDatabase.getString("Gender",""))
                ,Difficulty.valueOf(userLocalDatabase.getString("Difficulty", "")),
                userLocalDatabase.getString("Password","")
                ,userLocalDatabase.getString("Username",""),
                userLocalDatabase.getBoolean("IsDoingChallenges",false),
                userLocalDatabase.getBoolean("IsStudent",false),
                userLocalDatabase.getBoolean("HasChildren",false));
        Set<String> stringSet = new TreeSet<>();
        user.setSuggestionIds(userLocalDatabase.getStringSet("SuggestionIds",stringSet));
        user.setCompletedIds(userLocalDatabase.getStringSet("CompletedIds",stringSet));
        user.setUserId(userLocalDatabase.getString("Id",""));
        return user;
    }
    public void clearSuggestions()
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        Set<String> stringSet = new TreeSet<>();
        spEditor.putStringSet("SuggestionIds", stringSet);
        spEditor.commit();
    }
    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn",loggedIn);
        spEditor.commit();
    }
    public boolean isUserLoggedIn()
    {
       return userLocalDatabase.getBoolean("LoggedIn",false);
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

package com.example.bremme.eva_projectg6.domein;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

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
        spEditor.putString("Gender", user.getGender().toString());
        spEditor.putString("Difficulty", user.getDif().toString());
        spEditor.putBoolean("HasChildren", user.HasChilderen());
        spEditor.putBoolean("IsStudent",user.isStudent());
        spEditor.putBoolean("IsDoingChallenges", user.isDoingChallenges());
        /*if(user.getChallengeSuggestions().length!=0){
            for(int i=0;i<user.getChallengeSuggestions().length;i++)
            {
                spEditor.putString("ChallengeId"+i,user.getChallengeSuggestions()[i].getId());
                spEditor.putString("ChallengeDesc"+i,user.getChallengeSuggestions()[i].getDescription());
                spEditor.putString("ChallengeName"+i,user.getChallengeSuggestions()[i].getName());
                spEditor.putString("ChallengeDiff"+i,user.getChallengeSuggestions()[i].getDifficulty().toString());
                spEditor.putString("ChallengeUrl"+i,user.getChallengeSuggestions()[i].getUrl().toString());
            }
        }
        */
        spEditor.putStringSet("SuggestionIds",user.getSuggestionIds());
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
        Set<String> stringSet = new HashSet<>();
        user.setSuggestionIds(userLocalDatabase.getStringSet("SuggestionIds",stringSet));
        return user;
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

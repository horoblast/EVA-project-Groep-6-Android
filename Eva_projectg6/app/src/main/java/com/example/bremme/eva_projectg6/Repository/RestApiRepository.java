package com.example.bremme.eva_projectg6.Repository;

import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by BREMME on 20/10/15.
 */
public class RestApiRepository {
    private final String CHALLENGES = "https://groep6api.herokuapp.com/challenges";
    private final String REGISTER = "http://groep6api.herokuapp.com/register";
    private final String LOGIN = "https://groep6api.herokuapp.com/login";
    private final String USER = "http://groep6api.herokuapp.com/user";
    private final String USERNAMECHECK = "http://groep6api.herokuapp.com/checkusername";
    private Challenge[] challengeList;
    public RestApiRepository() {
    }

    public String getUsernamecheck() {
        return USERNAMECHECK;
    }

    public String getUser() {
        return USER;
    }

    public String getLOGIN() {
        return LOGIN;
    }

    public String getChallenges() {
        return CHALLENGES;
    }

    public String getRegister()
    {
        return REGISTER;
    }


    public Challenge[] getAllChallenges(JsonArray result)
    {

        challengeList = new Challenge[result.size()];
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).isJsonObject()) {
                JsonObject json = result.get(i).getAsJsonObject();
                String[] urls = new String[json.get("image").getAsJsonArray().size()];
                for (int j = 0; j < json.get("image").getAsJsonArray().size(); j++) {
                    urls = new String[json.get("image").getAsJsonArray().size()];
                    urls[j] = json.get("image").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
                }
                Challenge c = new Challenge(json.get("_id").getAsString(), json.get("name").getAsString(), json.get("description").getAsString(), Difficulty.valueOf(json.get("difficulty").getAsString()), "http://res.cloudinary.com/diyuj5c1j/image/upload/v1446068027/uikwm7g8kwongkjrbzbb.jpg");
                challengeList[i] = c;
            }
        }
        return challengeList;
    }

}

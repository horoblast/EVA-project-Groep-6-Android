package com.example.bremme.eva_projectg6.Repository;

import com.example.bremme.eva_projectg6.domein.Challenge;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by BREMME on 20/10/15.
 */
public class RestApiRepository {
    private final String CHALLENGES = "https://groep6api.herokuapp.com/challenges";
    private final String REGISTER = "http://groep6api.herokuapp.com/register";
    private final String LOGIN = "https://groep6api.herokuapp.com/login";
    private Challenge[] challengeList;
    public RestApiRepository() {
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
                Challenge c = new Challenge(json.get("_id").getAsString(), json.get("name").getAsString(), json.get("category").getAsString(), urls);
                challengeList[i] = c;
            }
        }
        return challengeList;
    }

}

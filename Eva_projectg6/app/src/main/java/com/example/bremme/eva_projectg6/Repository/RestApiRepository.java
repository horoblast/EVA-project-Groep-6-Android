package com.example.bremme.eva_projectg6.Repository;

import android.content.Context;
import android.util.Log;

import com.example.bremme.eva_projectg6.Challenge;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by BREMME on 20/10/15.
 */
public class RestApiRepository {
    private final String CHALLENGES = "https://groep6api.herokuapp.com/challenges";
    private Challenge[] challengeList;
    public RestApiRepository() {
    }

    public Challenge[] getAllChallenges(Context c)
    {
        
        Ion.with(c)
                .load(CHALLENGES)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error


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

                    }
                });
        return challengeList;
    }

}

package com.example.bremme.eva_projectg6.Repository;

import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.Gender;
import com.example.bremme.eva_projectg6.domein.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by BREMME on 20/10/15.
 */
public class RestApiRepository {
    private final String CHALLENGES = "https://groep6api.herokuapp.com/challenges";
    private final String REGISTER = "http://groep6api.herokuapp.com/register";
    private final String LOGIN = "https://groep6api.herokuapp.com/login";
    private final String USER = "http://groep6api.herokuapp.com/user";
    private final String USERNAMECHECK = "http://groep6api.herokuapp.com/checkusername";
    private final String PUTSUGGESTEDCHALLENGE ="http://groep6api.herokuapp.com/setsuggestions";
    private final String FINDCHALLENGEBYID="http://groep6api.herokuapp.com/findchallengebyid";
    private final String STARTUSERSERIES = "http://groep6api.herokuapp.com/startuserseries";
    private final String CURRENTCHALLENGE = "http://groep6api.herokuapp.com/setuserchallenge";
    private final String FACEBOOKLOGINCHECK ="http://groep6api.herokuapp.com/userfacebook";
    private final String FACEBOOKREGISTREER = "http://groep6api.herokuapp.com/registerfacebook";
    private Challenge[] challengeList;
    public RestApiRepository() {
    }

    public String getFACEBOOKREGISTREER() {
        return FACEBOOKREGISTREER;
    }

    public String getCURRENTCHALLENGE() {
        return CURRENTCHALLENGE;
    }
    public String getSTARTUSERSERIES() {
        return STARTUSERSERIES;
    }

    public String getPUTSUGGESTEDCHALLENGE() {
        return PUTSUGGESTEDCHALLENGE;
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

    public String getFINDCHALLENGEBYID() {
        return FINDCHALLENGEBYID;
    }

    public String getFACEBOOKLOGINCHECK() {
        return FACEBOOKLOGINCHECK;
    }


    public User getUser(JsonObject j)
    {

        String fname = j.get("firstname").getAsString();
        String lname = j.get("lastname").getAsString();
        String email = j.get("email").getAsString();
        String username = j.get("username").getAsString();
        String birthDate = j.get("birthdate").getAsString();
        boolean isStudent = j.get("isstudent").getAsBoolean();
        boolean hasChilderen = j.get("haschildren").getAsBoolean();
        Gender g = Gender.valueOf(capitalizeFirstLetter(j.get("gender").getAsString()));
        Difficulty dif = Difficulty.valueOf(j.get("difficulty").getAsString());
        boolean isdoingChallenges = j.get("isdoingchallenges").getAsBoolean();
        User newUser = new User(username, lname, g, hasChilderen, isdoingChallenges, isStudent, dif, email, fname, birthDate);
        Set<String> stringSet = new TreeSet<>();
        Set<String> completedChallenge = new TreeSet<>();
        if(isdoingChallenges)
        {
            JsonArray challengeSuggestions = j.get("challengessuggestions").getAsJsonArray();
            if(challengeSuggestions.size()!=0)
            {
                for(JsonElement cId : challengeSuggestions) {
                    stringSet.add(cId.getAsString());
                }
               //todo testen
            }
            JsonArray challengeCompleted = j.get("challengescompleted").getAsJsonArray();
            if(challengeCompleted.size()!=0)
            {
                for(JsonElement ccId : challengeCompleted){
                    completedChallenge.add(ccId.getAsString());
                }
            }
        }
        newUser.setSuggestionIds(stringSet);
        newUser.setCompletedIds(completedChallenge);
        return newUser;
    }

    public Challenge[] getAllChallenges(JsonArray result)
    {

        challengeList = new Challenge[result.size()];
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).isJsonObject()) {
                JsonObject json = result.get(i).getAsJsonObject();
                Challenge c = getChallenge(json);
                challengeList[i] = c;
            }
        }
        return challengeList;
    }
    public Challenge getChallenge(JsonObject json)
    {
            String url="";
            url = json.get("image").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
            Challenge c = new Challenge(json.get("_id").getAsString(), json.get("name").getAsString(), json.get("description").getAsString(), Difficulty.valueOf(json.get("difficulty").getAsString()),url);
            return c;
    }
    public String[] getSuggestedChallenges(JsonArray jsonArray)
    {
        String ids[] = new String [jsonArray.size()];
        for(int i=0;i<jsonArray.size();i++)
        {
            if (jsonArray.get(i).isJsonObject()){
                ids[i] = jsonArray.get(i).getAsString();
            }
        }
        return ids;
    }
    private String capitalizeFirstLetter(String str)
    {
        StringBuilder b = new StringBuilder(str);
        int i = 0;
        do {
            b.replace(i, i + 1, b.substring(i,i + 1).toUpperCase());
            i =  b.indexOf(" ", i) + 1;
        } while (i > 0 && i < b.length());
        return b.toString();

    }

}

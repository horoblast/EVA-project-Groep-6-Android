package com.example.bremme.eva_projectg6.Repository;

import android.util.Log;

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
    private final String COMPLETECHALLENGE = "http://groep6api.herokuapp.com/completecurrentchallenge";
    private final String LOGINWITHFB ="http://groep6api.herokuapp.com/loginFb";
    private final String SETALLSUGGESTIONS ="http://groep6api.herokuapp.com/setsuggestions";
    private final String GETALLCHALLENGESBYLIST = "http://groep6api.herokuapp.com/findmanychallengesbyid";
    private final String SETSCORERATING = "http://groep6api.herokuapp.com/setrating";
    private final String SUBMITUSERCHANGES = "http://groep6api.herokuapp.com/submitChanges";
    private final String COMPLETECHALLENGESERIES = "http://groep6api.herokuapp.com/completechallengeseries";
    private final String LENGTHCHALLENGES ="http://groep6api.herokuapp.com/challengeCount";

    private Challenge[] challengeList;
    public RestApiRepository() {
    }

    public String getLENGTHCHALLENGES() {
        return LENGTHCHALLENGES;
    }

    public String getSUBMITUSERCHANGES() {
        return SUBMITUSERCHANGES;
    }
    public String getCOMPLETECHALLENGESERIES(){return COMPLETECHALLENGESERIES;}
    public String getGETALLCHALLENGESBYLIST() {
        return GETALLCHALLENGESBYLIST;
    }
    public String getSETALLSUGGESTIONS() {
        return SETALLSUGGESTIONS;
    }
    public String getLOGINWITHFB() {
        return LOGINWITHFB;
    }
    public String getCOMPLETECHALLENGE() {
        return COMPLETECHALLENGE;
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
    public String getSETSCORERATING() {return SETSCORERATING;}

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
        newUser.setCurrentChallenge("");
        if(isdoingChallenges) {
            try {//blijkbaar kan currentchallenge null zijn in api
                String currentChallenge = j.get("currentchallenge").getAsString();
                newUser.setCurrentChallenge(currentChallenge);
            }catch(Exception e) {
                Log.i("currentchallenge", "currentchallenge is null");
            }
            try{
                JsonArray challengeSuggestions = j.get("challengessuggestions").getAsJsonArray();
                if (challengeSuggestions.size() != 0) {
                    for (JsonElement cId : challengeSuggestions) {
                        stringSet.add(cId.getAsString());
                    }
                    //todo testen
                }
                JsonArray challengeCompleted = j.get("challengescompleted").getAsJsonArray();
                if (challengeCompleted.size() != 0) {
                    for (JsonElement ccId : challengeCompleted) {
                        completedChallenge.add(ccId.getAsString());
                    }
                }
            } catch (Exception e) {
                Log.i("currentchallenge", "currentchallenge is null");
            }
        }
        newUser.setSuggestionIds(stringSet);
        newUser.setCompletedIds(completedChallenge);
        newUser.setUserId(j.get("_id").getAsString());
        return newUser;
    }

    public Challenge[] getAllChallenges(JsonArray result,String language)
    {
        challengeList = new Challenge[result.size()];
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).isJsonObject()) {
                JsonObject json = result.get(i).getAsJsonObject();
                Challenge c = getChallenge(json,language);
                challengeList[i] = c;
            }
        }
        return challengeList;
    }
    public Challenge getChallenge(JsonObject json,String language)
    {
        String lang = Character.toUpperCase(language.charAt(0)) + language.substring(1);
        String url ="";
        Challenge c;
        try{
            url = json.get("image").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
        }catch(Exception e)
        {
            //if image  = null
        }
       if(lang.equals("Nl"))
       {
           c = new Challenge(json.get("_id").getAsString(), json.get("name").getAsString(), json.get("description").getAsString(), Difficulty.valueOf(json.get("difficulty").getAsString()),url,json.get("childfriendly").getAsBoolean(),json.get("studentfriendly").getAsBoolean());
       }
        else
       {
           c = new Challenge(json.get("_id").getAsString(), json.get("name"+lang).getAsString(), json.get("description"+lang).getAsString(), Difficulty.valueOf(json.get("difficulty").getAsString()),url,json.get("childfriendly").getAsBoolean(),json.get("studentfriendly").getAsBoolean());
       }
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

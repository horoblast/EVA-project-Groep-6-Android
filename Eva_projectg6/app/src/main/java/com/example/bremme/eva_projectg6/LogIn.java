package com.example.bremme.eva_projectg6;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.media.tv.TvInputService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.bremme.eva_projectg6.Repository.DatabaseHelper;
import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.Gender;
import com.example.bremme.eva_projectg6.domein.User;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class LogIn extends AppCompatActivity {

    private EditText eUsername,ePassword;
    private Button loginButton;
    private UserLocalStore userLocalStore;
    private RestApiRepository repo;
    private Button b;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallBack;
    private DatabaseHelper localDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_in);
        mCallbackManager = CallbackManager.Factory.create();
        localDb = new DatabaseHelper(this);

        AccessTokenTracker tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        tracker.startTracking();
        ProfileTracker pTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                this.stopTracking();
                Profile.setCurrentProfile(currentProfile);
            }
        };
        pTracker.startTracking();
        //verbinden editText en button met layout
        Locale.setDefault(new Locale("nl"));
        LoginButton button = (LoginButton) findViewById(R.id.login_button);
        facebookCallback();
        button.registerCallback(mCallbackManager, mCallBack);
        button.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, publish_actions"));
        eUsername = (EditText) findViewById(R.id.eUsername);
        ePassword = (EditText) findViewById(R.id.ePassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        userLocalStore = new UserLocalStore(this);
        repo = new RestApiRepository();
        b= (Button) findViewById(R.id.buttonFill4);
        b.setText("");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eUsername.setText("arnedebremme");
                ePassword.setText("testimpl2");
            }
        });

    }
    //Naam wordt opgehaald van profile en wordt weergegeven
    private void displayWelcomeMessage(Profile profile){
        if(profile != null){
            Log.i("PROFIEL FACEBOOK",profile.getFirstName());

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getToken(View view)
    {
        final ProgressDialog dialog = ProgressDialog.show(LogIn.this,getResources().getString(R.string.waitScreen),this.getResources().getString(R.string.userInloggen),true);
        Ion.with(this)
                .load(repo.getLOGIN())
                .setBodyParameter("username", eUsername.getText().toString())
                .setBodyParameter("password", ePassword.getText().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {

                            if (result.isJsonObject()) {
                                String token = result.getAsJsonObject().get("token").getAsString();
                                userLocalStore.setToken(token);
                                Log.i("messagetoken", token);
                                findUserAndStore(dialog);
                            }
                        } catch (Exception er) {
                            Log.i("Error message", "Login misslukt kan geen token ophalen api down ?");
                            dialog.dismiss();
                            ePassword.setError(getResources().getString(R.string.wrongPassword));
                        }

                    }
                });

    }
    private void findUserAndStore(final ProgressDialog dialog)
    {
            Ion.with(this)
                    .load(repo.getUser()).setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                    .setBodyParameter("username", eUsername.getText().toString())
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            try {
                                putAllChallengesInLocalDb();
                                if (result.get(0).isJsonObject()) {
                                    JsonObject j = result.get(0).getAsJsonObject();
                                    User newUser = repo.getUser(j);
                                    userLocalStore.setUserLoggedIn(true);
                                    userLocalStore.storeUserData(newUser);
                                    dialog.dismiss();
                                    if (newUser.getCurrentChallenge().length() > 0) {
                                        goToViewChallenge(newUser.getCurrentChallenge());
                                    } else {
                                        challengesBekijken();
                                    }

                                }
                            } catch (Exception er) {
                                dialog.dismiss();
                                Log.i("Error message", "nullerinooo");
                                ePassword.setError(getResources().getString(R.string.wrongPassword));
                            }

                        }
                    });


    }
    private void challengesBekijken()
    {
        startActivity(new Intent(this, ChooseChallenge.class));
    }
    public void register(View view)
    {
        Intent i = new Intent(this,RegisterMain.class);
        startActivity(i);
        //registreer hyperlink
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

    private void facebookCallback()
    {
       mCallBack = new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try{
                                    String id =object.getString("id");
                                    String name = object.getString("name");
                                    String gender =  object.getString("gender");
                                    String birthDay = object.getString("birthday");
                                    String email = "";
                                    facebookUserpersisteren(id,gender,birthDay,email);
                                }catch (Exception e)
                                {
                                    Log.i("MESAAGEEEE ERROR" ,"FACEBOOK LOGIN MISSLUKT");
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            Profile.setCurrentProfile(profile2);
                            Log.v("facebook2 - profile", profile2.getFirstName());
                            mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                }
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        };
    }
    private void facebookUserpersisteren(final String id, final String gender, final String birthday, final String email)
    {
        /*{
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0NTMxOTEwODYsImlhdCI6MTQ0ODAwNzA4Nn0.w2cWfZ1WMB6hR473KLH23fgGfC3_Fv07TlL1KWp-IIo",
                "user": [
        {
            "_id": "56422127255def1f006de906",
                "facebookid": "10205318282465745",
                "isfacebooklogin": true,
                "isdoingchallenges": true,
                "birthdate": "Tue Jul 12 1994 08:00:00 GMT+0000 (UTC)",
                "haschildren": false,
                "isstudent": true,
                "difficulty": "easy",
                "email": "",
                "gender": "male",
                "lastname": "De Bremme",
                "firstname": "Arne",
                "username": "arne de bremme",
                "__v": 23,
                "currentchallenge": "563146978c5ba50300f2b4f9",
                "hash": "",
                "salt": "",
                "challengessuggestions": [
            "56313a951a99710300d9d13f",
                    "563146978c5ba50300f2b4f9",
                    "563141ab8c5ba50300f2b4f5"
            ],
            "challengescompleted": [
            "56313fc28c5ba50300f2b4f2",
                    "56313ee38c5ba50300f2b4f1",
                    "563151f48c5ba50300f2b500",
                    "563151058c5ba50300f2b4ff",
                    "563142de8c5ba50300f2b4f7"
            ]
        }
        ]
    }*/
        Ion.with(this)
                .load(repo.getLOGINWITHFB())
                .setBodyParameter("facebookid",id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i("yolo", "swag");
                        String token = result.get("token").getAsString();
                        userLocalStore.setToken(token);
                        if(result.get("user").getAsJsonArray().size()==0){
                            facebookUserRegistreren(gender, email, birthday, id);
                        }else{
                            JsonObject userJson = result.get("user").getAsJsonArray().get(0).getAsJsonObject();
                            User newUser = repo.getUser(userJson);
                            userLocalStore.setUserLoggedIn(true);
                            userLocalStore.storeUserData(newUser);
                            if (newUser.getCurrentChallenge().length() > 0) {
                                goToViewChallenge(newUser.getCurrentChallenge());
                            } else {
                                challengesBekijken();
                            }
                        }
                    }
                });
    }
    private void facebookUserRegistreren(String gender,String email,String birthdate,String id)
    {
        Profile p = Profile.getCurrentProfile();
        Intent i = new Intent(this,FacebookRegister.class);
        i.putExtra("gender",gender);
        i.putExtra("email",email);
        i.putExtra("birthdate",birthdate);
        i.putExtra("id",id);
        i.putExtra("name",p.getName());
        i.putExtra("firstname",p.getFirstName());
        i.putExtra("lastname", p.getLastName());
        startActivity(i);
    }
    private void goToViewChallenge(String currentChallengeId)
    {
        Intent intent = new Intent(this, ViewChallenges.class);
        intent.putExtra("CHALLENGE_ID", currentChallengeId);
        startActivity(intent);
    }

    private void putAllChallengesInLocalDb(){
        Ion.with(this)
                .load(repo.getChallenges())
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        String language = getResources().getConfiguration().locale.getLanguage();
                        localDb.putAllDataInDb(repo.getAllChallenges(result,language));
                    }
                });
    }
    }



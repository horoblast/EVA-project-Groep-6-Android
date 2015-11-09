package com.example.bremme.eva_projectg6;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.Locale;
import java.util.Random;

public class LogIn extends AppCompatActivity {

    private EditText eUsername,ePassword;
    private Button loginButton;
    private UserLocalStore userLocalStore;
    private RestApiRepository repo;
    private Button b;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;

        @Override
        public void onSuccess(LoginResult loginResult) {
            if(Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        Log.v("facebook - profile", profile2.getFirstName());
                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();
            }
            else {
                Profile profile = Profile.getCurrentProfile();
                Log.v("facebook - profile", profile.getFirstName());
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_in);
        mCallbackManager = CallbackManager.Factory.create();
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
        button.registerCallback(mCallbackManager, mCallBack);
        button.setReadPermissions("user_friends");
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

                                if (result.get(0).isJsonObject()) {
                                    JsonObject j = result.get(0).getAsJsonObject();
                                    User newUser = repo.getUser(j);
                                    userLocalStore.setUserLoggedIn(true);
                                    userLocalStore.storeUserData(newUser);
                                    dialog.dismiss();
                                    challengesBekijken();
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
    }



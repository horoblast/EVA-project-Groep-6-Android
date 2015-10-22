package com.example.bremme.eva_projectg6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.hardware.camera2.params.Face;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class LogIn extends AppCompatActivity {

    private EditText eUsername,ePassword;
    private Button loginButton;
    private UserLocalStore userLocalStore;
    private RestApiRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FacebookSdk.sdkInitialize(getApplicationContext());

        //verbinden editText en button met layout
        Locale.setDefault(new Locale("nl"));
        eUsername = (EditText) findViewById(R.id.eUsername);
        ePassword = (EditText) findViewById(R.id.ePassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        userLocalStore = new UserLocalStore(this);
        repo = new RestApiRepository();

    }


    public void login(View view)
    {
        //showProgressBar();
        Ion.with(this)
                .load(repo.getRegister())
                .setBodyParameter("username", eUsername.toString())
                .setBodyParameter("password", ePassword.toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        userLocalStore.setUserLoggedIn(true);
                        if(result.isJsonObject())
                        {
                           String token = result.getAsJsonObject().get("token").getAsString();
                        }
                    }
                });

    }
    private void showProgressBar()//verwijdert de inlogknop en toont een progressbar
    {
        ViewGroup layout = (ViewGroup) loginButton.getParent();
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW,R.id.ePassword);
        params.addRule(RelativeLayout.ALIGN_LEFT,R.id.ePassword);
        params.addRule(RelativeLayout.ALIGN_START,R.id.ePassword);
        params.addRule(RelativeLayout.ALIGN_RIGHT,R.id.ePassword);
        params.addRule(RelativeLayout.ALIGN_END,R.id.ePassword);
        if(layout!=null)
            layout.addView(new ProgressBar(this), params);
            layout.removeView(loginButton);
        try{

        }catch (NullPointerException er)
        {

        }catch(Exception e)
        {

        }


    }

    public void register(View view)
    {
        Intent i = new Intent(this,Register.class);
        startActivity(i);
        //registreer hyperlink
    }


}

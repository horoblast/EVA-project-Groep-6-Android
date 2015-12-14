package com.example.bremme.eva_projectg6;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bremme.eva_projectg6.domein.UserLocalStore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginRedirect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_redirect);
        UserLocalStore u = new UserLocalStore(this);
        Log.i("inlog","inlogredirect");

            if(u.isUserLoggedIn())
            {
                if(!u.getCurrentChallenge().equals(""))
                {
                    String s = u.getCurrentChallenge();
                    Intent intent = new Intent(this, ViewChallenges.class);
                    intent.putExtra("CHALLENGE_ID", u.getCurrentChallenge());
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(this, ChooseChallenge.class);
                    startActivity(intent);
                }
            }else{
                Intent intent = new Intent(this, LogIn.class);
                startActivity(intent);
            }
        }
    }


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
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.facebook.FacebookSdk;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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
        final ProgressDialog dialog = ProgressDialog.show(LogIn.this,getResources().getString(R.string.waitScreen),this.getResources().getString(R.string.userInloggen),true);
        Ion.with(this)
                .load(repo.getRegister())
                .setBodyParameter("username", eUsername.toString())
                .setBodyParameter("password", ePassword.toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try{

                            if (result.isJsonObject()) {
                                String token = result.getAsJsonObject().get("token").getAsString();
                                userLocalStore.setUserLoggedIn(true);
                                userLocalStore.setToken(token);
                                //todo nieuwe activiteit challenges bekijken.
                            }
                        }catch(Exception er)
                        {
                            Log.i("Error message","null");
                            dialog.dismiss();
                           ePassword.setError(getResources().getString(R.string.wrongPassword));
                        }

                    }
                });

    }

    public void register(View view)
    {
        Intent i = new Intent(this,Register.class);
        startActivity(i);
        //registreer hyperlink
    }


}

package com.example.bremme.eva_projectg6;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends AppCompatActivity {

    private EditText eUsername,ePassword;
    private Button inlogButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //verbinden editText en button met layout

        eUsername = (EditText) findViewById(R.id.eUsername);
        ePassword = (EditText) findViewById(R.id.ePassword);
        inlogButton = (Button) findViewById(R.id.loginButton);

    }
    public void login(View view)
    {
        //login logica
    }
    public void register(View view)
    {
        Intent i = new Intent(this,Register.class);
        startActivity(i);
        //registreer hyperlink
    }

}

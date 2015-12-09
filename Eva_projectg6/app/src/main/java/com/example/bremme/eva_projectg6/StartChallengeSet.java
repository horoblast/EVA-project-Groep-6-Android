package com.example.bremme.eva_projectg6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class StartChallengeSet extends AppCompatActivity {

    private UserLocalStore userLocalStore;
    private RestApiRepository repo;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_challenge_set);
        userLocalStore = new UserLocalStore(this);
        repo = new RestApiRepository();
        toolbar = (Toolbar) findViewById(R.id.tool_bar_Challenge);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView text = (TextView) findViewById(R.id.userNameTool);
        if(userLocalStore.isUserLoggedIn())
        {
            text.setText(userLocalStore.getLoggedInUser().getFirstname());
        }else{
            text.setText("user onbekend");
        }

        Button but =(Button) findViewById(R.id.btnChal);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSeries();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_challenge_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void startUserSeries()
    {
        Ion.with(this)
                .load(repo.getSTARTUSERSERIES())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .setBodyParameter("username",userLocalStore.getLoggedInUser().getUsername())
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                goToChooseChallenge();
            }
        });
    }
    private void goToChooseChallenge()
    {
        startActivity(new Intent(this,ChooseChallenge.class));
    }
}

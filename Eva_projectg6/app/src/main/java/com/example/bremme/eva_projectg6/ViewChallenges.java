package com.example.bremme.eva_projectg6;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.Gender;
import com.example.bremme.eva_projectg6.domein.User;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.example.bremme.eva_projectg6.R.id.donut_progress;

public class ViewChallenges extends AppCompatActivity {
    private RestApiRepository repo;
    private List<Challenge> challengesDone;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private UserLocalStore userLocalStore;
    private Toolbar toolbar;
    private DonutProgress donutProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_challenges);
        init();
        setAdapterWithChallenges();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_challenges, menu);
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
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void init(){

        donutProgress= (DonutProgress) findViewById(donut_progress);
        donutProgress.setProgress(0);
        userLocalStore = new UserLocalStore(this);
        repo = new RestApiRepository();
        challengesDone = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_Challenge);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        userLocalStore = new UserLocalStore(this);
        TextView text = (TextView) findViewById(R.id.userNameTool);
        if(userLocalStore.isUserLoggedIn())
        {
            text.setText(userLocalStore.getLoggedInUser().getFirstname());
        }else{
            text.setText("user onbekend");
        }

    }
    private void setAdapterWithChallenges()
    {
        Bundle bundle = getIntent().getExtras();
        final Set<String> idSet = userLocalStore.getLoggedInUser().getCompletedIds(); //geef uitgevoerde challenges
        idSet.add(bundle.getString("CHALLENGE_ID")); //set id van currentchallenge in alle challenges
        Ion.with(this)
                .load(repo.getUser()).setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .setBodyParameter("username", userLocalStore.getUsername())
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {

                            StringBuilder stringB = new StringBuilder("[");
                            if (result.get(0).isJsonObject()) {
                                JsonObject j = result.get(0).getAsJsonObject();
                                List<String> idChallenges = new ArrayList<String>();
                                JsonArray challengesCompleted = j.get("challengescompleted").getAsJsonArray();
                                String idChallengeCurrent = j.get("currentchallenge").getAsString();

                                idChallenges.add(idChallengeCurrent);
                                for(int i =0;i<challengesCompleted.size();i++)
                                {
                                    stringB.append("\"");
                                    idChallenges.add(challengesCompleted.get(i).getAsString());
                                    stringB.append(challengesCompleted.get(i).getAsString()+"\", ");
                                }
                                stringB.append("\"");
                                stringB.append(idChallengeCurrent);
                                stringB.append("\"]");

                                String language = getResources().getConfiguration().locale.getLanguage();
                               getChallengesByid(stringB.toString(),language,idChallenges);
                                //getChallengeObjects(idChallenges,language);
                            }
                        } catch (Exception er) {
                        }

                    }
                });

    }
    private void getChallengesByid(String ids, final String language, final List<String>idSorts)
    {
        final Context context = this;
            Ion.with(this)
                    .load(repo.getGETALLCHALLENGESBYLIST())
                    .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                    .setBodyParameter("ids", ids)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            challengesDone = Arrays.asList(repo.getAllChallenges(result, language));
                            Log.i("percent",(challengesDone.size()/21)*100+"");
                            donutProgress.setProgress((int)calculatePercent(challengesDone.size()));
                            sortChallengesDone(idSorts);
                            mAdapter = new ChallengeAdapter(challengesDone, context);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });

    }
    private void sortChallengesDone(List<String>ids)
    {
        List<Challenge> sortedChallenges = new ArrayList<>();
        for(String id : ids)
        {
            for(Challenge c : challengesDone)
            {
                if(c.getId().equals(id))
                {
                    sortedChallenges.add(c);
                    break;
                }
            }
        }
        challengesDone = sortedChallenges;
    }
    private double calculatePercent(int i)
    {
        double v = i;
        double x = (v/21) * 100;
        return x;
    }
}

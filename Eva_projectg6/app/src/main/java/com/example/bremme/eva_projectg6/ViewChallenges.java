package com.example.bremme.eva_projectg6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.Gender;
import com.example.bremme.eva_projectg6.domein.User;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class ViewChallenges extends AppCompatActivity {
    private RestApiRepository repo;
    private ArrayAdapter<String> listAdapter;
    private List challengesTitles;
    private Intent intent;
    private UserLocalStore userLocalStore;
    private Challenge[] challenges;
    private List<Challenge> challengesDone;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_challenges);
        userLocalStore = new UserLocalStore(this);
        repo = new RestApiRepository();
        challengesTitles = new ArrayList<>();

        getChallenges();
        challengesDone = userDummy().getChallenges();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getChallenges()
    {
        Ion.with(this)
                .load(repo.getChallenges())
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        challenges = repo.getAllChallenges(result);
                        Log.i("message", challenges[0].getName());
                        init();
                        getChoosenChallenge();

                        setText();
                    }
                });
    }

    private void init(){
        listView = (ListView) findViewById(R.id.challengesList);
    }

    private void getChoosenChallenge(){
        intent = getIntent();
        String challengeID =  intent.getStringExtra("CHALLENGE_ID");
        int lenght = challenges.length;

        for(int i = 0; i < lenght ; i++){
            if(challengeID.equals(challenges[i].getId())){
                challengesDone.add(challenges[i]);
            }
        }


    }

    private void setText(){
        for(Challenge challenge: challengesDone){
            challengesTitles.add(challenge.getName());
        }

        listAdapter = new ArrayAdapter<>(this, R.layout.simplerow, challengesTitles);

        listView.setAdapter(listAdapter);
    }

    private User userDummy(){
        List<Challenge> challenges = new ArrayList<>();
        challenges.add(new Challenge("1","Challenge 1","testtest",Difficulty.easy,null));
        challenges.add(new Challenge("2","Challenge 2","testtest",Difficulty.easy,null));
        challenges.add(new Challenge("3","Challenge 3","testtest",Difficulty.easy,null));

        return new User("Brecht","Tanghe","brecht_tanghe@hotmail.com","10/07/1995", Gender.Male, Difficulty.easy,"test1234","brechttanghe",true,true , false , challenges);
    }
}

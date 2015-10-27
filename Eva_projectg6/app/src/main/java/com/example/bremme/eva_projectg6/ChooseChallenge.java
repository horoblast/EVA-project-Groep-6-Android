package com.example.bremme.eva_projectg6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class ChooseChallenge extends AppCompatActivity {

    private RestApiRepository repo;
    private Challenge[] challenges;
    private List<Challenge> randomChallengeList;
    private Button challenge1;
    private Button challenge2;
    private Button challenge3;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_challenge);
        userLocalStore = new UserLocalStore(this);
        repo = new RestApiRepository();

        //getChallenges();
        challenges = getDummyData();
        randomChallengeList = getRandomChallengesOnDifficulty(Difficulty.Easy);

        init();

        setTextChallenges();

        challengeBtnClicked();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_challenge, menu);
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

    private List<Challenge> getRandomChallengesOnDifficulty(Difficulty difficulty){
        int length = challenges.length;
        List<Challenge> challengeList = new ArrayList<>();
        List<Challenge> randomList = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < length;i++) {
            if (challenges[i].getDifficulty() == difficulty) {
                challengeList.add(challenges[i]);
            }
        }
        for(int i = 0; i < 3; i++){
            int index = random.nextInt(challengeList.size());
            randomList.add(challengeList.get(index));
            challengeList.remove(index);
        }
        return randomList;
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
                        Log.i("message",challenges[0].getName());
                        setTextChallenges();
                    }
                });
    }


    private void init(){
        challenge1 = (Button) findViewById(R.id.btnChallenge1);
        challenge2 = (Button) findViewById(R.id.btnChallenge2);
        challenge3 = (Button) findViewById(R.id.btnChallenge3);
    }

    private void setTextChallenges()
    {
        challenge1.setText(randomChallengeList.get(0).getName());
        challenge2.setText(randomChallengeList.get(1).getName());
        challenge3.setText(randomChallengeList.get(2).getName());
    }

    private void challengeBtnClicked(){
        challenge1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChallengeDialog(0);
            }
        });

        challenge2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChallengeDialog(1);
            }
        });

        challenge3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChallengeDialog(2);
            }
        });
    }

    private void showChallengeDialog(int index){
        new AlertDialog.Builder(ChooseChallenge.this)
                .setTitle(randomChallengeList.get(index).getName())
                .setMessage(randomChallengeList.get(index).getDescription())
                .setPositiveButton("oke", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("anuleer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .show();
    }

    private Challenge[] getDummyData(){
        return new Challenge[]{
                new Challenge("1","Challenge 1" , "bsjbvbqbvjbjvbbvb" , Difficulty.Easy, null),
                new Challenge("2","Challenge 2" , "bsjbvbqbvjbjvbbvb" , Difficulty.Hard, null),
                new Challenge("3","Challenge 3" , "bsjbvbqbvjbjvbbvb" , Difficulty.Hard, null),
                new Challenge("4","Challenge 4" , "bsjbvbqbvjbjvbbvb" , Difficulty.Easy, null),
                new Challenge("5","Challenge 5" , "bsjbvbqbvjbjvbbvb" , Difficulty.Medium, null),
                new Challenge("6","Challenge 6" , "bsjbvbqbvjbjvbbvb" , Difficulty.Hard, null),
                new Challenge("7","Challenge 7" , "bsjbvbqbvjbjvbbvb" , Difficulty.Easy, null),
                new Challenge("8","Challenge 8" , "bsjbvbqbvjbjvbbvb" , Difficulty.Medium, null),
                new Challenge("9","Challenge 9" , "bsjbvbqbvjbjvbbvb" , Difficulty.Medium, null)
        };
    }

}

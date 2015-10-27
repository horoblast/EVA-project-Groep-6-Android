package com.example.bremme.eva_projectg6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.concurrent.ThreadLocalRandom.*;


public class ChooseChallenge extends AppCompatActivity {

    private RestApiRepository repo;
    private Challenge[] challenges;
    private Button challenge1;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_challenge);
        userLocalStore = new UserLocalStore(this);
        repo = new RestApiRepository();
        getChallenges();

        //challenges = new Challenge[]{
        //        new Challenge("1", "Challenge 1",  null),
        //        new Challenge("2", "Challenge 2",  null),
        //        new Challenge("3", "Challenge 3",  null),
        //        new Challenge("4", "Challenge 4",  null),
        //        new Challenge("5", "Challenge 5",  null)};

        //shuffleArray(challenges);

        challenge1 = (Button) findViewById(R.id.btnChallenge1);


    }

    static void shuffleArray(Challenge[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Challenge a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
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

    private void setTextChallenges()
    {
        challenge1.setText(challenges[0].getName());
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
}

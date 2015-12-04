package com.example.bremme.eva_projectg6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.bremme.eva_projectg6.Repository.DatabaseHelper;
import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.User;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class ChooseChallenge extends AppCompatActivity {

    private RestApiRepository repo;
    private Challenge[] challenges;
    private List<Challenge> randomChallengeList;
    private Button challenge1;
    private Button challenge2;
    private Button challenge3;
    private UserLocalStore userLocalStore;
    private Drawable dImages[];
    private DatabaseHelper localDb;
    private static int count=0;
    public static  String  CHALLENGE_ID = null;


    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_challenge);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_Challenge);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        userLocalStore = new UserLocalStore(this);
        randomChallengeList = new ArrayList<>();
        TextView text = (TextView) findViewById(R.id.userNameTool);
        if(userLocalStore.isUserLoggedIn())
        {
            text.setText(userLocalStore.getLoggedInUser().getFirstname());
        }else{
            text.setText("user onbekend");
        }
        repo = new RestApiRepository();
        dImages = new Drawable[3];
        getChallenges();
        //challenges = getDummyData();
        //setTextChallenges();
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
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            //todo settings page
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Challenge> getRandomChallengesOnDifficulty(User user){
        List<Challenge> challengeList = new ArrayList<>();
        List<Challenge> randomList = new ArrayList<>();
        StringBuilder sBuilder = new StringBuilder("[");
            int length = challenges.length;
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                if(user.getDif() ==Difficulty.easy)
                {
                    if (challenges[i].getDifficulty() == user.getDif() && isStudentFriendly(challenges[i],user.isStudent())&&isChildFriendly(challenges[i], user.HasChilderen())) {
                        challengeList.add(challenges[i]);
                    }
                }
                if(user.getDif() == Difficulty.medium)
                {
                    if (challenges[i].getDifficulty() == Difficulty.easy || challenges[i].getDifficulty() == Difficulty.medium) {
                        if(isStudentFriendly(challenges[i],user.isStudent())&&isChildFriendly(challenges[i],user.HasChilderen()))
                        challengeList.add(challenges[i]);
                    }
                }
                if(user.getDif() == Difficulty.hard)
                {
                    if (challenges[i].getDifficulty() == Difficulty.medium || challenges[i].getDifficulty() == Difficulty.hard ) {
                        if(isStudentFriendly(challenges[i],user.isStudent())&&isChildFriendly(challenges[i],user.HasChilderen()))
                        challengeList.add(challenges[i]);
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                //suggestie gevonden op niveau en in db steken
                int index = random.nextInt(challengeList.size());
                randomList.add(challengeList.get(index));
                sBuilder.append("\"");
                sBuilder.append(challengeList.get(index).getId());
                sBuilder.append("\"");
                if(i<2)
                    sBuilder.append(", ");
                Log.i("id toegevoegd: ",challengeList.get(index).getId());
                challengeList.remove(index);
            }
        sBuilder.append("]");
        putSuggestiesInDb(sBuilder.toString());
        return randomList;
        }

    private boolean isStudentFriendly(Challenge c,boolean student)
    {
        boolean status = true;
        if(student == true)
        {
            if(c.isStudentFriendly()!= true)
                status = false;
        }
        return status;
    }
    private boolean isChildFriendly(Challenge c , boolean child)
    {
        boolean status = true;
        if(child == true)
        {
            if(c.isChildFriendly()!= true)
                status = false;
        }
        return status;
    }
    private void putSuggestiesInDb(String challenges)
    {
        Ion.with(this).load(repo.getSETALLSUGGESTIONS())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .setBodyParameter("username", userLocalStore.getLoggedInUser().getUsername())
                .setBodyParameter("challengessuggestions", challenges)
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                //todo startuserseries
            }
        });
    }

    //haalt alle challenges op
    private void getChallenges()
    {//todo challenge lijst api call
        //kijken of user al suggesties heeft
        User u = userLocalStore.getLoggedInUser();
        Log.i("SUGGESTIES ZIJN ER AL ?", userLocalStore.getLoggedInUser().getSuggestionIds().size() + "");
        if(userLocalStore.getLoggedInUser().getSuggestionIds().size()!=0)
        {
            for(String id : userLocalStore.getLoggedInUser().getSuggestionIds())
            {
                Ion.with(this)
                        .load(repo.getFINDCHALLENGEBYID())
                        .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                        .setBodyParameter("_id", id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                count++;
                                String language = getResources().getConfiguration().locale.getLanguage();
                                Challenge c = repo.getChallenge(result,language);
                                randomChallengeList.add(c);
                                if(count==3){

                                    initDisplay();
                                    count =0;
                                }
                    }
                });
            }//user heeft geen suggesties -> nieuwe suggesties ophalen
        }else {
            /*Cursor cursor = localDb.getAllData();
            cursor.moveToFirst();
            ArrayList<Challenge> challengesList = new ArrayList<>();
            while(!cursor.isAfterLast()){
                Challenge c = new Challenge(cursor.getString(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("NAME")),
                        cursor.getString(cursor.getColumnIndex("Description")),
                        Difficulty.valueOf(cursor.getString(cursor.getColumnIndex("DIFFICULTY"))),
                        cursor.getString(cursor.getColumnIndex("URL")),
                        cursor.getInt(cursor.getColumnIndex("ISSTUDENTFRIENDLY"))>0,
                        cursor.getInt(cursor.getColumnIndex("ISCHILDFRIENDLY"))>0);

                challengesList.add(c);
                cursor.moveToNext();
            }
            cursor.close();
            challenges = challengesList.toArray(new Challenge[challengesList.size()]);*/
            Ion.with(this)
                    .load(repo.getChallenges())
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            String language = getResources().getConfiguration().locale.getLanguage();
                            Log.i("Locale",language);
                            challenges = repo.getAllChallenges(result,language);
                            randomChallengeList = getRandomChallengesOnDifficulty(userLocalStore.getLoggedInUser());
                            initDisplay();
                        }
                    });
        }
    }


    private void initDisplay()
    {
        init();
        setTextChallenges();
        challengeBtnClicked();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 3; i++) {
                        dImages[i] = loadImageFromWebOperations(randomChallengeList.get(i).getUrl().toString());
                    }
                    //Your code goes here
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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
    //methode voor het kiezen van een challenge
    private void showChallengeDialog(final int index) {

        try {
            AlertDialog.Builder builder =new AlertDialog.Builder(ChooseChallenge.this)
                    .setTitle(randomChallengeList.get(index).getName()).setIcon(dImages[index])
                    .setMessage(randomChallengeList.get(index).getDescription())
                    .setPositiveButton("Kies challenge", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setCurrentChallengeUser(randomChallengeList.get(index));
                        }
                    })
                    .setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!  lal
                        }
                    });

           final AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.challengedialog, null);
            LinearLayout linearLayout = (LinearLayout) dialogLayout.findViewById(R.id.challengeLayout);
            ImageView image = new ImageView(this);
            image.setImageDrawable(scaleImage(dImages[index]));
            linearLayout.addView(image);
            dialog.setView(dialogLayout);
            dialog.show();
        } catch (Exception e) {
        }
    }

    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            Drawable drawable = new ScaleDrawable(d, 0,400, 400).getDrawable();
            drawable.setBounds(0,0,400,400);
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }
    public Drawable scaleImage(Drawable image)
    {
        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 600, 350, false);
        image = new BitmapDrawable(getResources(), bitmapResized);
        return image;
    }

    private void setCurrentChallengeUser(final Challenge c) {

        Ion.with(this)
                .load(repo.getCURRENTCHALLENGE())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .setBodyParameter("username",userLocalStore.getLoggedInUser().getUsername())
                .setBodyParameter("_id",c.getId()).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                goToViewChallenge(c);
            }
        });
    }
    private void goToViewChallenge(Challenge challenge)
    {
        Intent intent = new Intent(ChooseChallenge.this, ViewChallenges.class);
        intent.putExtra("CHALLENGE_ID", challenge.getId());
        startActivity(intent);
    }


}

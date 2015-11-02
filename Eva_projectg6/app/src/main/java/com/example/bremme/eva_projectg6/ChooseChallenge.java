package com.example.bremme.eva_projectg6;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;


public class ChooseChallenge extends AppCompatActivity {

    private RestApiRepository repo;
    private Challenge[] challenges;
    private List<Challenge> randomChallengeList;
    private Button challenge1;
    private Button challenge2;
    private Button challenge3;
    private UserLocalStore userLocalStore;
    private Drawable dImages[];
    public static  String  CHALLENGE_ID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_challenge);
        userLocalStore = new UserLocalStore(this);
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
                        Log.i("message", challenges[0].getName());
                        init();
                        randomChallengeList = getRandomChallengesOnDifficulty(userLocalStore.getLoggedInUser().getDif());
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

    private void showChallengeDialog(final int index) {

        try {
            AlertDialog.Builder builder =new AlertDialog.Builder(ChooseChallenge.this)
                    .setTitle(randomChallengeList.get(index).getName()).setIcon(dImages[index])
                    .setMessage(randomChallengeList.get(index).getDescription())
                    .setPositiveButton("Kies challenge", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ChooseChallenge.this, ViewChallenges.class);
                            Challenge challenge = randomChallengeList.get(index);
                            intent.putExtra("CHALLENGE_ID", challenge.getId());
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    });

           final AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.challengedialog, null);
            //ImageView imageV = (ImageView)  dialogLayout.findViewById(R.id.challengeDialogImage);
            LinearLayout linearLayout = (LinearLayout) dialogLayout.findViewById(R.id.challengeLayout);
            ImageView image = new ImageView(this);
           Log.i("look bremme",dImages[index].getClass()+"");
            image.setImageDrawable(scaleImage(dImages[index]));
            linearLayout.addView(image);
            dialog.setView(dialogLayout);
            //ImageView imageV2 = (ImageView) dialog.findViewById(R.id.challengeDialogImage) ;
            dialog.show();
        } catch (Exception e) {
            Log.i("yoloo", "fack");
        }
    }

    private Challenge[] getDummyData(){
        return new Challenge[]{
                new Challenge("1","Challenge 1" , "bsjbvbqbvjbjvbbvb" , Difficulty.easy, null),
                new Challenge("2","Challenge 2" , "bsjbvbqbvjbjvbbvb" , Difficulty.hard, null),
                new Challenge("3","Challenge 3" , "bsjbvbqbvjbjvbbvb" , Difficulty.hard, null),
                new Challenge("4","Challenge 4" , "bsjbvbqbvjbjvbbvb" , Difficulty.easy, null),
                new Challenge("5","Challenge 5" , "bsjbvbqbvjbjvbbvb" , Difficulty.medium, null),
                new Challenge("6","Challenge 6" , "bsjbvbqbvjbjvbbvb" , Difficulty.hard, null),
                new Challenge("7","Challenge 7" , "bsjbvbqbvjbjvbbvb" , Difficulty.easy, null),
                new Challenge("8","Challenge 8" , "bsjbvbqbvjbjvbbvb" , Difficulty.medium, null),
                new Challenge("9","Challenge 9" , "bsjbvbqbvjbjvbbvb" , Difficulty.medium, null)
        };
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

}

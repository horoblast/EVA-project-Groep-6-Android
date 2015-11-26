package com.example.bremme.eva_projectg6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.User;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class FacebookRegister extends AppCompatActivity {

    private RadioGroup group2;
    private CheckBox cStudent;
    private CheckBox cChildren;
    private RestApiRepository repo;
    private UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_register);
        repo = new RestApiRepository();
        group2 = (RadioGroup) findViewById(R.id.radio2);
        cStudent = (CheckBox) findViewById(R.id.cStudent);
        cChildren = (CheckBox) findViewById(R.id.cChildren);
        userLocalStore = new UserLocalStore(this);
    }

    public void register(View view)
    {
        final Bundle bundle = getIntent().getExtras();
        boolean isStudent = cStudent.isChecked();
        boolean hasChildren = cChildren.isChecked();
        int id = group2.getCheckedRadioButtonId();
        View radioButton = group2.findViewById(id);
        int index =group2.indexOfChild(radioButton);
        Difficulty dif = Difficulty.easy;
        if(index==0){
            dif = Difficulty.easy;
        }
        if(index==1)
        {
            dif = Difficulty.medium;
        }
        if(index==2)
        {
            dif = Difficulty.hard;
        }
        final ProgressDialog dialog = ProgressDialog.show(this,getResources().getString(R.string.waitScreen),this.getResources().getString(R.string.userInloggen),true);
        Ion.with(this).load(repo.getFACEBOOKREGISTREER())
                .setBodyParameter("username",bundle.getString("name"))
                .setBodyParameter("firstname",bundle.getString("firstname"))
                .setBodyParameter("lastname",bundle.getString("lastname"))
                .setBodyParameter("gender",bundle.getString("gender"))
                .setBodyParameter("birthdate",bundle.getString("birthdate"))
                .setBodyParameter("facebookid",bundle.getString("id"))
                .setBodyParameter("email", bundle.getString("email"))
                .setBodyParameter("haschildren",hasChildren+"")
                .setBodyParameter("difficulty",dif.toString())
                .setBodyParameter("isstudent",isStudent+"")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String token = result.getAsJsonObject().get("token").getAsString();
                        userLocalStore.setToken(token);
                        userInloggen(bundle.getString("id"),dialog);

                    }
                });
    }
    private void userInloggen(String id, final ProgressDialog dialog)
    {
        Ion.with(this).load(repo.getFACEBOOKLOGINCHECK())
                .setBodyParameter("facebookid", id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                            if(result.get(0).isJsonObject())
                            {
                                JsonObject j = result.get(0).getAsJsonObject();
                                User newUser = repo.getUser(j);
                                userLocalStore.setUserLoggedIn(true);
                                userLocalStore.storeUserData(newUser);
                                challengesBekijken();
                                dialog.dismiss();
                            }else
                            {
                                dialog.dismiss();
                            }
                        }//facebook user inloggen
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facebook_register, menu);
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
    private void challengesBekijken()
    {
        startActivity(new Intent(this, ChooseChallenge.class));
    }
}

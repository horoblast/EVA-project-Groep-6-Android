package com.example.bremme.eva_projectg6;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.Icon;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.*;
import com.koushikdutta.async.future.FutureCallback;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.io.InputStream;
import java.util.Date;
import java.util.ResourceBundle;

public class Register extends AppCompatActivity {

    private EditText day;
    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private EditText email;
    private EditText month;
    private EditText year;
    private EditText password;
    private EditText passwordRepeat;
    private Button registerButton;
    private RadioGroup group1;
    private RadioGroup group2;
    private final int ELEMENTS=9;//#elementen die we willen checken
    private boolean valArray[];
    private UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initEditTexts();
        valArray = new boolean[ELEMENTS];// elementen die we willen checken
        for(int i =0;i<valArray.length;i++)//elementen initieel op false
        {
            valArray[i] = false;
        }
        addValidation();
        registerButton.setClickable(false);
        userLocalStore = new UserLocalStore(this);
    }
    public void register(View view)//onclick actie registratiebutton haalt data op van velden een maak nieuwe user aan
    {
        showProgressBar();
        Gender g =  Gender.Male;
        if(getIdFromRadioGroup(group1)==0)
            g = Gender.Male;
        else
            g = Gender.Female;
        Status s = Status.Married;
        switch (getIdFromRadioGroup(group2)){
            case 0: s = Status.Married;


            case 1: s= Status.InRelationShip;


            case 2: s = Status.Single;
        }
        User newUser = new User(getText(firstname),getText(lastname),getText(email),getText(day)+"/"+getText(month)+"/"+getText(year),g,s,getText(password),getText(username));
        userLocalStore.setUserLoggedIn(true);
        //putUserInDb(newUser);
        //TODO user in database steken
    }
    private void enableClickableRegisterButton()//zorgt dat je op de registratiebutton kunt klikken waneer alle registratievelden goed zijn ingevuld
    {
        int check =0;
        for(int i = 0;i<valArray.length;i++)
        {

           if(valArray[i]==false)
               break;
            check++;
        }
        Log.i("ENABLEE",check+" "+ELEMENTS);
        registerButton.setClickable(check == ELEMENTS);
    }
    private void showProgressBar()//verwijdert de registratieknop en toont een progressbar
    {
        ViewGroup layout = (ViewGroup) registerButton.getParent();
        if(layout!=null)
            layout.removeView(registerButton);
        try{
            layout.addView(new ProgressBar(this));
        }catch (NullPointerException er)
        {
            Log.i("nullpointerProgress","fout met button verwijderen");
        }catch(Exception e)
        {
            Log.i("nullpointerProgress","fout met button verwijderen");
        }


    }
    private void initEditTexts()//view elementen initialiseren
    {
        group1 = (RadioGroup) findViewById(R.id.radio1);
        group2 = (RadioGroup) findViewById(R.id.radio2);
        firstname = (EditText) findViewById(R.id.rFirstname);
        lastname = (EditText) findViewById(R.id.rLastname);
        username = (EditText) findViewById(R.id.rUsername);
        email = (EditText) findViewById(R.id.rEmail);
        day = (EditText) findViewById(R.id.rDay);
        month = (EditText) findViewById(R.id.rMonth);
        year = (EditText) findViewById(R.id.rYear);
        password = (EditText) findViewById(R.id.rPassword);
        passwordRepeat = (EditText) findViewById(R.id.rPasswordRepeat);
        registerButton = (Button) findViewById(R.id.rRegister);
        firstname.setError(getResources().getString(R.string.rFirstnameVal));
    }
    private void addValidation()//validatie toevoegen op de views
    {
        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( firstname.getText().toString().length()==0 ){
                    firstname.setError(getResources().getString(R.string.rFirstnameVal));
                    valArray[0] =false;
                    registerButton.setClickable(false);
                }{
                    valArray[0] = true;
                    enableClickableRegisterButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(lastname.getText().toString().length()==0)
                {
                    lastname.setError(getResources().getString(R.string.rLastnameVal));
                    valArray[1] = false;
                    registerButton.setClickable(false);
                }else {
                    valArray[1] = true;
                    enableClickableRegisterButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( username.getText().toString().length()==0 ){
                    username.setError(getResources().getString(R.string.rUsernameVal));
                    valArray[8] =false;
                    Log.i("BUTTON FALSE","REGISTER BUTTON FALSE");
                    registerButton.setClickable(false);
                }else{
                    Log.i("BUTTON TRUE","REGISTER BUTTON TRUE "+username.getText().toString().length());
                    valArray[8] = true;
                    enableClickableRegisterButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                //TODO kijk of de username al bestaat
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    email.setError(getResources().getString(R.string.rEmailVal));
                    valArray[2] = false;
                    registerButton.setClickable(false);
                }else{
                    valArray[2] = true;
                    enableClickableRegisterButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            int dag =0;
                try{
                    dag = Integer.parseInt(day.getText().toString());
                    if(dag<0||dag>=32||day.getText().toString().length()==0){
                        day.setError(getResources().getString(R.string.rDayVal));
                        valArray[3] = false;
                        registerButton.setClickable(false);
                    }else{
                        valArray[3] = true;
                        enableClickableRegisterButton();
                    }

                }catch (Exception e)
                {
                    day.setError(getResources().getString(R.string.parseException));
                    valArray[3] = false;
                    registerButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int maand=0;
                try{
                    maand = Integer.parseInt(month.getText().toString());
                    if(maand<0||maand>12||month.getText().toString().length()==0){
                        month.setError(getResources().getString(R.string.rMonthVal));
                        valArray[4] = false;
                        registerButton.setClickable(false);

                    }else{
                        valArray[4] = true;
                        enableClickableRegisterButton();
                    }

                }catch (Exception e)
                {
                    month.setError(getResources().getString(R.string.parseException));
                    valArray[4] = false;
                    registerButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int jaar=0;
                try{
                    jaar = Integer.parseInt(year.getText().toString());
                    if((jaar>0&&jaar<100)||(jaar>1900&&jaar< Calendar.getInstance().get(Calendar.YEAR)))
                    {
                        valArray[5] = true;
                        enableClickableRegisterButton();
                    }else{
                        year.setError(getResources().getString(R.string.rYearVal));
                        valArray[5] = false;
                        registerButton.setClickable(false);
                    }


                }catch (Exception e)
                {
                    year.setError(getResources().getString(R.string.parseException));
                    valArray[5] = false;
                    registerButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!password.getText().toString().matches("(?=.*[0-9])(?=\\S+$).{8,}$")){
                password.setError(getResources().getString(R.string.rPassVal));
                valArray[6] = false;
                registerButton.setClickable(false);
            }else{
                valArray[6] = true;
                enableClickableRegisterButton();
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Log.i("LET HIET OP", password.getText().toString() + "   -  " + passwordRepeat.getText().toString());
                    if (password.getText().toString().compareTo(passwordRepeat.getText().toString()) != 0) {
                        passwordRepeat.setError(getResources().getString(R.string.rMissmatchVal));
                        valArray[7] = false;
                        registerButton.setClickable(false);
                    } else {
                        valArray[7] = true;
                        enableClickableRegisterButton();
                    }
                } catch (Exception e) {
                    passwordRepeat.setError(getResources().getString(R.string.rMissmatchVal));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }
    private String getText(EditText editField)
    {
        return editField.getText().toString();
    }

    private int getIdFromRadioGroup(RadioGroup group)//geef het id van de radiobutton terug
    {
        int id = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(id);
        return group.indexOfChild(radioButton);
        
    }
    private void putUserInDb(User u) throws IOException {

    }
}

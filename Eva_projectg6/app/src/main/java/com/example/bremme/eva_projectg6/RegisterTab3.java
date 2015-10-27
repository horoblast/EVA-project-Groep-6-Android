package com.example.bremme.eva_projectg6;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.Gender;
import com.example.bremme.eva_projectg6.domein.User;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by BREMME on 22/10/15.
 */
public class RegisterTab3 extends Fragment{

    private Button registerButton;
    private EditText email;
    private EditText password;
    private EditText passwordRepeat;
    private RegisterTab1 tab1;
    private RegisterTab2 tab2;
    private RestApiRepository repo;
    private UserLocalStore userLocalStore;
    public RegisterTab3(RegisterTab1 tab1, RegisterTab2 tab2) {
        this.tab1 = tab1;
        this.tab2 = tab2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("onCreate", "Create tab3");
        View v = inflater.inflate(R.layout.register_tab3,container,false);
        repo = new RestApiRepository();
        userLocalStore = new UserLocalStore(this.getContext());
        init(v);
        addValidation();
        register();
        return v;
    }
    private void addValidation()
    {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError(getResources().getString(R.string.rEmailVal));
                } else {
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
                if (!password.getText().toString().matches("(?=.*[0-9])(?=\\S+$).{8,}$")) {
                    password.setError(getResources().getString(R.string.rPassVal));

                } else {
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
                    if (password.getText().toString().compareTo(passwordRepeat.getText().toString()) != 0) {
                        passwordRepeat.setError(getResources().getString(R.string.rMissmatchVal));

                    } else {
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
    private void init(View v)
    {
        email = (EditText) v.findViewById(R.id.rEmail);
        password = (EditText) v.findViewById(R.id.rPassword);
        passwordRepeat = (EditText) v.findViewById(R.id.rPasswordRepeat);
        registerButton = (Button) v.findViewById(R.id.rRegister);
        email.setError(getResources().getString(R.string.rEmailVal));
        password.setError(getResources().getString(R.string.rPassVal));
    }
    private void register()
    {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tab1.isCompleted() && tab2.isCompleted() && isCompleted()) {
                    Gender g = Gender.Male;
                    if (tab2.getGroup1Index() == 0)
                        g = Gender.Male;
                    else
                        g = Gender.Female;

                    Difficulty dif = Difficulty.Easy;
                    switch (tab2.getGroup2Index()) {
                        case 0:
                            dif = Difficulty.Easy;
                        case 1:
                            dif = Difficulty.Medium;
                        case 2:
                            dif = Difficulty.Hard;
                    }
                    Log.i("dificulty",dif.toString()+"");
                    int j = 0;
                    //omdat we bv 94 als jaar kunnen ingeven
                    if (tab2.getYear() < 100) {
                        j = tab2.getYear() + 1900;
                    } else {
                        j = tab2.getYear();
                    }
                    User newUser = new User(tab1.getFirstname(), tab1.getLastname(), email.getText().toString(), j+"-"+tab2.getMonth()+"-"+tab2.getDay(), g,dif, password.getText().toString(), tab1.getUsername(), false,tab2.isStudent(),tab2.hasChildren());
                    Log.i("INDEX RADIOG 2",tab2.getGroup2Index()+"");
                    Log.i("HIEERZOO", newUser.toString());
                    putUserInDb(newUser);
                }
            }
        });
    }
    public boolean isCompleted()
    {
        return email.getError()==null&&password.getError()==null&&passwordRepeat.getError()==null;
    }

    private void putUserInDb(User user) {
        final ProgressDialog dialog = ProgressDialog.show(RegisterTab3.this.getContext(),getResources().getString(R.string.waitScreen),this.getResources().getString(R.string.userInloggen),true);
        Ion.with(this)
                .load(repo.getRegister())
                .setBodyParameter("username", user.getUsername())
                .setBodyParameter("password", user.getPassword())
                .setBodyParameter("firstname", user.getFirstname())
                .setBodyParameter("lastname", user.getLastname())
                .setBodyParameter("difficulty", user.getDif().toString().toLowerCase())
                .setBodyParameter("isstudent", user.isStudent()+"")
                .setBodyParameter("haschildren", user.HasChilderen()+"").setBodyParameter("gender",user.getGender().toString().toLowerCase())
                .setBodyParameter("email", user.getEmail())
                .setBodyParameter("birthdate",user.getGebDatum())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //todo start een nieuwe activity
                        Intent i = new Intent(getContext(), LogIn.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });

    }
}

package com.example.bremme.eva_projectg6;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Random;

/**
 * Created by BREMME on 22/10/15.
 */
public class RegisterTab1 extends Fragment {

    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private Button b;
    private RestApiRepository repo;
    private static int count = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.register_tab1,container,false);
        Log.i("messageCkunt : ",count+"");
        init(v);
        addValidation();
        return v;
    }
    private void init(View v)
    {
        firstname = (EditText) v.findViewById(R.id.rFirstname);
        lastname = (EditText) v.findViewById(R.id.rLastname);
        username = (EditText) v.findViewById(R.id.rUsername);
        if(count==0){
            firstname.setError(getResources().getString(R.string.rFirstnameVal));
            lastname.setError(getResources().getString(R.string.rLastnameVal));
            username.setError(getResources().getString(R.string.rUsernameVal));
        }

        repo = new RestApiRepository();
        Log.i("text in firstname : is", firstname.getText().toString());
        b= (Button) v.findViewById(R.id.buttonFill);
        b.setText("");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();

                firstname.setText("Arne");
                lastname.setText("De Bremme");
                username.setText("arnedebremme");
                firstname.setError(null);
                lastname.setError(null);
                username.setError(null);
            }
        });
        count++;
    }
    private void addValidation()
    {
        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (firstname.getText().toString().length() == 0) {
                    firstname.setError(getResources().getString(R.string.rFirstnameVal));
                }
                {
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

                if (lastname.getText().toString().length() == 0) {
                    lastname.setError(getResources().getString(R.string.rLastnameVal));
                } else {
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
                if (username.getText().toString().length() == 0) {
                    username.setError(getResources().getString(R.string.rUsernameVal));
                } else {
                    checkDuplicateUsername(username.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
}
    public boolean isCompleted()
    {
        boolean b =firstname.getError()==null&&lastname.getError()==null&&username.getError()==null;
        Log.i("IS THIS TRUEE?", b + "");
        return firstname.getError()==null&&lastname.getError()==null&&username.getError()==null;
    }

    public String getFirstname() {
        return firstname.getText().toString();
    }

    public String getLastname() {
        return lastname.getText().toString();
    }

    public String getUsername() {
        return username.getText().toString();
    }
    private void checkDuplicateUsername(String uName)
    {
        Ion.with(this)
                .load(repo.getUsernamecheck()).setBodyParameter("username",uName).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (result.equals("true")) {
                    username.setError(getResources().getString(R.string.duplicateUsername));
                }

            }
        });
    }

}

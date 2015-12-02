package com.example.bremme.eva_projectg6;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Difficulty;
import com.example.bremme.eva_projectg6.domein.User;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        private EditTextPreference prefUsername;
        private UserLocalStore userLocalStore;
        private EditTextPreference prefEmail;
        private CheckBoxPreference prefHasKids;
        private CheckBoxPreference  prefIsStudent;
        private ListPreference difficulty;
        @Override
        public PreferenceManager getPreferenceManager() {
            return super.getPreferenceManager();
        }

        @Override
        public void onDestroyView() {
            compareUserChanges();
            super.onDestroyView();

        }

        private void compareUserChanges()
        {
            Boolean isChanged = false;
            User u = userLocalStore.getLoggedInUser();
            if(!prefUsername.getText().toString().equals(userLocalStore.getUsername()))
            {
                isChanged = true;
                u.setUsername(prefUsername.getText().toString());
            }
            if(!prefEmail.getText().toString().equals(userLocalStore.getEmail())){
                isChanged = true;
                u.setEmail(prefEmail.getText().toString());
            }
            if(!prefHasKids.isChecked() == userLocalStore.getHasChildren())
            {
                isChanged = true;
                u.setHasChilderen(prefHasKids.isChecked());
            }
            if(!prefIsStudent.isChecked() == userLocalStore.getStudent())
            {
                isChanged = true;
                u.setIsStudent(prefIsStudent.isChecked());
            }
            if(!difficulty.getValue().equals(userLocalStore.getDifficulty()))
            {
                isChanged = true;
                u.setDif(getDificultyFromPref());
            }
            if(isChanged)
            {
                setChanges(u);
            }

        }
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            userLocalStore = new UserLocalStore(this.getContext());
            prefUsername = (EditTextPreference)findPreference("Username");
            prefEmail = (EditTextPreference)findPreference("Email");
            prefHasKids = (CheckBoxPreference)findPreference("Haschildren");
            prefIsStudent = (CheckBoxPreference)findPreference("IsStudent");
            difficulty = (ListPreference) findPreference("Difficulty");
            prefUsername.setText(userLocalStore.getUsername());
            prefEmail.setText(userLocalStore.getEmail());
            prefHasKids.setChecked(userLocalStore.getHasChildren());
            prefIsStudent.setChecked(userLocalStore.getStudent());
            difficulty.setValueIndex(getIndex(userLocalStore.getDifficulty()));
        }
        private int getIndex(String dif)
        {
            int x = 0;
            if(dif.equals("medium"))
                x = 1;
            else if(dif.equals("hard"))
                x=2;
            return x;
        }
        private Difficulty getDificultyFromPref()
        {
            return Difficulty.valueOf(difficulty.getValue());
        }
        private void setChanges(User u)
        {
            userLocalStore.storeUserData(u);
            RestApiRepository repo = new RestApiRepository();
            Ion.with(this)
                    .load(repo.getSUBMITUSERCHANGES())
                    .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                    .setBodyParameter("username",u.getUsername())
                    .setBodyParameter("firstname",u.getFirstname())
                    .setBodyParameter("lastname",u.getLastname())
                    .setBodyParameter("email",u.getEmail())
                    .setBodyParameter("isstudent",u.isStudent()+"")
                    .setBodyParameter("haschildren",u.HasChilderen()+"")
                    .setBodyParameter("difficulty",u.getDif().toString().toLowerCase())
                    .setBodyParameter("birthdate",u.getGebDatum())
                    .asString().setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    //todo things when gelukt
                    Log.i("Settings userC",result+" <-text");
                    Log.i("Settings userC",result+" <-text");
                }
            });
        }
    }
}


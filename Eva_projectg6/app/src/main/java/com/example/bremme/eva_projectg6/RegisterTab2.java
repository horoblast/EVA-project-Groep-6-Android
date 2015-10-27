package com.example.bremme.eva_projectg6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Calendar;

/**
 * Created by BREMME on 22/10/15.
 */
public class RegisterTab2 extends Fragment {
    private EditText day;
    private EditText month;
    private EditText year;
    private RadioGroup group1;
    private RadioGroup group2;
    private CheckBox cStudent;
    private CheckBox cChildren;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_tab2, container, false);
        init(v);
        addValidation();
        return v;
    }
    private void init(View v)
    {
        group1 = (RadioGroup) v.findViewById(R.id.radio1);
        group2 = (RadioGroup) v.findViewById(R.id.radio2);
        day = (EditText) v.findViewById(R.id.rDay);
        month = (EditText) v.findViewById(R.id.rMonth);
        year = (EditText) v.findViewById(R.id.rYear);
        cStudent = (CheckBox) v.findViewById(R.id.cStudent);
        cChildren = (CheckBox) v.findViewById(R.id.cChildren);
        day.setError(getResources().getString(R.string.rDayVal));
        month.setError(getResources().getString(R.string.rMonthVal));
        year.setError(getResources().getString(R.string.rYearVal));

    }
    private void addValidation() {
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int dag = 0;
                try {
                    dag = Integer.parseInt(day.getText().toString());
                    if (dag < 0 || dag >= 32 || day.getText().toString().length() == 0) {
                        day.setError(getResources().getString(R.string.rDayVal));
                    } else {
                    }

                } catch (Exception e) {
                    day.setError(getResources().getString(R.string.parseException));
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
                int maand = 0;
                try {
                    maand = Integer.parseInt(month.getText().toString());
                    if (maand < 0 || maand > 12 || month.getText().toString().length() == 0) {
                        month.setError(getResources().getString(R.string.rMonthVal));

                    } else {

                    }

                } catch (Exception e) {
                    month.setError(getResources().getString(R.string.parseException));

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
                int jaar = 0;
                try {
                    jaar = Integer.parseInt(year.getText().toString());
                    if ((jaar > 0 && jaar < 100) || (jaar > 1900 && jaar < Calendar.getInstance().get(Calendar.YEAR))) {

                    } else {
                        year.setError(getResources().getString(R.string.rYearVal));

                    }


                } catch (Exception e) {
                    year.setError(getResources().getString(R.string.parseException));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public boolean isStudent()
    {
        return cStudent.isChecked();
    }
    public boolean hasChildren()
    {
        return cChildren.isChecked();
    }
    public boolean isCompleted()
    {
        return day.getError()==null&&month.getError()==null&&year.getError()==null;
    }

    public int getDay() {
        return Integer.parseInt(day.getText().toString());
    }

    public int getGroup1Index() {
        return getIdFromRadioGroup(group1);
    }


    public int getGroup2Index() {
        return getIdFromRadioGroup(group2);
    }

    public int getMonth() {
        return Integer.parseInt(month.getText().toString());
    }

    public int getYear() {
        return Integer.parseInt(year.getText().toString());
    }
    private int getIdFromRadioGroup(RadioGroup group)//geef het id van de radiobutton terug
    {
        int id = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(id);
        return group.indexOfChild(radioButton);

    }
}

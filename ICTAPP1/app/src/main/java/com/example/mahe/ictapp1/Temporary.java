package com.example.mahe.ictapp1;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Temporary extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText et_name;
    EditText et_regno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary);

        sharedPreferences = getSharedPreferences("Registration",0);

        et_name = (EditText)findViewById(R.id.editText);
        et_regno = (EditText)findViewById(R.id.editText2);

        et_name.setText(sharedPreferences.getString("Name"," not found"));
        et_regno.setText(sharedPreferences.getString("Regno",null));

    }
}

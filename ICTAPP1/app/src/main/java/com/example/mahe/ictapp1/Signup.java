package com.example.mahe.ictapp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    EditText et_name;
    EditText et_regno;
    EditText et_pwd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RegisterUser registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_name = (EditText) findViewById(R.id.et_name);
        et_regno = (EditText) findViewById(R.id.et_regno);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
    }

    public void signup(View view)
    {
        int nm=0,rg=0,pwd=0;
        sharedPreferences = getSharedPreferences("Registration",0);
        editor = sharedPreferences.edit();


        String name = et_name.getText().toString();
        if(name.length()<=0) {
            Toast.makeText(this, "Enter Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            nm = 1;
            editor.putString("Name",name);
        }


        String regno = et_regno.getText().toString();
        if(regno.length()<=0)
        {
            Toast.makeText(this,"Enter Registration Number",Toast.LENGTH_SHORT).show();
        }
        else
        {
            rg = 1;
            editor.putString("Regno",regno);
        }


        String password = et_pwd.getText().toString();
        if(password.length()<=0)
        {
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            pwd = 1;
            editor.putString("Password",password);
        }

        registerUser = new RegisterUser(regno,name,password);
        if(nm == 1 && rg == 1 && pwd == 1) {
            editor.putString("regno",regno);
            editor.putBoolean("Status",true);
            editor.commit();
            startActivity(new Intent(this, Nav.class));
        }
    }

    @Override
    public void onBackPressed() {
    }


}

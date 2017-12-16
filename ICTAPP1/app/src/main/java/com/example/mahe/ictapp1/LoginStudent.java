package com.example.mahe.ictapp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class LoginStudent extends AppCompatActivity {

    EditText regno;
    EditText pwd;
    public String st_regno;
    String st_pwd;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);

        regno = (EditText)findViewById(R.id.regno);
        pwd = (EditText)findViewById(R.id.pwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void loginClicked(View v)
    {

        st_regno = regno.getText().toString();
        st_pwd = pwd.getText().toString();

        new MyTask().execute();

    }


    public void clickHere(View view)
    {
        startActivity(new Intent(this,Signup.class));
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        List<String> regnos = new ArrayList<String>();
        List<String> pwds = new ArrayList<String>();


        @Override
        protected Void doInBackground(Void... arg0)
        {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,password);

                Statement st = con.createStatement();
                //String sql = "select * from ictapp1"
                final ResultSet rs = st.executeQuery("Select * from student_login");


                while(rs.next()) {
                    regnos.add(rs.getString(3));
                    pwds.add(rs.getString(4));
                }
                st.close();
                con.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            sharedPreferences = getSharedPreferences("Registration",0);
            editor = sharedPreferences.edit();
            if(regnos.contains(st_regno) && pwds.contains(st_pwd))
            {
                editor.putBoolean("Status",true);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), Nav.class);
                Bundle b = new Bundle();
                b.putString("regno",st_regno);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Invalid Registration No and Password",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

    }

    @Override
    public void onBackPressed() {
    }

}

package com.example.mahe.ictapp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoginFaculty extends AppCompatActivity {


    EditText username;
    EditText pwd;
    String f_username;
    String f_pwd;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login_faculty);

        username = (EditText)findViewById(R.id.username);
        pwd = (EditText)findViewById(R.id.pwd);
    }

    public void loginClicked(View v)
    {

        f_username = username.getText().toString();
        f_pwd = pwd.getText().toString();

        new MyTask().execute();

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        List<String> usernames = new ArrayList<String>();
        List<String> pwds = new ArrayList<String>();
        List<String> userids = new ArrayList<String>();

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,password);

                Statement st = con.createStatement();
                //String sql = "select * from ictapp1"
                final ResultSet rs = st.executeQuery("Select * from faculty_login");


                while(rs.next()) {
                    userids.add(rs.getString(1));
                    usernames.add(rs.getString(2));
                    pwds.add(rs.getString(3));
                }
                st.close();
                rs.close();
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

            if(usernames.contains(f_username) && pwds.contains(f_pwd))
            {
                int index;
                index = usernames.indexOf(f_username);
                String userid;
                userid = userids.get(index);
                sharedPreferences = getSharedPreferences("FacultyRegistration",0);
                editor = sharedPreferences.edit();
                editor.putString("userid",userid);
                editor.putBoolean("Status",true);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), FacultyHomeLayout.class);
                Bundle b = new Bundle();
                b.putString("userid",userid);
                intent.putExtras(b);
                startActivity(intent);
                //finish();
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

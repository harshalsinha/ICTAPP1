package com.example.mahe.ictapp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Nav_Appointments extends AppCompatActivity {

    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";

    EditText et_faculty;
    EditText et_date;
    EditText et_time;
    EditText et_location;

    String regno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__appointments);

        et_faculty = (EditText) findViewById(R.id.et_faculty);
        et_date = (EditText) findViewById(R.id.et_date);
        et_time = (EditText) findViewById(R.id.et_time);
        et_location = (EditText) findViewById(R.id.et_location);

        regno = null;
        Bundle b = getIntent().getExtras();
        if (b != null)
            regno = b.getString("regno");

        new MyTask().execute();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Nav.class);
        Bundle b = new Bundle();
        b.putString("regno",regno);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        //startActivity(new Intent(this,Nav.class));
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String date,time,location;
        String f_name;

        private final ProgressDialog dialog = new ProgressDialog(
                Nav_Appointments.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Refreshing data...");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,password);

                int S_SrNo = 0;

                Statement stInit = con.createStatement();
                final ResultSet rs = stInit.executeQuery("Select * from student_login where Regno like '"+regno+"' ");
                while(rs.next())
                {
                    S_SrNo = rs.getInt(1);
                }
                rs.close();
                stInit.close();

                Statement st = con.createStatement();

                String query;
                query = "Select * from appointments where S_SrNo = "+S_SrNo+" ";

                final ResultSet rs2 = st.executeQuery(query);

                String f_userid = null;

                while(rs2.next())
                {
                    f_userid = rs2.getString(2);
                    date = rs2.getString(3);
                    time = rs2.getString(4);
                    location = rs2.getString(5);
                }
                st.close();

                f_name = null;
                Statement stPost = con.createStatement();
                final ResultSet rs3 = stPost.executeQuery("Select * from faculty where SrNo = '"+f_userid+"'");
                while(rs3.next())
                {
                    f_name = rs3.getString(2);
                }
                rs3.close();
                stPost.close();

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
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            et_faculty.setText(f_name);
            et_date.setText(date);
            et_time.setText(time);
            et_location.setText(location);

            super.onPostExecute(result);


        }

    }


}

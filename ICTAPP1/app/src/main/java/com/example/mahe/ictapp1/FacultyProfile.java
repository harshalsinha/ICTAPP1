package com.example.mahe.ictapp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class FacultyProfile extends AppCompatActivity {

    String name;
    String regno;

    String F_userid;
    String S_userid;

    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Faculty Profile");
        setContentView(R.layout.activity_faculty_profile);

        Bundle b = getIntent().getExtras();
         name = null;
        if(b != null) {
            regno = b.getString("regno");
            name = b.getString("name");
        }
            new MyTask().execute();

    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        TextView tv_faculty_name;
        TextView tv_faculty_designation;
        TextView tv_faculty_domain;
        TextView tv_faculty_email;


        String f_names;
        String f_designations;
        String f_domains;
        String f_emails;


        private final ProgressDialog dialog = new ProgressDialog(
                FacultyProfile.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Refreshing data...");
            this.dialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0)
        {
            tv_faculty_name = (TextView)findViewById(R.id.faculty_name);
            tv_faculty_designation = (TextView)findViewById(R.id.faculty_designation);
            tv_faculty_domain = (TextView)findViewById(R.id.faculty_domain);
            tv_faculty_email = (TextView)findViewById(R.id.email);

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,password);

                Statement st = con.createStatement();
                //String sql = "select * from ictapp1"

                String query = "Select * from faculty where Name like '"+name+"' ";

                ResultSet rs = st.executeQuery(query);

                while(rs.next()) {
                    f_names = rs.getString(2);
                    f_designations = rs.getString(3);
                    f_domains = rs.getString(4);
                    f_emails = rs.getString(5);
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
            tv_faculty_name.setText(f_names);
            tv_faculty_designation.setText(f_designations);
            tv_faculty_domain.setText(f_domains);
            tv_faculty_email.setText(f_emails);

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            super.onPostExecute(result);
        }

    }

    private class MyTask2 extends AsyncTask<Void, Void, Void> {

        String availability,location;

        private final ProgressDialog dialog = new ProgressDialog(
                FacultyProfile.this);

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

                Statement st = con.createStatement();
                //String sql = "select * from ictapp1"

                String query = "Select * from faculty where Name like '"+name+"' ";

                ResultSet rs = st.executeQuery(query);
                F_userid = null;
                while(rs.next())
                    F_userid = rs.getString(1);

                st.close();

                Statement st4 = con.createStatement();
                query = "Select * from student_login where Regno = '"+regno+"' ";

                final ResultSet rs4 = st4.executeQuery(query);
                S_userid = null;
                while(rs4.next())
                {
                    S_userid = rs4.getString(1);
                }
                st4.close();
                rs4.close();

                Statement st2 = con.createStatement();
                query = "Select * from faculty_availability where SrNo = '"+F_userid+"' ";
                ResultSet rs2 = st2.executeQuery(query);

                availability = null;
                location = null;

                while(rs2.next()) {
                    availability = rs2.getString(2);
                    location = rs2.getString(3);
                }
                st2.close();

                if(availability.equals("Y") && location.equals(null))
                {
                    Statement st3 = con.createStatement();
                    query = "Select * from faculty where SrNo = '"+F_userid+"' ";
                    ResultSet rs3 = st3.executeQuery(query);

                    while (rs3.next())
                    {
                        location = rs3.getString(7);
                    }
                    st3.close();
                }

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
            Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();

            if(availability.equals("Y") )
                Toast.makeText(getApplicationContext(),"Available : "+availability+"ES"+" Location : "+location,Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Available : NO",Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
        }

    }

    private class MyTask3 extends AsyncTask<Void, Void, Void> {


        private final ProgressDialog dialog = new ProgressDialog(
                FacultyProfile.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Refreshing data...");
            this.dialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, password);

                Statement st = con.createStatement();

                String query;
                query = "Insert into appointment_requests(S_SrNo,F_SrNo) values ('" + S_userid + "','" + F_userid + "') ";
                st.executeUpdate(query);

                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            Toast.makeText(getApplicationContext(), "Appointment Requested:S_userid= "+S_userid+" F_userid="+F_userid, Toast.LENGTH_LONG).show();

        }

    }
    public void requestAppointment(View view)
    {
        new MyTask3().execute();
    }

    public void checkAvailability(View view)
    {
        new MyTask2().execute();
    }

    public void addQuick(View view)
    {
        Toast.makeText(getApplicationContext(),"Added To QuickList",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this,Nav.class));
    }
}

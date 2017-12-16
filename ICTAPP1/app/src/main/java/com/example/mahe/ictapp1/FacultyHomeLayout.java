package com.example.mahe.ictapp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FacultyHomeLayout extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String userid;
    String locationUpdate;

    EditText et_location;
    TextView tv_title;
    TextView tv_designation;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SwitchCompat switchCompat;

    int switchFlag;

    String[] appointmentTypes = {"Upcoming Appointments", "Pending Requests", "Appointments Completed"};
    int appointmtentImages[] = {R.drawable.ic_upcomin_appointments, R.drawable.ic_pending_appointments, R.drawable.ic_completed_appointments};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Faculty");
        setContentView(R.layout.activity_faculty_home_layout);

        switchFlag = 0;
        userid = null;
        Bundle b = getIntent().getExtras();
        if (b != null)
            userid = b.getString("userid");

        et_location = (EditText) findViewById(R.id.et_location);
        tv_title = (TextView) findViewById(R.id.Tv_title);
        tv_designation = (TextView) findViewById(R.id.Tv_designation);


        Spinner spin = (Spinner) findViewById(R.id.appointmentSpinner);
        spin.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), appointmtentImages, appointmentTypes);
        spin.setAdapter(customAdapter);

        switchCompat = (SwitchCompat) findViewById(R.id.switchButton);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    switchFlag = 1;
                }
                else
                {
                    switchFlag = 0;
                }
                new MyTask3().execute();
            }
        });

        new MyTask1().execute();

    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        switch (position)
        {
            case 1:
                break;
            case 2:
                Toast.makeText(this,appointmentTypes[position],Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(getApplicationContext(), AppointmentRequests.class);
                Bundle b = new Bundle();
                b.putString("userid",userid);
                intent.putExtras(b);
                startActivity(intent);*/
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.faculty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                // TODO put your code here to respond to the button tap
                //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_logout:
                sharedPreferences = getSharedPreferences("FacultyRegistration", 0);
                editor = sharedPreferences.edit();
                editor.putBoolean("Status", false);
                editor.commit();
                new MyTask4().execute();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void locationClicked(View view)
    {
        locationUpdate = et_location.getText().toString();
        new MyTask2().execute();
    }


    public void checkClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AppointmentRequests.class);
        Bundle b = new Bundle();
        b.putString("userid",userid);
        intent.putExtras(b);
        startActivity(intent);
    }

    private class MyTask1 extends AsyncTask<Void, Void, Void> {

        private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
        private static final String user = "sql12166501";
        private static final String password = "uEbMJ51LJm";


        private String title = null;
        private String designation = null;
        private String location = null;

        private final ProgressDialog dialog = new ProgressDialog(
                FacultyHomeLayout.this);

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

                String query = "Select * from faculty where SrNo = '" + userid + "'";

                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    title = rs.getString(2);
                    designation = rs.getString(3);
                    location = rs.getString(7);
                }

                st.close();
                rs.close();
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

            et_location.setText(location);
            tv_title.setText(title);
            tv_designation.setText(designation);
            super.onPostExecute(result);
        }

    }

    private class MyTask2 extends AsyncTask<Void, Void, Void> {

        private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
        private static final String user = "sql12166501";
        private static final String password = "uEbMJ51LJm";

        private final ProgressDialog dialog = new ProgressDialog(
                FacultyHomeLayout.this);

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

                String query = "Update faculty_availability set Location = '"+locationUpdate+"' where SrNo = '" + userid + "'";

                st.executeUpdate(query);

                st.close();
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

            Toast.makeText(getApplicationContext(),locationUpdate,Toast.LENGTH_LONG).show();
            et_location.setText(locationUpdate);
            super.onPostExecute(result);
        }


    }

    private class MyTask3 extends AsyncTask<Void, Void, Void> {

        private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
        private static final String user = "sql12166501";
        private static final String password = "uEbMJ51LJm";

        private final ProgressDialog dialog = new ProgressDialog(
                FacultyHomeLayout.this);

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

                if(switchFlag==1) {
                    query = "Update faculty_availability set Available = 'Y' where SrNo = '" + userid + "'";
                }
                else
                {
                    query = "Update faculty_availability set Available = 'N' where SrNo = '" + userid + "'";
                }
                st.executeUpdate(query);

                st.close();
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
            super.onPostExecute(result);
        }


    }

    private class MyTask4 extends AsyncTask<Void, Void, Void> {

        private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
        private static final String user = "sql12166501";
        private static final String password = "uEbMJ51LJm";
        private final ProgressDialog dialog = new ProgressDialog(
                FacultyHomeLayout.this);

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

                String query = "Update faculty_availability set Available = 'N' where SrNo = '" + userid + "'";

                st.executeUpdate(query);

                locationUpdate = null;

                query = "Update faculty_availability set Location = '"+locationUpdate+"' where SrNo = '" + userid + "'";

                st.executeUpdate(query);

                st.close();
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
            super.onPostExecute(result);
        }


    }

    @Override
    public void onBackPressed() {
    }

}

package com.example.mahe.ictapp1;

import org.apache.commons.lang3.StringUtils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class FinalizeAppointment extends AppCompatActivity {

    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";

    EditText et_location;
    EditText et_date;
    EditText et_time;

    String date;
    String time;
    String location;

    String F_userid;
    String S_userid;
    String regno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Appointment Details");
        setContentView(R.layout.activity_finalize_appointment);

        F_userid = null;
        S_userid = null;
        regno = null;
        Bundle b = getIntent().getExtras();
        if (b != null) {
            F_userid = b.getString("F_userid");
            regno = b.getString("regno");
        }
        et_location = (EditText)findViewById(R.id.et_appointment_location);
        et_date = (EditText)findViewById(R.id.et_appointment_date);
        et_time = (EditText)findViewById(R.id.et_appointmentTime);

        date = null;
        time = null;
        location = null;

    }

    public void doneClicked(View view)
    {
        date = et_date.getText().toString();
        time = et_time.getText().toString();
        location = et_location.getText().toString();

        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        private final ProgressDialog dialog = new ProgressDialog(
                FinalizeAppointment.this);

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

                Statement stInitial = con.createStatement();
                final ResultSet rs = stInitial.executeQuery("Select * from student_login where Regno = '"+regno+"' ");

                while(rs.next())
                {
                    S_userid = rs.getString(1);
                }

                stInitial.close();


                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO appointments (S_SrNo, F_SrNo, Date, Time, Location) VALUES ('"+S_userid+"', '"+F_userid+"', '"+date+"', '"+time+"', '"+location+"')");
                st.close();

                Statement stPost = con.createStatement();
                stPost.executeUpdate("Delete from appointment_requests where S_SrNo = '"+S_userid+"' and F_SrNo = '"+F_userid+"' ");
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

            Toast.makeText(getApplicationContext(),"S_userid = "+S_userid,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Appointment Finalized ",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), AppointmentRequests.class);
            Bundle b = new Bundle();
            b.putString("userid",F_userid);
            intent.putExtras(b);
            startActivity(new Intent(getApplicationContext(),FacultyHomeLayout.class));

            super.onPostExecute(result);
        }

    }

}

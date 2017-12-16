package com.example.mahe.ictapp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRequests extends AppCompatActivity {

    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";

    String[] titles;
    String[] subtitles;
    int[] images;

    String F_userid;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_requests);

        F_userid = null;
        Bundle b = getIntent().getExtras();
        if (b != null)
            F_userid = b.getString("userid");


        new MyTask().execute();
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        List<Integer> students = new ArrayList<Integer>();
        List<String> studentNames = new ArrayList<String>();
        List<String> studentRegs = new ArrayList<String>();

        int flag;

        private final ProgressDialog dialog = new ProgressDialog(
                AppointmentRequests.this);

        // can use UI thread here
        protected void onPreExecute() {
            flag = 0;
            this.dialog.setMessage("Refreshing data...");
            this.dialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, password);

                Statement st = con.createStatement();
                //String sql = "select * from ictapp1"
                final ResultSet rs = st.executeQuery("Select * from appointment_requests where F_SrNo = '"+F_userid+"'");


                while (rs.next()) {
                    students.add(rs.getInt(1));
                }
                st.close();
                rs.close();

                if (students.size() != 0) {
                    flag = 1;
                    int student_id;

                    for (int i = 0; i < students.size(); i++) {
                        Statement st2 = con.createStatement();

                        student_id = students.get(i);

                        final ResultSet rs2 = st2.executeQuery("Select * from student_login where SrNo = '" + student_id + "' ");
                        while (rs2.next()) {
                            studentNames.add(rs2.getString(2));
                            studentRegs.add(rs2.getString(3));
                        }

                    }
                    con.close();

                    titles = studentNames.toArray(new String[studentNames.size()]);
                    subtitles = studentRegs.toArray(new String[studentRegs.size()]);

                    images = new int[studentNames.size()];
                    for (int i = 0; i < studentNames.size(); i++)
                        images[i] = R.drawable.ic_pending_appointments;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            //  Toast.makeText(getApplicationContext(),"Inside Post Execute",Toast.LENGTH_LONG).show();

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            super.onPostExecute(result);

            if (flag == 1)
            {
                lv = (ListView) findViewById(R.id.list);

            ListAdapter listAdapter = new ListAdapter(AppointmentRequests.this, titles, subtitles, images);
            lv.setAdapter(listAdapter);

            //subtitles[0] += "Set:";
            //Toast.makeText(getApplicationContext(),subtitles[0],Toast.LENGTH_LONG).show();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(getApplicationContext(), FinalizeAppointment.class);
                        Bundle b = new Bundle();
                        b.putString("F_userid",F_userid);
                        b.putString("regno",subtitles[position]);
                        intent.putExtras(b);
                        startActivity(intent);
                }
            });

        }
            else
            {
                Toast.makeText(getApplicationContext(),"No Appointment Requests",Toast.LENGTH_LONG).show();
            }
    }

    }

}

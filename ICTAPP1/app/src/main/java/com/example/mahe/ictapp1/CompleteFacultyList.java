package com.example.mahe.ictapp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CompleteFacultyList extends AppCompatActivity {

    ListView lv;
    int flag;

    public ArrayList<String> f_names;
    public ArrayList<String> f_designations;
    public ArrayList<String> f_domains;
    public ArrayList<String> f_emails;


    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";

    String[] titles;
    String[] subtitles;
    int[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_faculty_list);
        f_names = new ArrayList<String>();
        f_designations = new ArrayList<String>();
        f_domains = new ArrayList<String>();
        f_emails = new ArrayList<String>();
        new MyTask().execute();
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {


        private final ProgressDialog dialog = new ProgressDialog(
                CompleteFacultyList.this);

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Refreshing data...");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            flag = 0;
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,password);

                Statement st = con.createStatement();
                //String sql = "select * from ictapp1"
                //"INSERT INTO `time_entry`(pid,tid,rid,tspend,description) VALUE ('"+pid+"','"+tid+"','"+rid+"',"+tspent+",'"+des+"')"
                //st.executeUpdate("INSERT INTO `student_info`(student_id,regno,name,password) VALUE (NULL,'"+regno+"','"+name+"','"+pwd+"')");


                String query;
                query = "Select * from faculty";

                flag = 1;

                ResultSet rs = st.executeQuery(query);

                while(rs.next())
                {
                    f_names.add(rs.getString(2));
                    f_designations.add(rs.getString(3));
                    f_domains.add(rs.getString(4));
                    f_emails.add(rs.getString(5));
                }
                st.close();
                con.close();

                titles = f_names.toArray(new String[f_names.size()]);

                subtitles = f_designations.toArray(new String[f_designations.size()]);


                images = new int[f_names.size()];
                for(int i=0;i<f_names.size();i++)
                    images[i] = R.drawable.ic_insert_emoticon_black_24dp;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            //  Toast.makeText(getApplicationContext(),"Inside Post Execute",Toast.LENGTH_LONG).show();

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            super.onPostExecute(result);

            lv = (ListView)findViewById(R.id.list);

            ListAdapter listAdapter = new ListAdapter(CompleteFacultyList.this,titles,subtitles,images);
            lv.setAdapter(listAdapter);

            //subtitles[0] += "Set:";
            //Toast.makeText(getApplicationContext(),subtitles[0],Toast.LENGTH_LONG).show();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), FacultyProfile.class);
                    Bundle b = new Bundle();
                    b.putString("name",titles[position]);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            });

        }

    }

}

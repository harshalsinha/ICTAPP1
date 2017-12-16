package com.example.mahe.ictapp1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.TransportPerformer;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lv;
    //String[] titles = {"Dr.Ajitha Shenoy","Mr.Arjun CV"};
    //String[] subtitles = {"Assistant Professor(Senior Scale)","Assistant Professor"};
    //int[] images = {R.drawable.rihanna,R.drawable.ic_insert_emoticon_black_24dp};

    String sem;

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
    String regno;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private boolean isStartup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Registration",0);


        setTitle("Current Sem Faculty");

        setContentView(R.layout.activity_nav);


        if(sharedPreferences.getBoolean("Status",false))
        {
            regno = sharedPreferences.getString(regno,"140953216");
        }
        else {
            Bundle b = getIntent().getExtras();
            if (b != null)
                regno = b.getString("regno");
        }



        if(regno.charAt(0)=='1' && regno.charAt(1)=='4')
        {
            sem = "6";
        }
        else if(regno.charAt(0)=='1' && regno.charAt(1)=='5')
        {
            sem = "4";
        }

        f_names = new ArrayList<String>();
        f_designations = new ArrayList<String>();
        f_domains = new ArrayList<String>();
        f_emails = new ArrayList<String>();

        //Toast.makeText(this,"Before MyTask()",Toast.LENGTH_LONG).show();
        new MyTask1().execute();
        //Toast.makeText(this,"After MyTask()",Toast.LENGTH_LONG).show();

        isStartup = false;


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment;

        sharedPreferences = getSharedPreferences("Registration",0);
        editor = sharedPreferences.edit();

        int id = item.getItemId();

        /*if (id == R.id.nav_quickList) {
            isStartup = false;
            if(isStartup == false) {
                ((LinearLayout) findViewById(R.id.content_nav)).removeAllViews();
            }
            fragment = new QuickList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_nav,fragment).commit();

        }*/ if (id == R.id.nav_faculty) {
            startActivity(new Intent(this,CompleteFacultyList.class));
            }

         else if (id == R.id.nav_appointments) {

            Intent intent = new Intent(getApplicationContext(), Nav_Appointments.class);
            Bundle b = new Bundle();
            b.putString("regno",regno);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_current) {
            startActivity(new Intent(this,Nav.class));

        } else if (id == R.id.nav_about) {

        }
        else if (id == R.id.nav_logout)
        {
            editor.clear();
            editor.commit();
            startActivity(new Intent(this,MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class MyTask1 extends AsyncTask<Void, Void, Void> {

        int flag;

        private final ProgressDialog dialog = new ProgressDialog(
                Nav.this);

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

                String semParameter = null;
                if(sem.equals("4"))
                    semParameter = "4A";
                else if(sem.equals("6"))
                    semParameter = "6B";

                String query;
                query = "Select * from faculty where Teaches like '"+semParameter+"' ";

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

            ListAdapter listAdapter = new ListAdapter(Nav.this,titles,subtitles,images);
            lv.setAdapter(listAdapter);

            //subtitles[0] += "Set:";
            //Toast.makeText(getApplicationContext(),subtitles[0],Toast.LENGTH_LONG).show();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), FacultyProfile.class);
                    Bundle b = new Bundle();
                    b.putString("name",titles[position]);
                    b.putString("regno",regno);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            });

        }

    }

}

package com.example.mahe.ictapp1;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class RegisterUser {
    String regno,name,pwd;
    private static final String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12166501";
    private static final String user = "sql12166501";
    private static final String password = "uEbMJ51LJm";
    public RegisterUser(String regno,String name,String pwd)
    {
        this.regno = regno;
        this.name = name;
        this.pwd = pwd;
        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0)
        {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,password);

                Statement st = con.createStatement();
                //String sql = "select * from ictapp1"
                //"INSERT INTO `time_entry`(pid,tid,rid,tspend,description) VALUE ('"+pid+"','"+tid+"','"+rid+"',"+tspent+",'"+des+"')"
                st.executeUpdate("INSERT INTO `student_info`(student_id,regno,name,password) VALUE (NULL,'"+regno+"','"+name+"','"+pwd+"')");
                st.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }

    }

}

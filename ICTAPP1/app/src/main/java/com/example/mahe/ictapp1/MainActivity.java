package com.example.mahe.ictapp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.R.attr.duration;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences_student;
    SharedPreferences sharedPreferences_faculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences_student = getSharedPreferences("Registration",0);
        sharedPreferences_faculty = getSharedPreferences("FacultyRegistration",0);

        if(sharedPreferences_student.getBoolean("Status",false))
        {
            Intent intent = new Intent(getApplicationContext(), Nav.class);
            Bundle b = new Bundle();
            b.putString("regno",sharedPreferences_student.getString("regno","140953216"));
            intent.putExtras(b);
            startActivity(intent);
            finish();
            //startActivity(new Intent(this,Nav.class));
            //Toast.makeText(this,sharedPreferences_student.getBoolean("Status",false)+"",Toast.LENGTH_SHORT).show();
        }
        else if(sharedPreferences_faculty.getBoolean("Status",false))
        {
            Intent intent = new Intent(getApplicationContext(), FacultyHomeLayout.class);
            //Bundle b = new Bundle();
            //b.putString("regno",sharedPreferences_student.getString("regno","140953216"));
            //intent.putExtras(b);
            startActivity(intent);
            //finish();
            //startActivity(new Intent(this,Nav.class));
            //Toast.makeText(this,sharedPreferences_student.getBoolean("Status",false)+"",Toast.LENGTH_SHORT).show();
        }
        else
            setContentView(R.layout.activity_main);
    }


    public void studentClicked(View view)
    {
        startActivity(new Intent(this,LoginStudent.class));
    }

    public void facultyClicked(View view){
        startActivity(new Intent(this,LoginFaculty.class));
    }

    @Override
    public void onBackPressed() {

    }

}

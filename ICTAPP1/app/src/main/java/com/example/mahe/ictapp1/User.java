package com.example.mahe.ictapp1;

/**
 * Created by MAHE on 17-Feb-17.
 */



public class User {
    String name,regno,pwd;
    Boolean status;
    public User(String regno,String pwd,String name)
    {
        this.name = name;
        this.regno = regno;
        this.pwd = pwd;
        status = true;
    }

    public User(String regno,String pwd)
    {
        this.regno = regno;
        this.pwd = pwd;
        this.name = "";
         status = true;
    }
}

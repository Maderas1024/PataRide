package com.jshikami235gmail.pataride;

/**
 * Created by student on 17/11/15.
 */
public class User {
    String fname;
    String lname;
    String password;
    String email;

    public User() {
    }

    public User(String fname, String lname, String password, String email) {
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

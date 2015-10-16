package com.example.bremme.eva_projectg6;

/**
 * Created by BREMME on 9/10/15.
 */
public class User {
    private String firstname;
    private String lastname;
    private String email;
    private String gebDatum;
    private Gender gender;
    private Status status;
    private String password;
    private String username;


    public User(String firstname, String lastname,String email,String gebDatum ,Gender gender,Status status, String password,String username ) {
        setFirstname(firstname);
        setEmail(email);
        setGender(gender);
        setGebDatum(gebDatum);
        setLastname(lastname);
        setPassword(password);
        setStatus(status);
        setUsername(username);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(String gebDatum) {
        this.gebDatum = gebDatum;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString(){
        return firstname+" "+lastname+" "+email+" "+gebDatum+" "+gender+" "+status+" " + password+" "+username;
    }
}

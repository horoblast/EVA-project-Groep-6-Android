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


    public User(String firstname, String lastname,String email,String gebDatum ,Gender gender,Status status, String password ) {
        this.firstname = firstname;
        this.email = email;
        this.gender = gender;
        this.gebDatum = gebDatum;
        this.lastname = lastname;
        this.password = password;
        this.status = status;
    }
}

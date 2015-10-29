package com.example.bremme.eva_projectg6.domein;

import java.util.List;

/**
 * Created by BREMME on 9/10/15.
 */
public class User {
    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String gebDatum;
    private Gender gender;
    private Difficulty dif;
    private String password;
    private String username;
    private boolean isStudent;
    private boolean hasChilderen;
    private boolean isDoingChallenges;
    private List<Challenge> challenges;

    public User(String firstname, String lastname,String email,String gebDatum ,Gender gender,Difficulty difficulty, String password,String username,boolean isDoingChallenges,boolean student,boolean hasChilderen ) {
        setFirstname(firstname);
        setEmail(email);
        setGender(gender);
        setGebDatum(gebDatum);
        setLastname(lastname);
        setPassword(password);
        setDif(difficulty);
        setUsername(username);
        setIsDoingChallenges(isDoingChallenges);
        setIsStudent(student);
        setHasChilderen(hasChilderen);
    }

    public User(String firstname, String lastname,String email,String gebDatum ,Gender gender,Difficulty difficulty, String password,String username,boolean isDoingChallenges,boolean student,boolean hasChilderen , List<Challenge> challenges) {
       this(firstname, lastname, email, gebDatum, gender, difficulty, password, username, isDoingChallenges, student, hasChilderen);
        setChallenges(challenges);
    }
    public User(){}

    public User(String username, String lastname,Gender gender, boolean hasChilderen, boolean isDoingChallenges, boolean isStudent, Difficulty dif, String email, String firstname, String gebDatum) {
        setFirstname(firstname);
        setEmail(email);
        setGender(gender);
        setGebDatum(gebDatum);
        setLastname(lastname);
        setPassword(password);
        setDif(dif);
        setUsername(username);
        setIsDoingChallenges(isDoingChallenges);
        setIsStudent(isStudent);
        setHasChilderen(hasChilderen);
    }

    public boolean isHasChilderen() {
        return hasChilderen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isDoingChallenges() {
        return isDoingChallenges;
    }

    public void setIsDoingChallenges(boolean isDoingChallenges) {
        this.isDoingChallenges = isDoingChallenges;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString(){
        return firstname+" "+lastname+" "+email+" "+gebDatum+" "+gender+" "+dif+" " + password+" "+username +" student: " + isStudent+" challenges: "+ isDoingChallenges+" hasChildren: "+hasChilderen;
    }

    public Difficulty getDif() {
        return dif;
    }

    public void setDif(Difficulty dif) {
        this.dif = dif;
    }

    public boolean HasChilderen() {
        return hasChilderen;
    }

    public void setHasChilderen(boolean hasChilderen) {
        this.hasChilderen = hasChilderen;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    public List<Challenge> getChallenges(){
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges){
        this.challenges = challenges;
    }
}

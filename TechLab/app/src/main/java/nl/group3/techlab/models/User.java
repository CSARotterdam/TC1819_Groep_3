package nl.group3.techlab.models;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class User implements Serializable {

    int id;
    int role;
    String studentNumber;
    String firstName;
    String lastName;
    String email;
    UUID apiKey;
    int broken;

    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public int getId() {
        return id;
    }

    public int getRole() {
        return role;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UUID getApiKey() {
        return apiKey;
    }

    public int getBroken(){ return broken; }

    public void addBroken(){ this.broken += 1; }
}

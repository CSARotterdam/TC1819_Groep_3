package nl.group3.techlab.models;

import java.io.Serializable;

public class Writer implements Serializable{
    private int id;
    private String name;

    public Writer(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

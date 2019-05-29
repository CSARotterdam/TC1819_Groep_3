package nl.group3.techlab.models;

import java.net.URL;

import nl.group3.techlab.models.Item;

public class Book extends Item {

    private String title;
    private String[] writers;
    private String ISBN;
    private String publisher;
    private int stock;

    public Book(String type, String id, String description, int borrowDays, URL imageUrl,
                String title, String[] writers, String ISBN, String publisher, int stock){
        super(type, id, description, borrowDays, imageUrl);
        this.title = title;
        this.writers = writers;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.stock = stock;
    }
}

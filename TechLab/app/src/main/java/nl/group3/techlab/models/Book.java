package nl.group3.techlab.models;

import java.io.Serializable;
import java.net.URL;

import nl.group3.techlab.models.Item;

public class Book extends Item implements Serializable {

    private String title;
    private Writer[] writers;
    private String ISBN;
    private String publisher;
    private int stock;

    public Book(String type, String id, String description, int borrowDays, URL imageUrl,
                String title, Writer[] writers, String ISBN, String publisher, int stock){
        super(type, id, description, borrowDays, imageUrl);
        this.title = title;
        this.writers = writers;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public Writer[] getWriters() {
        return writers;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getStock() {
        return stock;
    }
}

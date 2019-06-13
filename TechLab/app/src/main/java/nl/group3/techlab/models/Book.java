package nl.group3.techlab.models;

import java.io.Serializable;
import java.net.URL;

import nl.group3.techlab.models.Item;

public class Book extends Item implements Serializable {

    private Writer[] writers;
    private String ISBN;
    private String publisher;

    public Book(String type, String id, String description, int borrowDays, URL imageUrl,
                String title, Writer[] writers, String ISBN, String publisher, int stock, int broken){
        super(type, id, title,description, borrowDays, imageUrl, stock, broken);
        this.writers = writers;
        this.ISBN = ISBN;
        this.publisher = publisher;
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

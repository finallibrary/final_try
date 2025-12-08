package model;

import java.time.LocalDate;
import java.util.List;

public class CD implements Media {
    private final String title;
    private final String artist;
    private boolean borrowed = false;
    private LocalDate dueDate = null;
    private User borrower = null;

    private String id;
    private double fine;

    private int quantity;   // ← الكمية الجديدة

    public CD(String title, String artist, String id, int quantity) {
        this.title = title;
        this.artist = artist;
        this.id = id;
        this.borrowed = false;
        this.fine = 0;
        this.quantity = quantity;   // ← تخزين الكمية
    }
    
   


    public String getTitle() { return title; }
    public String getAuthor() { return artist; }
    public String getId() { return id; }

    public boolean isBorrowed() { return borrowed; }
    public LocalDate getDueDate() { return dueDate; }
    public User getBorrower() { return borrower; }

    public int getQuantity() { return quantity; }             // ← getter
    public void setQuantity(int quantity) { this.quantity = quantity; } // ← setter
    @Override
    public String getIsbn() { return id; }
    @Override
    public void borrow(User user) {
        if (quantity <= 0) {
            throw new IllegalStateException("CD is out of stock!");
        }

        quantity--;     // ← نقص الكمية

        borrowed = true;
        borrower = user;
        dueDate = LocalDate.now().plusDays(7);
    }

    @Override
    public void returnMedia() {
        returnCD();
    }

    public void returnCD() {
        quantity++;     // ← رجّع الكمية

        borrowed = false;
        borrower = null;
        dueDate = null;
    }

    @Override
    public boolean isOverdue() {
        return borrowed && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public long getDaysOverdue() {
        if (isOverdue()) {
            return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }

    @Override
    public double getFinePerDay() {
        return 20.0;
    }

    @Override
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "CD: " + title + ", Artist: " + artist + ", Quantity: " + quantity;
    }
}

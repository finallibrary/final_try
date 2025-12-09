package model;

import java.time.LocalDate;

public class Book implements Media {

    private final String title;
    private final String author;
    private final String isbn;
    private int quantity;

    private boolean borrowed = false;
    private LocalDate dueDate = null;
    private User borrower = null;

    public Book(String title, String author, String isbn, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
    }

    public String getTitle() { 
        return title; 
    }

    public String getAuthor() { 
        return author; 
    }

    @Override
    public String getIsbn() { 
        return isbn; 
    }

    public int getQuantity() { 
        return quantity; 
    }

    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }

    public boolean isBorrowed() { 
        return borrowed; 
    }

    public LocalDate getDueDate() { 
        return dueDate; 
    }

    public User getBorrower() { 
        return borrower; 
    }

    @Override
    public void borrow(User user) {
        if (quantity <= 0) {
            throw new IllegalStateException("Book is out of stock!");
        }

        quantity--;
        borrowed = true;
        borrower = user;
        dueDate = LocalDate.now().plusDays(28);
    }

    public void returnBook() {
        quantity++;
        borrowed = false;
        borrower = null;
        dueDate = null;
    }

    public boolean isOverdue() {
        return borrowed && LocalDate.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (isOverdue()) {
            return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }

    @Override
    public double getFinePerDay() {
        return 10.0;
    }

    @Override
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public void returnMedia() {
        returnBook();
    }

    @Override
    public String toString() {
        return "Title: " + title
             + ", Author: " + author
             + ", ISBN: " + isbn
             + ", Quantity: " + quantity;
    }
}

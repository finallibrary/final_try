package model;

import java.time.LocalDate;
import java.util.List;

public interface Media {
    String getTitle();
    String getIsbn();
    String getAuthor();
    int getQuantity();
    boolean isBorrowed();
    void borrow(User user);
    void returnMedia();
    boolean isOverdue();
    long getDaysOverdue();
    User getBorrower();
    double getFinePerDay();
  
    void setDueDate(LocalDate dueDate);
    LocalDate getDueDate();
}

package service;

import model.User;
import model.Book;
import model.Loan;

public class UserService {

    public void addFine(User user, double amount) {
        user.addFine(amount);
    }

    public void payFine(User user, double amount) {
        user.payFine(amount);
    }

    
    public boolean canBorrow(User user, Book book) {
        if (book.getQuantity() <= 0) {
            return false; // لا توجد نسخ متاحة
        }
        if (user.getFineBalance() > 0) {
            return false; // يوجد غرامة
        }
        for (Loan l : user.getLoans()) {
            if (l.isOverdue()) return false; // يوجد كتب متأخرة
        }
        return true; // يمكن الاستعارة
    }


    public String getBorrowStatus(User user, Book book) {
    if (book.getQuantity() <= 0) {
        return "You cannot borrow a new book because all copies are borrowed or overdue.";
    }
    if (user.getFineBalance() > 0) {
        return "You cannot borrow a new book because you have unpaid fines.";
    }
    for (Loan l : user.getLoans()) {
        if (l.isOverdue()) {
            return "You cannot borrow a new book because you have overdue books.";
        }
    }
    return "You can borrow books.";
}

    
}

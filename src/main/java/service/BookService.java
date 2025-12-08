package service;

import model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BookService extends Observable {

    private final List<Book> books = new ArrayList<>();
    private SearchStrategy<Book> searchStrategy;


    public void addBook(String title, String author, String isbn, int quantity) {
        boolean exists = books.stream().anyMatch(b -> b.getIsbn().equals(isbn));
        if (exists) return;  // لو الكتاب موجود، ما نضيفه
        books.add(new Book(title, author, isbn, quantity)); // ← استخدم الكمية
    }

    public void addBook(Book book) {
        boolean exists = books.stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()));
        if (!exists) {
            books.add(book);
        }
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void setSearchStrategy(SearchStrategy strategy) {
        this.searchStrategy = strategy;
    }

    public List<Book> search(String query) {
        if (searchStrategy == null) return new ArrayList<>();
        return searchStrategy.search(books, query);
    }

    public boolean borrowBook(Book book, model.User user) {
        if (!user.canBorrow()) return false;
        if (book.getQuantity() > 0) {          // ← تحقق من الكمية المتاحة
            book.borrow(user);                  // يقلل الكمية داخليًا
            return true;
        }
        return false;                           // لا يوجد نسخ متاحة للاستعارة
    }

    public void returnBook(Book book, model.User user) {
        if (book.getQuantity() < 1 || book.getBorrower() == user) {  
            // زيادة الكمية عند الإرجاع
            if (book.isOverdue()) user.addFine(5.0);
            book.returnBook();
        }
    }


    public void checkOverdueBooks() {
        for (Book book : books) {
            if (book.isBorrowed() && book.isOverdue()) {
                setChanged();
                notifyObservers(book);
            }
        }
    }
}

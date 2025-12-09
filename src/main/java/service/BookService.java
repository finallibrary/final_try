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
        if (exists) {
            return;
        }
        books.add(new Book(title, author, isbn, quantity));
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
        if (searchStrategy == null) {
            return new ArrayList<>();
        }
        return searchStrategy.search(books, query);
    }

    public boolean borrowBook(Book book, model.User user) {
        if (!user.canBorrow()) {
            return false;
        }

        if (book.getQuantity() > 0) {
            book.borrow(user);
            return true;
        }

        return false;
    }

    public void returnBook(Book book, model.User user) {
        boolean canReturn = book.getQuantity() < 1 || book.getBorrower() == user;

        if (canReturn) {
            if (book.isOverdue()) {
                user.addFine(5.0);
            }
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

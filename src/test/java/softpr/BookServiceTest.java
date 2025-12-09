package softpr;

import model.Book;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    public void setup() {
        bookService = new BookService();
    }

    @Test
    public void borrowBookQuantityMoreThanOneKeepsAvailable() {
        Book book = new Book("Java Basics", "James Gosling", "001", 3);
        User user = new User("Noor", "noorfayek321@gmail.com");

        boolean result = bookService.borrowBook(book, user);

        assertTrue(result);
        assertEquals(2, book.getQuantity());
        assertTrue(book.isBorrowed());
    }

    @Test
    public void borrowBookQuantityOneBecomesNotAvailable() {
        Book book = new Book("Python Basics", "Author B", "002", 1);
        User user = new User("Noor", "noor@gmail.com");

        boolean result = bookService.borrowBook(book, user);

        assertTrue(result);
        assertEquals(0, book.getQuantity());
        assertTrue(book.isBorrowed());

        User anotherUser = new User("Ali", "ali@gmail.com");

        assertFalse(bookService.borrowBook(book, anotherUser));
    }

    @Test
    public void returnBookIncreasesQuantity() {
        Book book = new Book("C++ Basics", "Author C", "003", 2);
        User user = new User("Noor", "noor@gmail.com");

        bookService.borrowBook(book, user);

        assertEquals(1, book.getQuantity());

        bookService.returnBook(book, user);

        assertEquals(2, book.getQuantity());
        assertFalse(book.isBorrowed());
    }

    @Test
    public void borrowFailsIfUserCannotBorrow() {
        Book book = new Book("Data Structures", "Mark Allen", "004", 2);

        User restrictedUser = new User("Noor", "noor@gmail.com") {
            @Override
            public boolean canBorrow() {
                return false;
            }
        };

        boolean result = bookService.borrowBook(book, restrictedUser);

        assertFalse(result);
        assertEquals(2, book.getQuantity());
        assertFalse(book.isBorrowed());
    }

    @Test
    public void borrowBookFailsWhenQuantityZero() {
        Book book = new Book("Java", "Author", "005", 1);
        User user = new User("Noor", "noor@gmail.com");

        bookService.borrowBook(book, user);

        User anotherUser = new User("Ali", "ali@gmail.com");

        assertFalse(bookService.borrowBook(book, anotherUser));
        assertEquals(0, book.getQuantity());
    }

    @Test
    public void addBookIncreasesList() {
        bookService.addBook("Java Basics", "James Gosling", "001", 2);

        List<Book> books = bookService.getAllBooks();
        assertEquals(1, books.size());
    }

    @Test
    public void duplicateISBNShouldNotAddBook() {
        bookService.addBook("Java", "A", "001", 2);

        int before = bookService.getAllBooks().size();

        bookService.addBook("Python", "B", "001", 2);

        assertEquals(before, bookService.getAllBooks().size());
    }

    @Test
    void bookGetFinePerDayShouldReturnCorrectValue() {
        Book book = new Book("Data Structures", "Mark Allen", "002", 1);

        assertEquals(10.0, book.getFinePerDay());
    }

    @Test
    void bookSetDueDateShouldChangeDueDate() {
        Book book = new Book("Data Structures", "Mark Allen", "002", 1);

        LocalDate newDate = LocalDate.now().plusDays(5);

        book.setDueDate(newDate);

        assertEquals(newDate, book.getDueDate());
    }

    @Test
    void testGetDaysOverdueDirectly() throws Exception {
        User user = new User("Noor", "noor@gmail.com");
        Book book = new Book("Java", "Author", "006", 1);

        book.borrow(user);

        var field = Book.class.getDeclaredField("dueDate");
        field.setAccessible(true);
        field.set(book, LocalDate.now().minusDays(3));

        assertEquals(3, book.getDaysOverdue());
    }

    @Test
    public void returnBookAddsFineIfOverdue() {
        User user = new User("Noor", "noor@gmail.com");

        Book book = new Book("Java", "Author", "004", 1) {
            @Override
            public boolean isOverdue() {
                return true;
            }
        };

        book.borrow(user);

        bookService.returnBook(book, user);

        assertEquals(5.0, user.getFineBalance());
        assertFalse(book.isBorrowed());
    }

    @Test
    void testGetBorrowerAndDueDate() {
        User user = new User("Noor", "noor@gmail.com");

        Book book = new Book("Java", "Author", "001", 2);

        book.borrow(user);

        assertEquals(user, book.getBorrower());
        assertNotNull(book.getDueDate());
    }

    @Test
    void testToString() {
        Book book = new Book("Java", "Author", "003", 2);

        String str = book.toString();

        assertTrue(str.contains("Java"));
        assertTrue(str.contains("Author"));
        assertTrue(str.contains("003"));
    }

    @Test
    void searchWithNoStrategyReturnsEmpty() {
        BookService service = new BookService();

        List<Book> result = service.search("Java");

        assertTrue(result.isEmpty());
    }

    @Test
    void searchWithStrategyFindsBooks() {
        BookService service = new BookService();

        service.addBook("Java Basics", "Author A", "001", 1);

        SearchStrategy<Book> strategy = new SearchStrategy<>() {
            @Override
            public List<Book> search(List<Book> list, String query) {
                List<Book> result = new ArrayList<>();
                String q = query.toLowerCase();

                for (Book b : list) {
                    if (b.getTitle().toLowerCase().contains(q)) {
                        result.add(b);
                    }
                }

                return result;
            }
        };

        service.setSearchStrategy(strategy);

        List<Book> result = service.search("Java");

        assertEquals(1, result.size());
    }

    @Test
    void searchWithStrategyNoMatches() {
        BookService service = new BookService();

        service.addBook("Python Basics", "Author B", "002", 1);

        service.setSearchStrategy(new SearchByTitle<Book>());

        List<Book> result = service.search("Java");

        assertTrue(result.isEmpty());
    }

    @Test
    public void checkOverdueBooksNotifiesObserver() {
        BookService service = new BookService();
        User user = new User("Noor", "noorfayek321@gmail.com");

        Book book = new Book("Java", "Author", "005", 1) {
            @Override
            public boolean isOverdue() {
                return true;
            }
        };

        book.borrow(user);

        service.addBook(book);

        Observer observer = mock(Observer.class);

        service.addObserver(observer);

        service.checkOverdueBooks();

        verify(observer, times(1)).update(eq(service), any(Book.class));
    }
}

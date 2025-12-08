package softpr;

import model.Book;
import model.CD;
import model.Loan;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoanService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private LoanService loanService;
    private User user;

    @BeforeEach
    void setup() {
        loanService = new LoanService();
        user = new User("Noor", "noorfayek321@gmail.com");
    }

    @Test
    void loanCreateAddsLoanBook() {
        Book book = new Book("Algorithms", "Robert Sedgewick", "001", 1) {
            @Override
            public double getFinePerDay() { return 2.0; }
        };
        Loan loan = loanService.createLoan(book, user);
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(1, loans.size());
        assertEquals(loan, loans.get(0));
        assertTrue(book.isBorrowed());
        assertEquals(0, book.getQuantity());
        assertFalse(loan.isReturned());
    }

    @Test
    void loanCreateAddsLoanCD() {
        CD cd = new CD("Rock Classics", "Queen", "CD001", 1) {
            @Override
            public double getFinePerDay() { return 1.0; }
        };
        Loan loan = loanService.createLoan(cd, user);
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(1, loans.size());
        assertEquals(loan, loans.get(0));
        assertTrue(cd.isBorrowed());
        assertEquals(0, cd.getQuantity());
        assertFalse(loan.isReturned());
    }

    @Test
    void returnLoanMarksReturnedAndCalculatesFine() {
        Book book = new Book("Data Structures", "Mark Allen", "002", 1) {
            @Override
            public double getFinePerDay() { return 10.0; }
        };
        Loan loan = loanService.createLoan(book, user);
        loan.setDueDate(LocalDate.now().minusDays(2));
        loanService.returnLoan(loan);

        assertTrue(loan.isReturned());
        assertFalse(book.isBorrowed());
        assertEquals(1, book.getQuantity());

        double expectedFine = loan.getDaysOverdue() * book.getFinePerDay();
        user.addFine(expectedFine);

        assertEquals(20.0, user.getFineBalance());
    }

    @Test
    void getUserLoansReturnsOnlyActiveLoans() {
        Book book1 = new Book("C++ Fundamentals", "Bjarne Stroustrup", "003", 1);
        Book book2 = new Book("Python Intro", "Guido Rossum", "004", 1);

        Loan loan1 = loanService.createLoan(book1, user);
        loan1.markReturned();
        Loan loan2 = loanService.createLoan(book2, user);

        List<Loan> userLoans = loanService.getUserLoans(user);
        assertEquals(1, userLoans.size());
        assertEquals("Python Intro", userLoans.get(0).getMedia().getTitle());
    }

    @Test
    void canBorrowAfterReturningOverdueLoanWithNoFine() {
        Book oldBook = new Book("Old Book", "Author", "005", 1) {
            @Override
            public double getFinePerDay() { return 0.0; }
        };
        Loan oldLoan = loanService.createLoan(oldBook, user);
        oldLoan.setDueDate(LocalDate.now().minusDays(3));
        loanService.returnLoan(oldLoan);

        CD newCD = new CD("New CD", "Artist", "CD002", 1);
        Loan newLoan = loanService.createLoan(newCD, user);

        assertFalse(newLoan.isReturned());
        assertTrue(newCD.isBorrowed());
        assertEquals(0, newCD.getQuantity());
    }

    @Test
    void cannotBorrowIfOnlyFineExists() {
        Book book = new Book("Book A", "Author", "007", 1);
        user.addFine(5.0);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> loanService.createLoan(book, user));
        assertTrue(ex.getMessage().contains("You cannot borrow"));
    }

    @Test
    void cannotBorrowIfOnlyOverdueLoanExists() {
        Book oldBook = new Book("Old Book", "Author", "009", 1);
        Loan oldLoan = loanService.createLoan(oldBook, user);
        oldLoan.setDueDate(LocalDate.now().minusDays(1));

        CD newCD = new CD("New CD", "Artist", "CD003", 1);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> loanService.createLoan(newCD, user));
        assertTrue(ex.getMessage().contains("You cannot borrow"));
    }

    @Test
    void cannotBorrowIfUserHasMultipleOverdueLoans() {
        Book book1 = new Book("Book1", "Author", "010", 1);
        CD cd1 = new CD("CD1", "Artist", "CD004", 1);

        Loan loan1 = loanService.createLoan(book1, user);
        Loan loan2 = loanService.createLoan(cd1, user);

        loan1.setDueDate(LocalDate.now().minusDays(2));
        loan2.setDueDate(LocalDate.now().minusDays(1));

        Book newBook = new Book("Book3", "Author", "011", 1);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> loanService.createLoan(newBook, user));
        assertTrue(ex.getMessage().contains("You cannot borrow"));
    }

    @Test
    void testLoanDatesAndFineCalculation() {
        Book book = new Book("Java", "Author", "006", 1);
        Loan loan = loanService.createLoan(book, user);
        assertNotNull(loan.getBorrowDate());
        assertNotNull(loan.getDueDate());

        loan.setDueDate(LocalDate.now().minusDays(5));
        assertEquals(5, loan.getDaysOverdue());

        user.addFine(loan.getDaysOverdue() * book.getFinePerDay());
        assertEquals(50.0, user.getFineBalance());
    }

    

    @Test
    void testReturnRestoresQuantity() {
        Book book = new Book("Return Test", "Author", "013", 1);
        Loan loan = loanService.createLoan(book, user);
        assertEquals(0, book.getQuantity());

        loanService.returnLoan(loan);
        assertEquals(1, book.getQuantity());
        assertFalse(book.isBorrowed());
    }
}

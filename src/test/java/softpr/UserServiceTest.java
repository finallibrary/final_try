package softpr;

import model.Book;
import model.Loan;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private User user;
    private UserService userService;

    @BeforeEach
    public void setup() {
        user = new User("Noor", "noorfayek321@gmail.com");
        userService = new UserService();
    }

    @Test
    void validEmailShouldCreateUser() {
        User u = new User("Hala", "hala123@gmail.com");
        assertEquals("Hala", u.getName());
        assertEquals("hala123@gmail.com", u.getEmail());
    }

    @Test
    void emailWithoutAtShouldThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new User("Hala", "hala.gmail.com")
        );
        assertEquals("Invalid email format: must contain @", ex.getMessage());
    }

    @Test
    void nullEmailShouldThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new User("Hala", null)
        );
        assertEquals("Invalid email format: must contain @", ex.getMessage());
    }

    @Test
    void emptyEmailShouldThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new User("Hala", "")
        );
        assertEquals("Invalid email format: must contain @", ex.getMessage());
    }

    @Test
    void emailWithWeirdAtStillValidAccordingToCurrentLogic() {
        User u = new User("Hala", "@gmail.com"); // بما إن الشرط فقط contains("@")
        assertEquals("@gmail.com", u.getEmail());
    }

    // ================================
    //       USER SERVICE TESTS
    // ================================

    @Test
    void addFineShouldIncreaseUserBalance() {
        assertEquals(0, user.getFineBalance());
        userService.addFine(user, 15.0);
        assertEquals(15.0, user.getFineBalance());
    }

    @Test
    public void userCanBorrowWhenNoFineAndNoOverdue() {
        Book book = new Book("Some Book", "Author", "001", 1);
        assertTrue(userService.canBorrow(user, book));
        assertEquals("You can borrow books.", userService.getBorrowStatus(user, book));
    }

    @Test
    public void userCannotBorrowWithFine() {
        Book book = new Book("Some Book", "Author", "001", 1);
        user.addFine(10);
        assertFalse(userService.canBorrow(user, book));
        assertEquals("You cannot borrow a new book because you have unpaid fines.",
                userService.getBorrowStatus(user, book));
    }

    @Test
    public void payingFineReducesBalance() {
        user.addFine(10);
        userService.payFine(user, 5);
        assertEquals(5, user.getFineBalance());
    }

    @Test
    public void overpayingFineResetsToZero() {
        user.addFine(10);
        userService.payFine(user, 20);
        assertEquals(0, user.getFineBalance());
    }

    @Test
    void testGetNameAndEmail() {
        assertEquals("Noor", user.getName());
        assertEquals("noorfayek321@gmail.com", user.getEmail());
    }
    @Test
    void getOverdueLoansReturnsEmptyListWhenNoLoans() {
        assertTrue(user.getOverdueLoans().isEmpty());
    }

    @Test
    void getOverdueLoansReturnsEmptyListWhenNoLoanIsOverdue() {
        Book book = new Book("A", "B", "001", 1);

        Loan loan = new Loan(book, user); // يستخدم constructor تبعك
        loan.setDueDate(LocalDate.now().plusDays(5)); // نخليه مش overdue

        assertTrue(user.getOverdueLoans().isEmpty());
    }

    @Test
    void getOverdueLoansReturnsOnlyOverdueLoans() {
        Book b1 = new Book("Book1", "A", "001", 1);
        Book b2 = new Book("Book2", "B", "002", 1);

        Loan overdueLoan = new Loan(b1, user);
        overdueLoan.setDueDate(LocalDate.now().minusDays(5)); // overdue

        Loan okLoan = new Loan(b2, user);
        okLoan.setDueDate(LocalDate.now().plusDays(3)); // مش overdue

        List<Loan> result = user.getOverdueLoans();

        assertEquals(1, result.size());
        assertTrue(result.contains(overdueLoan));
        assertFalse(result.contains(okLoan));
    }

    @Test
    void getOverdueLoansReturnsMultipleOverdueLoans() {
        Book b1 = new Book("Book1", "A", "001", 1);
        Book b2 = new Book("Book2", "B", "002", 1);

        Loan overdue1 = new Loan(b1, user);
        overdue1.setDueDate(LocalDate.now().minusDays(10));

        Loan overdue2 = new Loan(b2, user);
        overdue2.setDueDate(LocalDate.now().minusDays(7));

        List<Loan> result = user.getOverdueLoans();

        assertEquals(2, result.size());
        assertTrue(result.contains(overdue1));
        assertTrue(result.contains(overdue2));
    }

}

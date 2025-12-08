package softpr;

import model.Book;
import model.Loan;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.time.LocalDate;

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
    void addFineShouldIncreaseUserBalance() {
        assertEquals(0, user.getFineBalance());
        userService.addFine(user, 15.0);
        assertEquals(15.0, user.getFineBalance());
    }

    @Test
    public void userCanBorrowWhenNoFineAndNoOverdue() {
        Book book = new Book("Some Book", "Author", "001", 1);
        assertTrue(userService.canBorrow(user, book));
        assertEquals("You can borrow books.", userService.getBorrowStatus(user,book));
    }

    @Test
    public void userCannotBorrowWithFine() {
        Book book = new Book("Some Book", "Author", "001", 1);
        user.addFine(10);
        assertFalse(userService.canBorrow(user, book));
        assertEquals("You cannot borrow a new book because you have unpaid fines.",
                     userService.getBorrowStatus(user,book));
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
}

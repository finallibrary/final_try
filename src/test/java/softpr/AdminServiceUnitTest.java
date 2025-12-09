package softpr;

import model.Admin;
import model.User;
import model.Book;
import model.Loan;
import service.AdminService;
import service.LoanService;
import exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceUnitTest {

    private Admin admin;
    private AdminService adminService;
    private User user;
    private LoanService loanService;

    @BeforeEach
    public void setup() {
        admin = new Admin("admin", "1234");
        adminService = new AdminService(admin);
        adminService.login("admin", "1234");

        user = new User("hala", "hala@gmail.com");

        AdminService.getAllUsers().clear();
        adminService.addUser(user);

        loanService = new LoanService();
    }

    @Test
    public void validLoginShouldSetIsLoggedInTrue() {
        AdminService service = new AdminService(admin);
        service.login("admin", "1234");
        assertTrue(service.isLoggedIn());
    }

    @Test
    public void invalidLoginThrowsExceptionAndIsLoggedInFalse() {
        AdminService service = new AdminService(admin);
        assertThrows(AuthenticationException.class, () -> service.login("admin", "wrong"));
        assertFalse(service.isLoggedIn());
    }

    @Test
    public void isLoggedInReflectsLogout() {
        AdminService service = new AdminService(admin);
        service.login("admin", "1234");
        assertTrue(service.isLoggedIn());
        service.logout();
        assertFalse(service.isLoggedIn());
    }

    @Test
    public void logoutWhenNotLoggedInDoesNotChangeIsLoggedIn() {
        AdminService service = new AdminService(admin);
        assertFalse(service.isLoggedIn());
        service.logout();
        assertFalse(service.isLoggedIn());
    }

    @Test
    public void adminCanUnregisterUserWithNoLoansOrFines() {
        assertTrue(AdminService.getAllUsers().contains(user));
        adminService.unregisterUser(user);
        assertFalse(AdminService.getAllUsers().contains(user));
    }

    @Test
    public void cannotUnregisterUserWithActiveLoan() {
        Book book = new Book("Clean Code", "Robert Martin", "123", 1);
        Loan loan = loanService.createLoan(book, user);

        assertNotNull(loan);
        assertTrue(AdminService.getAllUsers().contains(user));

        adminService.unregisterUser(user);

        assertTrue(AdminService.getAllUsers().contains(user));
    }

    @Test
    public void cannotUnregisterUserWithUnpaidFine() {
        user.addFine(5.0);

        assertTrue(AdminService.getAllUsers().contains(user));
        adminService.unregisterUser(user);
        assertTrue(AdminService.getAllUsers().contains(user));
    }

    @Test
    public void nonLoggedInAdminCannotUnregister() {
        adminService.logout();

        assertTrue(AdminService.getAllUsers().contains(user));
        adminService.unregisterUser(user);
        assertTrue(AdminService.getAllUsers().contains(user));
    }
}

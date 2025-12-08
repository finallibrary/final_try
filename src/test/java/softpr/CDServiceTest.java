package softpr;

import model.CD;
import model.User;
import service.CaseInsensitiveCDSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CDService;

import java.time.LocalDate;
import java.util.List;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CDServiceTest {

    private CDService cdService;
    private User user;

    @BeforeEach
    void setUp() {
        cdService = new CDService();
        cdService.setSearchStrategy(new CaseInsensitiveCDSearch());

        user = new User("Noor", "noorfayek321@gmail.com");
    }

    @Test
    void testAddCDWithQuantity() {
        cdService.addCD("Rock", "Queen", "CD001", 3);
        List<CD> cds = cdService.getAllCDs();
        assertEquals(1, cds.size());
        assertEquals(3, cds.get(0).getQuantity());
    }

    @Test
    void testBorrowCDReducesQuantity() {
        cdService.addCD("Rock", "Queen", "CD001", 2);
        CD cd = cdService.getAllCDs().get(0);

        boolean borrowed = cdService.borrowCD(cd, user);
        assertTrue(borrowed);
        assertEquals(1, cd.getQuantity());
        assertTrue(cd.isBorrowed());
        assertEquals(user, cd.getBorrower());
    }

    @Test
    void testBorrowCDWhenQuantityIsOne() {
        cdService.addCD("Rock", "Queen", "CD001", 1);
        CD cd = cdService.getAllCDs().get(0);

        assertTrue(cdService.borrowCD(cd, user));
        assertEquals(0, cd.getQuantity());
        assertTrue(cd.isBorrowed());
        assertEquals(user, cd.getBorrower());

        User anotherUser = new User("Ali", "ali@example.com");
        assertFalse(cdService.borrowCD(cd, anotherUser)); // لا يمكن استعارة نسخة أخرى
    }

    @Test
    void testReturnCDIncreasesQuantity() {
        cdService.addCD("Rock", "Queen", "CD001", 1);
        CD cd = cdService.getAllCDs().get(0);

        cdService.borrowCD(cd, user);
        assertEquals(0, cd.getQuantity());

        cdService.returnCD(cd, user);
        assertEquals(1, cd.getQuantity());
        assertFalse(cd.isBorrowed());
        assertNull(cd.getBorrower());
    }

    @Test
    void testSearchCD() {
        cdService.addCD("Rock", "Queen", "CD001", 1);
        cdService.addCD("Jazz Hits", "Miles", "CD002", 1);

        List<CD> results = cdService.search("rock");
        assertEquals(1, results.size());
        assertEquals("Rock", results.get(0).getTitle());

        results = cdService.search("Miles");
        assertEquals(1, results.size());
        assertEquals("Jazz Hits", results.get(0).getTitle());

        results = cdService.search("CD002");
        assertEquals(1, results.size());
        assertEquals("Jazz Hits", results.get(0).getTitle());

        results = cdService.search("none");
        assertTrue(results.isEmpty());
    }

    @Test
    void testBorrowCDWhenUserCannotBorrow() {
        cdService.addCD("Rock", "Queen", "CD001", 1);
        CD cd = cdService.getAllCDs().get(0);

        User mockUser = mock(User.class);
        when(mockUser.canBorrow()).thenReturn(false);

        assertFalse(cdService.borrowCD(cd, mockUser));
    }

    @Test
    void testReturnCDOverdueAddsFine() {
        cdService.addCD("Rock", "Queen", "CD001", 1);
        CD cd = cdService.getAllCDs().get(0);

        cdService.borrowCD(cd, user);
        cd.setDueDate(LocalDate.now().minusDays(2));

        double fineBefore = user.getFineBalance();
        cdService.returnCD(cd, user);

        assertFalse(cd.isBorrowed());
        assertTrue(user.getFineBalance() > fineBefore); // يجب أن يزيد الغرامة
    }

    @Test
    void testCheckOverdueCDsNotifiesObservers() {
        Observer mockObserver = mock(Observer.class);
        cdService.addObserver(mockObserver);

        cdService.addCD("Rock", "Queen", "CD001", 1);
        CD cd = cdService.getAllCDs().get(0);

        cdService.borrowCD(cd, user);
        cd.setDueDate(LocalDate.now().minusDays(1));

        cdService.checkOverdueCDs();
        verify(mockObserver, times(1)).update(eq(cdService), eq(cd));
    }

    @Test
    void testCheckOverdueCDsNoNotificationIfNotOverdue() {
        Observer mockObserver = mock(Observer.class);
        cdService.addObserver(mockObserver);

        cdService.addCD("Rock", "Queen", "CD001", 1);
        CD cd = cdService.getAllCDs().get(0);

        cdService.borrowCD(cd, user);

        cdService.checkOverdueCDs();
        verify(mockObserver, never()).update(any(), any());
    }
}

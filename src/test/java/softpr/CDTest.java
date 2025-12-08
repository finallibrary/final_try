package softpr;

import model.CD;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CDTest {

    private CD cd;
    private User user;

    @BeforeEach
    void setup() {
        cd = new CD("Rock Classics", "Queen", "CD002", 2); // ← الكمية الآن مطلوبة
        user = new User("Noor", "noorfayek321@gmail.com");
    }

    @Test
    void testCDGetters() {
        assertEquals("Rock Classics", cd.getTitle());
        assertEquals("Queen", cd.getAuthor());
        assertEquals(2, cd.getQuantity());
        assertFalse(cd.isBorrowed());
        assertNull(cd.getDueDate());
        assertNull(cd.getBorrower());
    }

    @Test
    void testBorrowReducesQuantity() {
        cd.borrow(user);
        assertEquals(1, cd.getQuantity());
        assertTrue(cd.isBorrowed());
        assertEquals(user, cd.getBorrower());
    }

    @Test
    void testBorrowWhenLastCopy() {
        cd = new CD("Rock Classics", "Queen", "CD002", 1);
        cd.borrow(user);
        assertEquals(0, cd.getQuantity());
        assertTrue(cd.isBorrowed());
    }

    @Test
    void testReturnIncreasesQuantity() {
        cd.borrow(user);
        int beforeReturn = cd.getQuantity();
        cd.returnMedia();
        assertEquals(beforeReturn + 1, cd.getQuantity());
        assertFalse(cd.isBorrowed());
        assertNull(cd.getBorrower());
        assertNull(cd.getDueDate());
    }

    @Test
    void testFinePerDay() {
        assertEquals(20.0, cd.getFinePerDay());
    }

    @Test
    void cdSetDueDateShouldChangeDueDate() {
        LocalDate newDate = LocalDate.now().plusDays(3);
        cd.setDueDate(newDate);
        assertEquals(newDate, cd.getDueDate());
    }

    @Test
    void testOverdueAndDaysOverdue() throws Exception {
        cd.borrow(user);
        java.lang.reflect.Field dueField = CD.class.getDeclaredField("dueDate");
        dueField.setAccessible(true);
        dueField.set(cd, LocalDate.now().minusDays(3));

        assertTrue(cd.isOverdue());
        assertEquals(3, cd.getDaysOverdue());
    }

    @Test
    void testToString() {
        String str = cd.toString();
        assertTrue(str.contains("CD: Rock Classics"));
        assertTrue(str.contains("Queen"));
        assertTrue(str.contains("Quantity: 2"));
    }
}

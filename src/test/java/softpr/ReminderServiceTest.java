package softpr;

import model.User;
import model.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.EmailService;
import service.ReminderService;

import java.util.List;

import static org.mockito.Mockito.*;

public class ReminderServiceTest {

    private EmailService emailService;
    private ReminderService reminderService;
    private User user;

    @BeforeEach
    public void setup() {
        emailService = mock(EmailService.class);
        reminderService = new ReminderService(emailService);
        user = new User("Noor", "noorfayek321@gmail.com");
    }

    @Test
    public void shouldSendEmailForOverdueBook() {
        Loan loan = mock(Loan.class);
        when(loan.isOverdue()).thenReturn(true);
        when(loan.isReturned()).thenReturn(false);
        when(loan.getUser()).thenReturn(user);

        reminderService.sendOverdueReminders(List.of(loan));

        verify(emailService, times(1))
                .sendEmail(eq(user), contains("1 overdue book"));
    }

    @Test
    public void shouldNotSendEmailIfNotOverdueOrReturned() {
        Loan loan = mock(Loan.class);
        when(loan.isOverdue()).thenReturn(false);
        when(loan.isReturned()).thenReturn(true);
        when(loan.getUser()).thenReturn(user);

        reminderService.sendOverdueReminders(List.of(loan));

        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    public void shouldGroupMultipleOverdueBooksInOneEmail() {
        Loan loan1 = mock(Loan.class);
        Loan loan2 = mock(Loan.class);

        when(loan1.isOverdue()).thenReturn(true);
        when(loan1.isReturned()).thenReturn(false);
        when(loan1.getUser()).thenReturn(user);

        when(loan2.isOverdue()).thenReturn(true);
        when(loan2.isReturned()).thenReturn(false);
        when(loan2.getUser()).thenReturn(user);

        reminderService.sendOverdueReminders(List.of(loan1, loan2));

        // نتأكد أن الإيميل واحد فقط يرسل ويحتوي على عدد 2
        verify(emailService, times(1))
                .sendEmail(eq(user), contains("2 overdue book"));
    }

    @Test
    public void shouldSendEmailForOverdueCD() {
        Loan loan = mock(Loan.class);
        when(loan.isOverdue()).thenReturn(true);
        when(loan.isReturned()).thenReturn(false);
        when(loan.getUser()).thenReturn(user);

        reminderService.sendOverdueReminders(List.of(loan));

        verify(emailService, times(1))
                .sendEmail(eq(user), contains("1 overdue book"));
    }
}

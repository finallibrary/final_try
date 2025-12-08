package service;

import model.Loan;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderService {

    private final EmailService emailService;

    public ReminderService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendOverdueReminders(List<Loan> loans) {

        // خريطة لتخزين عدد الكتب المتأخرة لكل مستخدم
        Map<User, Integer> overdueCount = new HashMap<>();

        for (Loan loan : loans) {
            // إذا الكتاب متأخر ولم يُرجع بعد
            if (!loan.isReturned() && loan.isOverdue()) {
                // زيادة العدّاد للمستخدم
                overdueCount.put(
                        loan.getUser(),
                        overdueCount.getOrDefault(loan.getUser(), 0) + 1
                );
            }
        }

        // إرسال إيميل واحد لكل مستخدم مع عدد الكتب المتأخرة
        for (Map.Entry<User, Integer> entry : overdueCount.entrySet()) {
            User user = entry.getKey();
            int count = entry.getValue();
            emailService.sendEmail(user, "You have " + count + " overdue book(s).");
        }
    }
}

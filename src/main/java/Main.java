import java.util.*;
import java.util.List;
import java.time.LocalDate;
import javax.swing.*;
import java.awt.*;
import io.github.cdimascio.dotenv.Dotenv;

import model.*;
import service.*;

public class Main {

    private static final int BORROW_DAYS = 28;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Book b1 = new Book("Java Basics", "James Gosling", "001", 1);
        Book b2 = new Book("Python Intro", "Guido Rossum", "002", 8);
        Book b3 = new Book("C++ Fundamentals", "Bjarne Stroustrup", "003", 5);
        Book b4 = new Book("Data Structures", "Mark Allen", "004", 1);
        Book b5 = new Book("Algorithms", "Robert Sedgewick", "005", 6);
        Book b6 = new Book("Machine Learning", "Tom Mitchell", "006", 3);
        List<Book> books = new ArrayList<>(Arrays.asList(b1, b2, b3, b4, b5, b6));

        CD cd1 = new CD("Classical Hits", "Beethoven", "CD001", 9);
        CD cd2 = new CD("Rock Classics", "Queen", "CD002", 4);
        CD cd3 = new CD("Jazz Essentials", "Miles Davis", "CD003", 2);
        CD cd4 = new CD("Pop Top", "Taylor Swift", "CD004", 7);
        List<CD> cds = new ArrayList<>(Arrays.asList(cd1, cd2, cd3, cd4));

        List<User> users = new ArrayList<>();
        User u1 = new User("Noor", "halawad257@gmail.com");
        User u2 = new User("Hala", "s12217844@stu.najah.edu");
        User u3 = new User("Hala", "halaawwad455@gmail.com");
        User u4 = new User("Sara", "sara@gmail.com");
        users.addAll(Arrays.asList(u1, u2, u3, u4));

        LoanService loanService = new LoanService();
        FineService fineService = new FineService();

        Dotenv dotenv = Dotenv.load();
        String emailUser = dotenv.get("EMAIL_USERNAME");
        String emailPass = dotenv.get("EMAIL_PASSWORD");
        String adminUser = dotenv.get("ADMIN_USERNAME");
        String adminPass = dotenv.get("ADMIN_PASSWORD");

        EmailService emailService = new EmailService(emailUser, emailPass);
        MediaEmailNotifier notifier = new MediaEmailNotifier(emailService);
        Admin admin = new Admin(adminUser, adminPass);

        Loan l1 = loanService.createLoan(b1, u1);
        Loan l2 = loanService.createLoan(cd1, u2);
        Loan l3 = loanService.createLoan(b2, u3);
        Loan l4 = loanService.createLoan(cd2, u3);
        Loan l5 = loanService.createLoan(cd3, u3);
        Loan l6 = loanService.createLoan(cd3, u2);

        try {
            var field = Loan.class.getDeclaredField("dueDate");
            field.setAccessible(true);
            field.set(l1, LocalDate.now().minusDays(29));
            field.set(l2, LocalDate.now().minusDays(8));
            field.set(l3, LocalDate.now().minusDays(30));
            field.set(l4, LocalDate.now().minusDays(8));
            field.set(l5, LocalDate.now().minusDays(8));
            field.set(l6, LocalDate.now().minusDays(8));
        } catch (Exception e) {}

        if (l1.isOverdue()) fineService.addFine(u1, l1.getFineAmount());
        if (l2.isOverdue()) fineService.addFine(u2, l2.getFineAmount());
        if (l3.isOverdue()) fineService.addFine(u3, l3.getFineAmount());
        if (l4.isOverdue()) fineService.addFine(u3, l4.getFineAmount());
        if (l5.isOverdue()) fineService.addFine(u3, l5.getFineAmount());
        if (l6.isOverdue()) fineService.addFine(u2, l6.getFineAmount());

        JFrame frame = new JFrame("Library Management System");
        frame.setSize(520, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        ImageIcon bg = new ImageIcon("C:\\Users\\JC\\eclipse-workspace\\final_try\\src\\test\\resources\\library.png");
        JLabel background = new JLabel(bg);
        background.setLayout(new GridBagLayout());
        frame.setContentPane(background);

        Font f = new Font("Arial", Font.BOLD, 20);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Color gray = new Color(60, 63, 65, 150);
        Color redish = new Color(120, 40, 40, 150);
        Color text = new Color(240, 240, 240);

        JButton adminBtn = new JButton("Login as Admin");
        adminBtn.setFont(f);
        adminBtn.setPreferredSize(new Dimension(250, 60));
        adminBtn.setForeground(text);
        adminBtn.setBackground(gray);
        adminBtn.setOpaque(true);
        adminBtn.setContentAreaFilled(true);
        adminBtn.setFocusPainted(false);
        adminBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200,120), 2));

        JButton userBtn = new JButton("Login as User");
        userBtn.setFont(f);
        userBtn.setPreferredSize(new Dimension(250, 60));
        userBtn.setForeground(text);
        userBtn.setBackground(gray);
        userBtn.setOpaque(true);
        userBtn.setContentAreaFilled(true);
        userBtn.setFocusPainted(false);
        userBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200,120), 2));

        JButton exitBtn = new JButton("Exit");
        exitBtn.setFont(f);
        exitBtn.setPreferredSize(new Dimension(250, 60));
        exitBtn.setForeground(text);
        exitBtn.setBackground(redish);
        exitBtn.setOpaque(true);
        exitBtn.setContentAreaFilled(true);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200,120), 2));

        gbc.gridy = 0; background.add(adminBtn, gbc);
        gbc.gridy = 1; background.add(userBtn, gbc);
        gbc.gridy = 2; background.add(exitBtn, gbc);

        adminBtn.addActionListener(e -> adminLogin(frame, admin, sc, books, cds, users, loanService, notifier));
        userBtn.addActionListener(e -> userLogin(frame, users, sc, books, cds, loanService));
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private static void adminLogin(JFrame frame, Admin admin, Scanner sc, List<Book> books, List<CD> cds,
                                   List<User> users, LoanService loanService, MediaEmailNotifier notifier) {
        JPanel panel = new JPanel(new GridLayout(3,2,10,15));
        panel.add(new JLabel("Username:")); JTextField userField = new JTextField();
        panel.add(userField);
        panel.add(new JLabel("Password:")); JPasswordField passField = new JPasswordField();
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (userField.getText().equals(admin.getUsername()) && 
                admin.checkPassword(new String(passField.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Welcome Admin!");
                adminMenu(sc, books, cds, users, loanService, notifier);
            } else {
                JOptionPane.showMessageDialog(frame, "Wrong username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void userLogin(JFrame frame, List<User> users, Scanner sc, List<Book> books, List<CD> cds, LoanService loanService) {
        JPanel panel = new JPanel(new GridLayout(3,2,10,15));
        panel.add(new JLabel("Name:")); JTextField nameField = new JTextField();
        panel.add(nameField);
        panel.add(new JLabel("Email:")); JTextField emailField = new JTextField();
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "User Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            User user = null;
            for (User u : users) {
                if (u.getName().equalsIgnoreCase(name) && u.getEmail().equalsIgnoreCase(email)) {
                    user = u;
                    break;
                }
            }

            if (user != null) {
                JOptionPane.showMessageDialog(frame, "Welcome, " + user.getName() + "!");
                userMenu(sc, user, books, cds, loanService);
            } else {
                JOptionPane.showMessageDialog(frame, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void adminMenu(Scanner sc, List<Book> books, List<CD> cds, List<User> users,
                                  LoanService loanService, MediaEmailNotifier notifier) {
        while (true) {
            ConsoleArt.clear();
            ConsoleArt.title("Admin Dashboard");

            String menu = """
                1 │ View All Media (Books & CDs)
                2 │ Add New Media
                3 │ Delete Media
                4 │ Search Media
                5 │ View Users & Fines
                6 │ Register New User
                7 │ Show Borrowed Media Count
                8 │ Send Overdue Reminders
                9 │ Unregister User
                0 │ Logout
                """;

            ConsoleArt.box(menu);
            System.out.print(ConsoleArt.CYAN + ConsoleArt.BOLD + "Enter choice: " + ConsoleArt.RESET);
            String choice = sc.nextLine().trim();

            if (choice.equals("0")) {
                ConsoleArt.success("Logged out successfully!");
                ConsoleArt.waitEnter();
                return;
            }

            int num;
            try {
                num = Integer.parseInt(choice);
            } catch (Exception e) {
                ConsoleArt.error("Please enter a number!");
                ConsoleArt.waitEnter();
                continue;
            }

            switch (num) {
                case 1 -> viewAllMedia(books, cds, loanService);
                case 2 -> addMedia(sc, books, cds);
                case 3 -> deleteMedia(sc, books, cds);
                case 4 -> searchMedia(sc, books, cds, loanService);
                case 5 -> viewUsersAndFines(users);
                case 6 -> registerNewUser(sc, users);
                case 7 -> showBorrowedCount(books, cds);
                case 8 -> sendReminders(users, loanService, notifier);
                case 9 -> unregisterUser(sc, users, loanService);
                default -> {
                    ConsoleArt.error("Wrong choice!");
                    ConsoleArt.waitEnter();
                }
            }
        }
    }

    private static void userMenu(Scanner sc, User user, List<Book> books, List<CD> cds, LoanService loanService) {
        while (true) {
            ConsoleArt.clear();
            ConsoleArt.title("Welcome, " + user.getName() + "!");

            String fineText = user.getFineBalance() > 0
                ? ConsoleArt.RED + "Outstanding Fine: $" + String.format("%.2f", user.getFineBalance()) + ConsoleArt.RESET
                : ConsoleArt.GREEN + "No outstanding fines" + ConsoleArt.RESET;

            String menu = """
                %s

                1 │ View All Books
                2 │ View All CDs
                3 │ Search Media
                4 │ Borrow Media
                5 │ Return Media
                6 │ View & Pay Fine
                7 │ Logout
                """.formatted(fineText);

            ConsoleArt.box(menu);
            System.out.print(ConsoleArt.CYAN + "Choose: " + ConsoleArt.RESET);

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                sc.nextLine();
                ConsoleArt.error("Enter a valid number!");
                ConsoleArt.waitEnter();
                continue;
            }

            switch (choice) {
                case 1 -> showAll(books, loanService, "BOOKS");
                case 2 -> showAll(cds, loanService, "CDS");
                case 3 -> searchMediaUser(sc, books, cds, loanService);
                case 4 -> borrowMedia(sc, user, books, cds, loanService);
                case 5 -> returnMedia(sc, user, loanService);
                case 6 -> payFine(sc, user);
                case 7 -> {
                    ConsoleArt.success("See you, " + user.getName() + "!");
                    ConsoleArt.waitEnter();
                    return;
                }
                default -> {
                    ConsoleArt.error("Invalid choice!");
                    ConsoleArt.waitEnter();
                }
            }
        }
    }

    private static void showAll(List<? extends Media> list, LoanService loanService, String type) {
        ConsoleArt.clear();
        ConsoleArt.title(type.isEmpty() ? "All Media" : "All " + type);

        for (Media m : list) {
            int total = m.getQuantity();
            int borrowed = (int) loanService.getAllLoans().stream()
                .filter(l -> l.getMedia().equals(m) && !l.isReturned())
                .count();
            int available = total - borrowed;

            String status = available > 0
                ? ConsoleArt.GREEN + "(Available)" + ConsoleArt.RESET
                : ConsoleArt.RED + "(Borrowed)" + ConsoleArt.RESET;

            System.out.println(" • " + m + " | Total: " + total + " | Borrowed: " + borrowed + 
                             " | Available: " + available + " " + status);
        }
        ConsoleArt.waitEnter();
    }

    private static void viewAllMedia(List<Book> books, List<CD> cds, LoanService loanService) {
        ConsoleArt.clear();
        ConsoleArt.title("All Library Media");
        System.out.println(ConsoleArt.YELLOW + ConsoleArt.BOLD + "BOOKS" + ConsoleArt.RESET);
        showAll(books, loanService, "");
        System.out.println("\n" + ConsoleArt.YELLOW + ConsoleArt.BOLD + "CDS" + ConsoleArt.RESET);
        showAll(cds, loanService, "");
        ConsoleArt.waitEnter();
    }

    private static void addMedia(Scanner sc, List<Book> books, List<CD> cds) {
        ConsoleArt.clear();
        ConsoleArt.title("Add New Media");
        System.out.print("1-Book 2-CD: ");
        int type = sc.nextInt(); sc.nextLine();

        System.out.print("Quantity: ");
        int qty = sc.nextInt(); sc.nextLine();
        if (qty <= 0) {
            ConsoleArt.error("Quantity must be more than 0!");
            ConsoleArt.waitEnter();
            return;
        }

        if (type == 1) {
            System.out.print("Title: "); String title = sc.nextLine();
            System.out.print("Author: "); String author = sc.nextLine();
            System.out.print("ISBN: "); String isbn = sc.nextLine();

            boolean exists = false;
            for (Book b : books) {
                if (b.getIsbn().equals(isbn)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                ConsoleArt.error("This ISBN already exists!");
            } else {
                books.add(new Book(title, author, isbn, qty));
                ConsoleArt.success("Book added successfully!");
            }
        } else if (type == 2) {
            System.out.print("Title: "); String title = sc.nextLine();
            System.out.print("Artist: "); String artist = sc.nextLine();
            System.out.print("CD ID: "); String id = sc.nextLine();

            boolean exists = false;
            for (CD c : cds) {
                if (c.getIsbn().equals(id)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                ConsoleArt.error("This CD ID already exists!");
            } else {
                cds.add(new CD(title, artist, id, qty));
                ConsoleArt.success("CD added successfully!");
            }
        } else {
            ConsoleArt.error("Wrong choice!");
        }
        ConsoleArt.waitEnter();
    }

    private static void deleteMedia(Scanner sc, List<Book> books, List<CD> cds) {
        ConsoleArt.clear();
        ConsoleArt.title("Delete Media");
        System.out.print("1-Book 2-CD: ");
        int type = sc.nextInt(); sc.nextLine();

        if (type == 1) {
            System.out.print("Enter ISBN: "); String isbn = sc.nextLine();
            books.removeIf(b -> b.getIsbn().equals(isbn));
            ConsoleArt.success("Book deleted if it existed!");
        } else if (type == 2) {
            System.out.print("Enter CD ID: "); String id = sc.nextLine();
            cds.removeIf(c -> c.getIsbn().equals(id));
            ConsoleArt.success("CD deleted if it existed!");
        }
        ConsoleArt.waitEnter();
    }

    private static void searchMedia(Scanner sc, List<Book> books, List<CD> cds, LoanService loanService) {
        ConsoleArt.clear();
        ConsoleArt.title("Search Media");
        System.out.print("1-Book 2-CD: ");
        int type = sc.nextInt(); sc.nextLine();

        if (type == 1) {
            System.out.println("Search by: 1-Title 2-Author 3-ISBN");
            int by = sc.nextInt(); sc.nextLine();
            System.out.print("Enter search word: "); String word = sc.nextLine();

            List<Book> result = new ArrayList<>();
            for (Book b : books) {
                boolean match = false;
                if (by == 1 && b.getTitle().toLowerCase().contains(word.toLowerCase())) match = true;
                if (by == 2 && b.getAuthor().toLowerCase().contains(word.toLowerCase())) match = true;
                if (by == 3 && b.getIsbn().equalsIgnoreCase(word)) match = true;
                if (match && b.getQuantity() > 0) result.add(b);
            }
            showAllWithQuantity(result, loanService, "Search Results - Books");
        } else if (type == 2) {
            System.out.println("Search by: 1-Title 2-Artist 3-CD ID");
            int by = sc.nextInt(); sc.nextLine();
            System.out.print("Enter search word: "); String word = sc.nextLine();

            List<CD> result = new ArrayList<>();
            for (CD c : cds) {
                boolean match = false;
                if (by == 1 && c.getTitle().toLowerCase().contains(word.toLowerCase())) match = true;
                if (by == 2 && c.getAuthor().toLowerCase().contains(word.toLowerCase())) match = true;
                if (by == 3 && c.getIsbn().equalsIgnoreCase(word)) match = true;
                if (match && c.getQuantity() > 0) result.add(c);
            }
            showAllWithQuantity(result, loanService, "Search Results - CDs");
        }
        ConsoleArt.waitEnter();
    }

    private static void searchMediaUser(Scanner sc, List<Book> books, List<CD> cds, LoanService loanService) {
        searchMedia(sc, books, cds, loanService);
    }

    private static void viewUsersAndFines(List<User> users) {
        ConsoleArt.clear();
        ConsoleArt.title("Users & Fines");
        for (User u : users) {
            System.out.println(" • " + u.getName() + " | " + u.getEmail() + 
                " | Fine: " + ConsoleArt.RED + "$" + u.getFineBalance() + ConsoleArt.RESET);
        }
        ConsoleArt.waitEnter();
    }

    private static void registerNewUser(Scanner sc, List<User> users) {
        ConsoleArt.clear();
        ConsoleArt.title("Register New User");
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        users.add(new User(name, email));
        ConsoleArt.success("User registered!");
        ConsoleArt.waitEnter();
    }

    private static void showBorrowedCount(List<Book> books, List<CD> cds) {
        int bookCount = 0, cdCount = 0;
        for (Book b : books) if (b.isBorrowed()) bookCount++;
        for (CD c : cds) if (c.isBorrowed()) cdCount++;

        ConsoleArt.clear();
        ConsoleArt.title("Borrowed Items");
        System.out.println("Total borrowed: " + (bookCount + cdCount));
        System.out.println(" Books: " + bookCount);
        System.out.println(" CDs: " + cdCount);
        ConsoleArt.waitEnter();
    }

    private static void sendReminders(List<User> users, LoanService loanService, MediaEmailNotifier notifier) {
        ConsoleArt.clear();
        ConsoleArt.title("Sending Reminders");
        int count = 0;
        for (User u : users) {
            boolean hasOverdue = false;
            for (Loan l : loanService.getUserLoans(u)) {
                if (l.isOverdue()) {
                    hasOverdue = true;
                    break;
                }
            }
            if (hasOverdue) {
                notifier.update(null, u);
                count++;
            }
        }
        ConsoleArt.success(count + " reminder(s) sent!");
        ConsoleArt.waitEnter();
    }

    private static void unregisterUser(Scanner sc, List<User> users, LoanService loanService) {
        ConsoleArt.clear();
        ConsoleArt.title("Unregister User");
        System.out.print("Enter user email: "); String email = sc.nextLine();

        User found = null;
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                found = u;
                break;
            }
        }

        if (found == null) ConsoleArt.error("User not found!");
        else if (found.getFineBalance() > 0) ConsoleArt.error("User has unpaid fines!");
        else if (loanService.getUserLoans(found).stream().anyMatch(l -> !l.isReturned())) 
            ConsoleArt.error("User has active loans!");
        else {
            users.remove(found);
            ConsoleArt.success("User removed!");
        }
        ConsoleArt.waitEnter();
    }

    private static void borrowMedia(Scanner sc, User user, List<Book> books, List<CD> cds, LoanService loanService) {
        ConsoleArt.clear();
        ConsoleArt.title("Borrow Media");

        boolean hasIssue = false;
        for (Loan l : loanService.getUserLoans(user)) {
            if (l.isOverdue() || !l.isReturned()) {
                hasIssue = true;
                break;
            }
        }
        if (hasIssue) {
            ConsoleArt.error("You have overdue or active loans!");
            ConsoleArt.waitEnter();
            return;
        }

        System.out.print("1-Book 2-CD: ");
        int type = sc.nextInt(); sc.nextLine();

        if (type == 1) {
            System.out.print("Book title: "); String title = sc.nextLine();
            Book found = null;
            for (Book b : books) {
                if (b.getTitle().equalsIgnoreCase(title) && b.getQuantity() > 0) {
                    found = b;
                    break;
                }
            }
            if (found != null) {
                loanService.createLoan(found, user);
                ConsoleArt.success("Book borrowed! Available now: " + found.getQuantity());
            } else ConsoleArt.error("Book not available!");
        } else if (type == 2) {
            System.out.print("CD title: "); String title = sc.nextLine();
            CD found = null;
            for (CD c : cds) {
                if (c.getTitle().equalsIgnoreCase(title) && c.getQuantity() > 0) {
                    found = c;
                    break;
                }
            }
            if (found != null) {
                loanService.createLoan(found, user);
                ConsoleArt.success("CD borrowed! Available now: " + found.getQuantity());
            } else ConsoleArt.error("CD not available!");
        }
        ConsoleArt.waitEnter();
    }

    private static void returnMedia(Scanner sc, User user, LoanService loanService) {
        ConsoleArt.clear();
        ConsoleArt.title("Return Media");

        List<Loan> activeLoans = new ArrayList<>();
        for (Loan l : loanService.getUserLoans(user)) {
            if (!l.isReturned()) activeLoans.add(l);
        }

        if (activeLoans.isEmpty()) {
            ConsoleArt.error("You have nothing to return!");
            ConsoleArt.waitEnter();
            return;
        }

        System.out.print("1-Book 2-CD: ");
        int type = sc.nextInt(); sc.nextLine();

        List<Loan> typeLoans = new ArrayList<>();
        if (type == 1) {
            for (Loan l : activeLoans) {
                if (l.getMedia() instanceof Book) typeLoans.add(l);
            }
        } else {
            for (Loan l : activeLoans) {
                if (l.getMedia() instanceof CD) typeLoans.add(l);
            }
        }

        if (typeLoans.isEmpty()) {
            ConsoleArt.error("You don't have any " + (type == 1 ? "books" : "CDs") + " to return!");
        } else {
            System.out.println("Your items:");
            for (int i = 0; i < typeLoans.size(); i++) {
                System.out.println((i+1) + "- " + typeLoans.get(i).getMedia().getTitle());
            }
            System.out.print("Choose number to return: ");
            int num = sc.nextInt(); sc.nextLine();

            if (num < 1 || num > typeLoans.size()) {
                ConsoleArt.error("Wrong number!");
            } else {
                Loan loan = typeLoans.get(num - 1);
                if (loan.isOverdue()) {
                    System.out.println(ConsoleArt.YELLOW + "Overdue! Fine: $" + loan.getFineAmount() + ConsoleArt.RESET);
                }
                loanService.returnLoan(loan);
                ConsoleArt.success((type == 1 ? "Book" : "CD") + " returned successfully!");
            }
        }
        ConsoleArt.waitEnter();
    }

    private static void payFine(Scanner sc, User user) {
        ConsoleArt.clear();
        ConsoleArt.title("Pay Fine");
        System.out.println("Current fine: " + ConsoleArt.RED + "$" + user.getFineBalance() + ConsoleArt.RESET);
        System.out.println();
        System.out.println(ConsoleArt.BLUE + "Overdue items:" + ConsoleArt.RESET);

        boolean hasOverdue = false;
        for (Loan l : user.getLoans()) {
            if (l.isOverdue()) {
                hasOverdue = true;
                System.out.println(" - " + l.getMedia().getTitle() + 
                    " | Days late: " + l.getDaysOverdue() + 
                    " | Fine: $" + l.getFineAmount());
            }
        }
        if (!hasOverdue) System.out.println(ConsoleArt.YELLOW + "No overdue items." + ConsoleArt.RESET);

        System.out.println();

        if (user.getFineBalance() <= 0) {
            ConsoleArt.info("You have no fine to pay.");
        } else {
            System.out.print("How much do you want to pay? $");
            double amount = sc.nextDouble(); sc.nextLine();
            if (amount > 0 && amount <= user.getFineBalance()) {
                user.payFine(amount);
                ConsoleArt.success("Paid! Remaining fine: $" + user.getFineBalance());
            } else {
                ConsoleArt.error("Invalid amount!");
            }
        }
        ConsoleArt.waitEnter();
    }

    private static <T extends Media> void showAllWithQuantity(List<T> list, LoanService loanService, String title) {
        ConsoleArt.clear();
        ConsoleArt.title(title);
        for (T item : list) {
            int borrowed = 0;
            for (Loan l : loanService.getAllLoans()) {
                if (l.getMedia().equals(item) && !l.isReturned()) borrowed++;
            }
            int available = item.getQuantity() - borrowed;
            String status = available > 0 
                ? ConsoleArt.GREEN + "(Available: " + available + ")" + ConsoleArt.RESET
                : ConsoleArt.RED + "(Not available)" + ConsoleArt.RESET;

            System.out.println(" • " + item + " | Total: " + item.getQuantity() + " " + status);
        }
        ConsoleArt.waitEnter();
    }
}
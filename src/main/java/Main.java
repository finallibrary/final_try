import java.util.*;
import java.util.List;
import java.time.LocalDate;
import javax.swing.*;
import java.awt.*;
import io.github.cdimascio.dotenv.Dotenv;

import model.*;
import service.*;
import model.ConsoleArt;

public class Main {
  /*   private static final int BORROW_DAYS = 28;
    private static FineService fineService = new FineService();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        
        // ======= Default Data =======
        Book b1 = new Book("Java Basics", "James Gosling", "001",1
        		);
        Book b2 = new Book("Python Intro", "Guido Rossum", "002",8);
        Book b3 = new Book("C++ Fundamentals", "Bjarne Stroustrup", "003",5);
        Book b4 = new Book("Data Structures", "Mark Allen", "004",1);
        Book b5 = new Book("Algorithms", "Robert Sedgewick", "005",6);
        Book b6 = new Book("Machine Learning", "Tom Mitchell", "006",3);
        List<Book> books = new ArrayList<>(Arrays.asList(b1, b2, b3, b4, b5, b6));

        CD cd1 = new CD("Classical Hits", "Beethoven", "CD001",9);
        CD cd2 = new CD("Rock Classics", "Queen", "CD002",4);
        CD cd3 = new CD("Jazz Essentials", "Miles Davis", ""
        		+ ""
        		+ "",2);
        CD cd4 = new CD("Pop Top", "Taylor Swift", "CD004",7);
        List<CD> cds = new ArrayList<>(Arrays.asList(cd1, cd2, cd3, cd4));

        List<User> users = new ArrayList<>();
        User u1 = new User("Noor", "halawad257@gmail.com");
        User u2 = new User("Hala", "s12217844@stu.najah.edu");
        User u3 = new User("Hala", "halaawwad455@gmail.com");
        User u4 = new User("Sara", "sara@gmail.com");
        users.addAll(Arrays.asList(u1, u2, u3, u4));

        FineService fineService = new FineService();
        LoanService loanService = new LoanService();

        Dotenv dotenv = Dotenv.load();
        String emailUser = dotenv.get("EMAIL_USERNAME");
        String emailPass = dotenv.get("EMAIL_PASSWORD");
        String adminUsername = dotenv.get("ADMIN_USERNAME");
        String adminPassword = dotenv.get("ADMIN_PASSWORD");

        EmailService emailService = new EmailService(emailUser, emailPass);
        MediaEmailNotifier notifier = new MediaEmailNotifier(emailService);

        Admin admin = new Admin(adminUsername, adminPassword);

        // Auto create some overdue loans for testing
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
        } catch (Exception ignored) {}

        if (l1.isOverdue()) fineService.addFine(u1, l1.getFineAmount());
        if (l2.isOverdue()) fineService.addFine(u2, l2.getFineAmount());
        if (l3.isOverdue()) fineService.addFine(u3, l3.getFineAmount());
        if (l4.isOverdue()) fineService.addFine(u3, l4.getFineAmount());
        if (l5.isOverdue()) fineService.addFine(u3, l5.getFineAmount());
        if (l6.isOverdue()) fineService.addFine(u2, l6.getFineAmount());

        // ==================================== GUI MAIN MENU ====================================
        JFrame frame = new JFrame("Library Management System");
        frame.setSize(520, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        ImageIcon bg = new ImageIcon("C:\\Users\\kp\\eclipse-workspace\\try\\src\\test\\resources\\library.png");
        JLabel background = new JLabel(bg);
        background.setLayout(new GridBagLayout());
        frame.setContentPane(background);

        Font f = new Font("Arial", Font.BOLD, 20);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ألوان هادية + شفافية
        Color softGray = new Color(60, 63, 65, 150); // شفاف
        Color softRed  = new Color(120, 40, 40, 150); // شفاف أحمر هادي
        Color textColor = new Color(240, 240, 240);

        // -----------------------------------------------------
        // Admin Button
        // -----------------------------------------------------
        JButton adminBtn = new JButton("Login as Admin");
        adminBtn.setFont(f);
        adminBtn.setPreferredSize(new Dimension(250, 60));
        adminBtn.setForeground(textColor);
        adminBtn.setBackground(softGray);
        adminBtn.setOpaque(true);
        adminBtn.setContentAreaFilled(true);
        adminBtn.setFocusPainted(false);
        adminBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200,120), 2));

        gbc.gridy = 0;
        background.add(adminBtn, gbc);

        // -----------------------------------------------------
        // User Button
        // -----------------------------------------------------
        JButton userBtn = new JButton("Login as User");
        userBtn.setFont(f);
        userBtn.setPreferredSize(new Dimension(250, 60));
        userBtn.setForeground(textColor);
        userBtn.setBackground(softGray);
        userBtn.setOpaque(true);
        userBtn.setContentAreaFilled(true);
        userBtn.setFocusPainted(false);
        userBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200,120), 2));

        gbc.gridy = 1;
        background.add(userBtn, gbc);

        // -----------------------------------------------------
        // Exit Button
        // -----------------------------------------------------
        JButton exitBtn = new JButton("Exit");
        exitBtn.setFont(f);
        exitBtn.setPreferredSize(new Dimension(250, 60));
        exitBtn.setForeground(textColor);
        exitBtn.setBackground(softRed);
        exitBtn.setOpaque(true);
        exitBtn.setContentAreaFilled(true);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200,120), 2));

        gbc.gridy = 2;
        background.add(exitBtn, gbc);

      


        adminBtn.addActionListener(e -> adminLogin(frame, admin, sc, books, cds, users, loanService, notifier));
        userBtn.addActionListener(e -> userLogin(frame, users, sc, books, cds, loanService));
        exitBtn.addActionListener(e -> System.exit(0));

        
        adminBtn.setFocusable(false);
        userBtn.setFocusable(false);
        exitBtn.setFocusable(false);

        adminBtn.setRolloverEnabled(false);
        userBtn.setRolloverEnabled(false);
        exitBtn.setRolloverEnabled(false);
     
        frame.setVisible(true);
    }

    private static void adminLogin(JFrame frame, Admin admin, Scanner sc, List<Book> books, List<CD> cds,
                                   List<User> users, LoanService loanService, MediaEmailNotifier notifier) {
        JPanel p = new JPanel(new GridLayout(3,2,10,15));
        p.add(new JLabel("Username:")); JTextField u = new JTextField();
        p.add(u); p.add(new JLabel("Password:")); JPasswordField pf = new JPasswordField();
        p.add(pf);

        int r = JOptionPane.showConfirmDialog(frame, p, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            if (u.getText().equals(admin.getUsername()) && admin.checkPassword(new String(pf.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Welcome Admin!");
                adminMenu(sc, books, cds, users, loanService, notifier);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void userLogin(JFrame frame, List<User> users, Scanner sc, List<Book> books, List<CD> cds, LoanService loanService) {
        JPanel p = new JPanel(new GridLayout(3,2,10,15));
        p.add(new JLabel("Name:")); JTextField n = new JTextField();
        p.add(n); p.add(new JLabel("Email:")); JTextField e = new JTextField();
        p.add(e);

        int r = JOptionPane.showConfirmDialog(frame, p, "User Login", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            User user = users.stream()
                    .filter(u -> u.getName().equalsIgnoreCase(n.getText().trim()) && u.getEmail().equalsIgnoreCase(e.getText().trim()))
                    .findFirst().orElse(null);
            if (user != null) {
                JOptionPane.showMessageDialog(frame, "Welcome, " + user.getName() + "!");
                userMenu(sc, user, books, cds, loanService);
            } else {
                JOptionPane.showMessageDialog(frame, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ================================== ADMIN MENU ==================================
    private static void adminMenu(Scanner sc, List<Book> books, List<CD> cds,
                                  List<User> users, LoanService loanService,
                                  MediaEmailNotifier notifier) {
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
            String in = sc.nextLine().trim();

            if (in.equals("0")) { ConsoleArt.success("Logged out!"); ConsoleArt.waitEnter(); return; }

            int c;
            try { c = Integer.parseInt(in); } catch (Exception ex) { ConsoleArt.error("Invalid input!"); ConsoleArt.waitEnter(); continue; }

            switch (c) {
            case 1 -> viewAllMedia(books, cds, loanService);

                case 2 -> addMedia(sc, books, cds);
                case 3 -> deleteMedia(sc, books, cds);
                case 4 -> searchMedia(sc, books, cds,loanService);
                case 5 -> viewUsersAndFines(users);
                case 6 -> registerNewUser(sc, users);
                case 7 -> showBorrowedCount(books, cds);
                case 8 -> sendReminders(users, loanService, notifier);
                case 9 -> unregisterUser(sc, users, loanService);
                default -> { ConsoleArt.error("Invalid option!"); ConsoleArt.waitEnter(); }
            }
        }
    }

    // ================================== USER MENU ==================================
    private static void userMenu(Scanner sc, User user, List<Book> books,
                                 List<CD> cds, LoanService loanService) {
        while (true) {
            ConsoleArt.clear();
            ConsoleArt.title("Welcome, " + user.getName() + "!");

            String fine = user.getFineBalance() > 0
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
                """.formatted(fine);

            ConsoleArt.box(menu);

            System.out.print(ConsoleArt.CYAN + "Choose: " + ConsoleArt.RESET);
            int c = 0;
            try { c = sc.nextInt(); sc.nextLine(); }
            catch (Exception e) { sc.nextLine(); ConsoleArt.error("Enter a number!"); ConsoleArt.waitEnter(); continue; }

            switch (c) {
                case 1 -> showAll(books, loanService, "BOOKS");
                case 2 -> showAll(cds, loanService, "CDS");
                case 3 -> searchMediaUser(sc, books, cds,loanService);
                case 4 -> borrowMedia(sc, user, books, cds, loanService);
                case 5 -> returnMedia(sc, user, loanService);
                case 6 -> payFine(sc, user);
                case 7 -> { ConsoleArt.success("Goodbye, " + user.getName() + "!"); ConsoleArt.waitEnter(); return; }
                default -> { ConsoleArt.error("Invalid choice!"); ConsoleArt.waitEnter(); }
            }
        }
    }

    // ================================== HELPER METHODS ==================================
    private static void showAll(List<? extends Media> list, LoanService loanService, String type) {
        ConsoleArt.clear();
        ConsoleArt.title(type.isEmpty() ? "Media List" : "All " + type);

        for (Media m : list) {
            int total = m.getQuantity(); // الكمية الكلية
            int borrowed = (int) loanService.getAllLoans().stream() // كل القرضات
                               .filter(l -> l.getMedia().equals(m) && !l.isReturned())
                               .count();
            int available = total - borrowed;

            // الحالة تعتمد على عدد النسخ المتاحة
            String status = available > 0 
                            ? ConsoleArt.GREEN + "(Available)" + ConsoleArt.RESET
                            : ConsoleArt.RED + "(Borrowed)" + ConsoleArt.RESET;

            System.out.println(" • " + m + " | Total: " + total + " | Borrowed: " + borrowed + " | Available: " + available + " " + status);
        }

        ConsoleArt.waitEnter();
    }


    private static void viewAllMedia(List<Book> books, List<CD> cds, LoanService loanService) {
        ConsoleArt.clear();
        ConsoleArt.title("All Library Media");

        System.out.println(ConsoleArt.YELLOW + ConsoleArt.BOLD + "BOOKS" + ConsoleArt.RESET);
        showAll(books, loanService, "" );

        System.out.println("\n" + ConsoleArt.YELLOW + ConsoleArt.BOLD + "CDS" + ConsoleArt.RESET);
        showAll(cds, loanService,"");

        ConsoleArt.waitEnter();
    }


   private static void addMedia(Scanner sc, List<Book> books, List<CD> cds) {
    ConsoleArt.clear();
    ConsoleArt.title("Add New Media");
    System.out.print("1-Book  2-CD → ");
    int t = sc.nextInt(); sc.nextLine();

    System.out.print("Quantity: "); 
    int qty = sc.nextInt(); sc.nextLine(); // قراءة الكمية
    if (qty <= 0) { ConsoleArt.error("Quantity must be > 0!"); ConsoleArt.waitEnter(); return; }

    if (t == 1) {
        System.out.print("Title: "); String title = sc.nextLine();
        System.out.print("Author: "); String author = sc.nextLine();
        System.out.print("ISBN: "); String isbn = sc.nextLine();
        if (books.stream().anyMatch(b -> b.getIsbn().equals(isbn))) {
            ConsoleArt.error("ISBN already exists!");
        } else {
            books.add(new Book(title, author, isbn, qty));
            ConsoleArt.success("Book added with quantity " + qty + "!");
        }
    } else if (t == 2) {
        System.out.print("Title: "); String title = sc.nextLine();
        System.out.print("Artist: "); String artist = sc.nextLine();
        System.out.print("CD ID: "); String id = sc.nextLine();
        if (cds.stream().anyMatch(c -> c.getId().equals(id))) {
            ConsoleArt.error("CD ID already exists!");
        } else {
            cds.add(new CD(title, artist, id, qty));
            ConsoleArt.success("CD added with quantity " + qty + "!");
        }
    } else ConsoleArt.error("Wrong type!");
    ConsoleArt.waitEnter();
}

    private static void deleteMedia(Scanner sc, List<Book> books, List<CD> cds) {
        ConsoleArt.clear();
        ConsoleArt.title("Delete Media");
        System.out.print("1-Book  2-CD → ");
        int t = sc.nextInt(); sc.nextLine();

        if (t == 1) {
            System.out.print("Enter ISBN: "); String isbn = sc.nextLine();
            books.removeIf(b -> b.getIsbn().equals(isbn));
            ConsoleArt.success("Book deleted (if existed)!");
        } else if (t == 2) {
            System.out.print("Enter CD ID: "); String id = sc.nextLine();
            cds.removeIf(c -> c.getId().equals(id));
            ConsoleArt.success("CD deleted (if existed)!");
        }
        ConsoleArt.waitEnter();
    }

  private static void searchMedia(Scanner sc, List<Book> books, List<CD> cds, LoanService loanService) {
    ConsoleArt.clear();
    ConsoleArt.title("Search Media");
    System.out.print("1-Book  2-CD → ");
    int t = sc.nextInt(); sc.nextLine();

    if (t == 1) {
        System.out.println("Search by: 1-Title  2-Author  3-ISBN");
        int s = sc.nextInt(); sc.nextLine();
        System.out.print("Query: "); String q = sc.nextLine();

        SearchStrategy<Book> strategy = switch (s) {
            case 1 -> new SearchByTitle<>();
            case 2 -> new SearchByAuthor<>();
            case 3 -> new SearchByISBN<>();
            default -> null;
        };

        if (strategy != null) {
            List<Book> res = strategy.search(books, q)
                                     .stream()
                                     .filter(book -> book.getQuantity() > 0)
                                     .toList();
            showAllWithQuantity(res, loanService, "Search Results - Books");
        } else {
            ConsoleArt.error("Invalid search type!");
        }

    } else if (t == 2) {
        System.out.println("Search by: 1-Title  2-Author  3-CD ID");
        int s = sc.nextInt(); sc.nextLine();
        System.out.print("Query: "); String q = sc.nextLine();

        List<CD> res = cds.stream()
                .filter(cd -> {
                    boolean matches = switch (s) {
                        case 1 -> cd.getTitle().toLowerCase().contains(q.toLowerCase());
                        case 2 -> cd.getAuthor().toLowerCase().contains(q.toLowerCase());
                        case 3 -> cd.getId().toLowerCase().contains(q.toLowerCase());
                        default -> false;
                    };
                    return matches && cd.getQuantity() > 0;
                })
                .toList();

        showAllWithQuantity(res, loanService, "Search Results - CDs");
    }

    ConsoleArt.waitEnter();
}

    private static void viewUsersAndFines(List<User> users) {
        ConsoleArt.clear();
        ConsoleArt.title("Users & Fines");
        users.forEach(u -> System.out.println(" • " + u.getName() + " | " + u.getEmail() +
                " | Fine: " + ConsoleArt.RED + "$" + u.getFineBalance() + ConsoleArt.RESET));
        ConsoleArt.waitEnter();
    }

    private static void registerNewUser(Scanner sc, List<User> users) {
        ConsoleArt.clear();
        ConsoleArt.title("Register New User");
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        users.add(new User(name, email));
        ConsoleArt.success("User registered successfully!");
        ConsoleArt.waitEnter();
    }

    private static void showBorrowedCount(List<Book> books, List<CD> cds) {
        long b = books.stream().filter(Book::isBorrowed).count();
        long c = cds.stream().filter(CD::isBorrowed).count();
        ConsoleArt.clear();
        ConsoleArt.title("Borrowed Media");
        System.out.println("Total borrowed items: " + (b + c));
        System.out.println("   Books: " + b);
        System.out.println("   CDs: " + c);
        ConsoleArt.waitEnter();
    }

    private static void sendReminders(List<User> users, LoanService loanService, MediaEmailNotifier notifier) {
        ConsoleArt.clear();
        ConsoleArt.title("Sending Overdue Reminders");
        int sent = 0;
        for (User u : users) {
            if (loanService.getUserLoans(u).stream().anyMatch(Loan::isOverdue)) {
                notifier.update(null, u);
                sent++;
            }
        }
        ConsoleArt.success(sent + " reminder email(s) sent!");
        ConsoleArt.waitEnter();
    }

    private static void unregisterUser(Scanner sc, List<User> users, LoanService loanService) {
        ConsoleArt.clear();
        ConsoleArt.title("Unregister User");
        System.out.print("Enter user email: "); String email = sc.nextLine();
        User target = users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);

        if (target == null) ConsoleArt.error("User not found!");
        else if (target.getFineBalance() > 0) ConsoleArt.error("Cannot delete user with unpaid fines!");
        else if (target.getLoans().stream().anyMatch(l -> !l.isReturned())) ConsoleArt.error("User has active loans!");
        else {
            users.remove(target);
            ConsoleArt.success("User " + target.getName() + " removed!");
        }
        ConsoleArt.waitEnter();
    }

    // User actions
    private static void searchMediaUser(Scanner sc, List<Book> books, List<CD> cds, LoanService loanService) {
        // نفس الكود بتاع البحث اللي فوق بس بدون عنوان مختلف
        searchMedia(sc, books, cds, loanService);
    }

    private static void borrowMedia(Scanner sc, User user, List<Book> books, List<CD> cds, LoanService loanService) {
    ConsoleArt.clear();
    ConsoleArt.title("Borrow Media");

    if (loanService.getUserLoans(user).stream().anyMatch(l -> l.isOverdue() || !l.isReturned())) {
        ConsoleArt.error("You have overdue or active items!");
        ConsoleArt.waitEnter();
        return;
    }

    System.out.print("1-Book  2-CD → ");
    int t = sc.nextInt(); sc.nextLine();

    if (t == 1) {
        System.out.print("Book title: "); String title = sc.nextLine();
        Book b = books.stream()
                      .filter(book -> book.getTitle().equalsIgnoreCase(title) && book.getQuantity() > 0)
                      .findFirst().orElse(null);
        if (b != null) { 
            loanService.createLoan(b, user); 
            ConsoleArt.success("Book borrowed! Remaining quantity: " + b.getQuantity());
        } else ConsoleArt.error("Book not available!");
    } else if (t == 2) {
        System.out.print("CD title: "); String title = sc.nextLine();
        CD c = cds.stream()
                  .filter(cd -> cd.getTitle().equalsIgnoreCase(title) && cd.getQuantity() > 0)
                  .findFirst().orElse(null);
        if (c != null) { 
            loanService.createLoan(c, user); 
            ConsoleArt.success("CD borrowed! Remaining quantity: " + c.getQuantity());
        } else ConsoleArt.error("CD not available!");
    }
    ConsoleArt.waitEnter();
}

   // نفترض عندك هذا في مكان أعلى، مثلاً في main


 // على مستوى الكلاس

private static void returnMedia(Scanner sc, User user, LoanService loanService) {
    ConsoleArt.clear();
    ConsoleArt.title("Return Media");

    List<Loan> userLoans = loanService.getUserLoans(user).stream()
            .filter(l -> !l.isReturned())
            .toList();

    if (userLoans.isEmpty()) {
        ConsoleArt.error("You have no borrowed media!");
        ConsoleArt.waitEnter();
        return;
    }

    System.out.print("1-Book  2-CD → ");
    int t = sc.nextInt(); sc.nextLine();

    if (t == 1) {
        List<Loan> books = userLoans.stream()
                .filter(l -> l.getMedia() instanceof Book)
                .toList();
        if (books.isEmpty()) ConsoleArt.error("You have no borrowed books!");
        else {
            System.out.println("Books you borrowed:");
            for (int i = 0; i < books.size(); i++)
                System.out.println((i+1) + "- " + books.get(i).getMedia().getTitle());

            System.out.print("Select book number to return → ");
            int choice = sc.nextInt(); sc.nextLine();

            if (choice < 1 || choice > books.size()) ConsoleArt.error("Invalid selection!");
            else {
                Loan loan = books.get(choice - 1);
                if (loan.isOverdue() ) {
                    double fine = loan.getFineAmount();
                  
                   
                    System.out.println(ConsoleArt.YELLOW + "This book is overdue! Fine: $" + fine + ConsoleArt.RESET);
                }   
            
                loanService.returnLoan(loan);
                ConsoleArt.success("Book returned!");
            }
        }
    } else if (t == 2) {
        List<Loan> cds = userLoans.stream()
                .filter(l -> l.getMedia() instanceof CD)
                .toList();
        if (cds.isEmpty()) ConsoleArt.error("You have no borrowed CDs!");
        else {
            System.out.println("CDs you borrowed:");
            for (int i = 0; i < cds.size(); i++)
                System.out.println((i+1) + "- " + cds.get(i).getMedia().getTitle());

            System.out.print("Select CD number to return → ");
            int choice = sc.nextInt(); sc.nextLine();

            if (choice < 1 || choice > cds.size()) ConsoleArt.error("Invalid selection!");
            else {
                Loan loan = cds.get(choice - 1);
                if (loan.isOverdue() ) {
                    double fine = loan.getFineAmount();
                   
                 
                    System.out.println(ConsoleArt.YELLOW + "This CD is overdue! Fine: $" + fine + ConsoleArt.RESET);
                }
                loanService.returnLoan(loan);
                ConsoleArt.success("CD returned!");
            }
        }
    }

    ConsoleArt.waitEnter();
}

    private static void payFine(Scanner sc, User user) {
        ConsoleArt.clear();
        ConsoleArt.title("Fine Payment");

        // --- طباعة الغرامة الحالية ---
        System.out.println("Current fine: " + ConsoleArt.RED + "$" + user.getFineBalance() + ConsoleArt.RESET);

        // --- عرض الكتب/CDs المتأخرة ---
        System.out.println();
        System.out.println(ConsoleArt.BLUE + "Overdue items:" + ConsoleArt.RESET);

        boolean found = false;

        for (Loan loan : user.getLoans()) {
            if (loan.isOverdue()) {
                Media m = loan.getMedia();
                found = true;

                System.out.println(" - " + m.getTitle()
                        + " | Days overdue: " + loan.getDaysOverdue()
                        + " | Fine: " + loan.getFineAmount() + " NIS");
            }
        }

        if (!found) {
            System.out.println(ConsoleArt.YELLOW + "No overdue items." + ConsoleArt.RESET);
        }

        System.out.println();

        // --- الدفع ---
        if (user.getFineBalance() <= 0) {
            ConsoleArt.info("No fine to pay.");
        } else {
            System.out.print("Amount to pay: $");
            double amt = sc.nextDouble();
            sc.nextLine();

            if (amt > 0 && amt <= user.getFineBalance()) {
                user.payFine(amt);
                ConsoleArt.success("Payment successful! Remaining: $" + user.getFineBalance());
            } else {
                ConsoleArt.error("Invalid amount!");
            }
        }

        ConsoleArt.waitEnter();
    }
    
    private static <T extends Media> void showAllWithQuantity(List<T> list, LoanService loanService, String type) {
        ConsoleArt.clear();
        ConsoleArt.title(type);
        for (T item : list) {
            long borrowedCount = loanService.getAllLoans().stream()
                                    .filter(l -> l.getMedia().equals(item) && !l.isReturned())
                                    .count();
            long available = item.getQuantity() - borrowedCount;
            String status = available > 0
                            ? ConsoleArt.GREEN + "(Available: " + available + ")" + ConsoleArt.RESET
                            : ConsoleArt.RED + "(Borrowed out)" + ConsoleArt.RESET;

            System.out.println(" • " + item + " | Total: " + item.getQuantity() + "  " + status);
        }
        ConsoleArt.waitEnter();
    }*/


    
}

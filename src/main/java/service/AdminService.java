package service;

import model.Admin;
import model.User;
import model.Loan;
import exception.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdminService {

    private final Admin admin;
    private boolean loggedIn = false;
    private static final List<User> users = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(AdminService.class.getName());

    public AdminService(Admin admin) {
        this.admin = admin;
    }

    public void login(String username, String password) {
        boolean usernameMatches = admin.getUsername().equals(username);
        boolean passwordMatches = admin.checkPassword(password);

        if (usernameMatches && passwordMatches) {
            loggedIn = true;
            logger.info("Login successful! Welcome, admin.");
        } else {
            throw new AuthenticationException("Invalid credentials!");
        }
    }

    public void logout() {
        if (loggedIn) {
            loggedIn = false;
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void addUser(User user) {
        if (loggedIn) {
            users.add(user);
        }
    }

    public void unregisterUser(User user) {
        if (!loggedIn) {
            logger.warning("Access denied! Please log in as admin first.");
            return;
        }

        if (user.getFineBalance() > 0) {
            logger.warning("Cannot unregister user with unpaid fines.");
            return;
        }

        boolean hasActiveLoan = user.getLoans().stream().anyMatch(loan -> !loan.isReturned());
        if (hasActiveLoan) {
            logger.warning("Cannot unregister user with active loans.");
            return;
        }

        users.remove(user);
        logger.info("User " + user.getName() + " unregistered successfully.");
    }

    public static List<User> getAllUsers() {
        return users;
    }
}

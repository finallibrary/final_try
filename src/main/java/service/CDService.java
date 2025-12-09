package service;

import model.CD;
import model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CDService extends Observable {

    private final List<CD> cds = new ArrayList<>();
    private SearchStrategy<CD> searchStrategy;

    public void addCD(String title, String artist, String id, int quantity) {
        CD existing = cds.stream()
                         .filter(c -> c.getIsbn().equals(id))
                         .findFirst()
                         .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            cds.add(new CD(title, artist, id, quantity));
        }
    }

    public void addCD(CD cd) {
        CD existing = cds.stream()
                         .filter(c -> c.getIsbn().equals(cd.getIsbn()))
                         .findFirst()
                         .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + cd.getQuantity());
        } else {
            cds.add(cd);
        }
    }

    public List<CD> getAllCDs() {
        return cds;
    }

    public void setSearchStrategy(SearchStrategy strategy) {
        this.searchStrategy = strategy;
    }

    public List<CD> search(String query) {
        if (searchStrategy == null) {
            return new ArrayList<>();
        }
        return searchStrategy.search(cds, query);
    }

    public boolean borrowCD(CD cd, User user) {
        if (!user.canBorrow()) {
            return false;
        }

        if (cd.getQuantity() > 0) {
            cd.borrow(user);
            return true;
        }

        return false;
    }

    public void returnCD(CD cd, User user) {
        boolean canReturn = cd.getBorrower() == user || cd.getQuantity() < 1;

        if (canReturn) {
            if (cd.isOverdue()) {
                user.addFine(cd.getFinePerDay());
            }
            cd.returnMedia();
        }
    }

    public void checkOverdueCDs() {
        for (CD cd : cds) {
            if (cd.getQuantity() < 1 && cd.isOverdue()) {
                setChanged();
                notifyObservers(cd);
            }
        }
    }
}

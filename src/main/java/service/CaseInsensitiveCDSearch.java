package service;

import model.CD;
import java.util.ArrayList;
import java.util.List;

public class CaseInsensitiveCDSearch implements SearchStrategy<CD> {

    @Override
    public List<CD> search(List<CD> cds, String query) {
        List<CD> results = new ArrayList<>();
        if (query == null || query.isEmpty()) return results;

        String lowerQuery = query.toLowerCase();

        for (CD cd : cds) {
            if (cd.getTitle().toLowerCase().contains(lowerQuery)
                || cd.getAuthor().toLowerCase().contains(lowerQuery)
                || cd.getId().toLowerCase().contains(lowerQuery)) {
                results.add(cd);
            }
        }
        return results;
    }
}

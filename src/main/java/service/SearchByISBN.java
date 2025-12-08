package service;

import model.Book;
import model.Media;

import java.util.ArrayList;
import java.util.List;

public class SearchByISBN<T extends Media> implements SearchStrategy<T> {
    @Override
    public List<T> search(List<T> list, String query) {
        List<T> result = new ArrayList<>();
        String q = query.toLowerCase();
        for (T item : list) {
            if (item.getIsbn().toLowerCase().contains(q)) {
                result.add(item);
            }
        }
        return result;
    }
}

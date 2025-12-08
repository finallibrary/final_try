package service;

import model.Book;
import java.util.List;

public interface SearchStrategy<T> {
    List<T> search(List<T> list, String query);
}


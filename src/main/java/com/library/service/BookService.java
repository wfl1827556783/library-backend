package com.library.service;

import com.library.entity.Book;
import java.util.List;

public interface BookService {

    Book addBook(Book book);

    Book updateBook(Book book);

    void deleteBook(Long id);

    Book findById(Long id);

    List<Book> findAll();
}

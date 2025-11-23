package com.library.service;

import com.library.entity.Borrow;
import java.util.List;

public interface BorrowService {

    Borrow borrowBook(Long userId, Long bookId);

    Borrow returnBook(Long borrowId);

    List<Borrow> findByUser(Long userId);

    Borrow findById(Long id);

    List<Borrow> findAll();
}

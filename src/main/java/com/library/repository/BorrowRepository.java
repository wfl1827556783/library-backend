package com.library.repository;

import com.library.entity.Book;
import com.library.entity.Borrow;
import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    List<Borrow> findByUser(User user);

    List<Borrow> findByUserAndBookAndReturnedFalse(User user, Book book);
}

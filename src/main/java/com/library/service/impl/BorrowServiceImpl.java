package com.library.service.impl;

import com.library.entity.Book;
import com.library.entity.Borrow;
import com.library.entity.User;
import com.library.exception.BusinessException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.UserRepository;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Borrow borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException("图书不存在"));

        if (book.getStock() <= 0) {
            throw new BusinessException("库存不足");
        }

        // 检查用户是否已经借阅了这本书且未归还
        List<Borrow> existingBorrows = borrowRepository.findByUserAndBookAndReturnedFalse(user, book);
        if (!existingBorrows.isEmpty()) {
            throw new BusinessException("您已经借阅了这本书，请先归还");
        }

        // 库存 -1
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        Borrow record = new Borrow();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowTime(LocalDateTime.now());
        record.setReturned(false);

        return borrowRepository.save(record);
    }

    @Override
    public Borrow returnBook(Long borrowId) {
        Borrow record = findById(borrowId);

        if (record.getReturned()) {
            throw new BusinessException("书籍已归还");
        }

        // 更新库存 +1
        Book book = record.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        record.setReturned(true);
        record.setReturnTime(LocalDateTime.now());

        return borrowRepository.save(record);
    }

    @Override
    public List<Borrow> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return borrowRepository.findByUser(user);
    }

    @Override
    public Borrow findById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new BusinessException("借阅记录不存在"));
    }

    @Override
    public List<Borrow> findAll() {
        return borrowRepository.findAll();
    }
}

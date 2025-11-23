package com.library.service.impl;

import com.library.entity.Book;
import com.library.entity.Category;
import com.library.exception.BusinessException;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Book addBook(Book book) {
        // 验证分类是否存在
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Category category = categoryRepository.findById(book.getCategory().getId())
                    .orElseThrow(() -> new BusinessException("分类不存在"));
            book.setCategory(category);
        }

        // 设置默认库存
        if (book.getStock() == null) {
            book.setStock(0);
        }

        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        Book existingBook = findById(book.getId());

        // 验证分类是否存在
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            Category category = categoryRepository.findById(book.getCategory().getId())
                    .orElseThrow(() -> new BusinessException("分类不存在"));
            book.setCategory(category);
        }

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("图书不存在");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("图书不存在"));
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException("分类不存在"));
        return bookRepository.findByCategory(category);
    }

    @Override
    public List<Book> findByTitleContaining(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }
}

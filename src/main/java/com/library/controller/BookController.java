package com.library.controller;

import com.library.common.Result;
import com.library.dto.BookDTO;
import com.library.entity.Book;
import com.library.entity.Category;
import com.library.repository.CategoryRepository;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookService.addBook(book);
        BookDTO result = convertToDTO(savedBook);
        return Result.success(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Result<BookDTO> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        book.setId(id);
        Book updatedBook = bookService.updateBook(book);
        BookDTO result = convertToDTO(updatedBook);
        return Result.success(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Result<?> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<BookDTO> getBookById(@PathVariable("id") Long id) {
        Book book = bookService.findById(id);
        BookDTO result = convertToDTO(book);
        return Result.success(result);
    }

    @GetMapping
    public Result<List<BookDTO>> getAllBooks(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                             @RequestParam(value = "keyword", required = false) String keyword) {
        List<Book> books;
        if (categoryId != null) {
            books = bookService.findByCategory(categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            books = bookService.findByTitleContaining(keyword);
        } else {
            books = bookService.findAll();
        }
        List<BookDTO> result = books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book, "categoryId");
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            book.setCategory(category);
        }
        return book;
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        BeanUtils.copyProperties(book, dto);
        if (book.getCategory() != null) {
            dto.setCategoryId(book.getCategory().getId());
        }
        return dto;
    }
}




package com.library.controller;

import com.library.common.Result;
import com.library.dto.BorrowDTO;
import com.library.dto.BorrowRequestDTO;
import com.library.entity.Borrow;
import com.library.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = "*")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public Result<BorrowDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO requestDTO) {
        Borrow borrow = borrowService.borrowBook(requestDTO.getUserId(), requestDTO.getBookId());
        BorrowDTO result = convertToDTO(borrow);
        return Result.success(result);
    }

    @PutMapping("/{id}/return")
    public Result<BorrowDTO> returnBook(@PathVariable Long id) {
        Borrow borrow = borrowService.returnBook(id);
        BorrowDTO result = convertToDTO(borrow);
        return Result.success(result);
    }

    @GetMapping("/user/{userId}")
    public Result<List<BorrowDTO>> getBorrowsByUser(@PathVariable Long userId) {
        List<Borrow> borrows = borrowService.findByUser(userId);
        List<BorrowDTO> result = borrows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<BorrowDTO> getBorrowById(@PathVariable Long id) {
        Borrow borrow = borrowService.findById(id);
        BorrowDTO result = convertToDTO(borrow);
        return Result.success(result);
    }

    @GetMapping
    public Result<List<BorrowDTO>> getAllBorrows() {
        List<Borrow> borrows = borrowService.findAll();
        List<BorrowDTO> result = borrows.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    private BorrowDTO convertToDTO(Borrow borrow) {
        BorrowDTO dto = new BorrowDTO();
        BeanUtils.copyProperties(borrow, dto);
        if (borrow.getUser() != null) {
            dto.setUserId(borrow.getUser().getId());
            dto.setUsername(borrow.getUser().getUsername());
        }
        if (borrow.getBook() != null) {
            dto.setBookId(borrow.getBook().getId());
            dto.setBookTitle(borrow.getBook().getTitle());
        }
        return dto;
    }
}




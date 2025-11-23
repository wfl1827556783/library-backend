package com.library.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowDTO {

    private Long id;

    private Long userId;

    private String username;

    private Long bookId;

    private String bookTitle;

    private LocalDateTime borrowTime;

    private LocalDateTime returnTime;

    private Boolean returned;
}




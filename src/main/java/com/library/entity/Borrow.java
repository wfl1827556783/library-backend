package com.library.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "borrow")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 借阅用户
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 借阅书籍
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    // 借出时间
    private LocalDateTime borrowTime;

    // 归还时间
    private LocalDateTime returnTime;

    // 是否归还
    private Boolean returned;
}

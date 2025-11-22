package com.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 书名
    private String title;

    // 作者
    private String author;

    // 库存数量
    private Integer stock;

    // 多对一，一个分类对应多本书
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

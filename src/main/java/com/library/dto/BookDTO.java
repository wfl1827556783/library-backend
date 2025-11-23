package com.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookDTO {

    private Long id;

    @NotBlank(message = "书名不能为空")
    private String title;

    private String author;

    private String isbn;

    private String description;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    private BigDecimal price;

    private Long categoryId;
}




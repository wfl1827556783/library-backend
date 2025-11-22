package com.library.common;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;     // 200 成功，其它失败
    private String message;   // 信息
    private T data;           // 数据

    // 成功，无数据
    public static <T> Result<T> success() {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        return r;
    }

    // 成功，有数据
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setData(data);
        r.setMessage("success");
        return r;
    }

    // 失败
    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }

    // 自定义 code + message
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}

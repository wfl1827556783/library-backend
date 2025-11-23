package com.library.exception;

import com.library.common.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 捕获业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBusinessException(BusinessException ex) {
        if (ex.getCode() != null) {
            return Result.error(ex.getCode(), ex.getMessage());
        }
        return Result.error(ex.getMessage());
    }

    // 捕获参数验证异常（@Valid）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String message = errors.values().stream()
                .collect(Collectors.joining(", "));
        return Result.error(400, "参数验证失败: " + message);
    }

    // 捕获约束违反异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return Result.error(400, "参数验证失败: " + message);
    }

    // 捕获数据完整性违反异常（如唯一约束）
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            return Result.error(400, "数据已存在，请勿重复添加");
        }
        return Result.error(400, "数据完整性错误: " + message);
    }

    // 捕获所有异常（兜底）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception ex) {
        // 记录完整堆栈信息（使用 SLF4J，便于集中式日志采集）
        logger.error("Unhandled exception", ex);
        
        // 开发环境可返回详细错误信息，生产环境返回通用信息
        String message = "系统异常，请联系管理员";
        String detailMessage = ex.getMessage();
        
        // 常见错误类型处理
        if (ex instanceof org.springframework.dao.DataAccessException) {
            message = "数据库操作异常: " + (detailMessage != null ? detailMessage : "请检查数据库连接");
        } else if (ex instanceof java.sql.SQLException) {
            message = "数据库连接异常: " + (detailMessage != null ? detailMessage : "请检查数据库配置");
        } else if (ex instanceof org.springframework.web.HttpRequestMethodNotSupportedException) {
            message = "请求方法不支持: " + detailMessage;
        } else if (ex instanceof org.springframework.http.converter.HttpMessageNotReadableException) {
            message = "请求参数格式错误: " + (detailMessage != null ? detailMessage : "请检查JSON格式");
        } else if (detailMessage != null && !detailMessage.isEmpty()) {
            // 如果有详细错误信息，在开发环境返回
            message = "系统异常: " + detailMessage;
        }
        
        return Result.error(500, message);
    }
}

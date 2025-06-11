package com.naz1k1.exception;

import com.naz1k1.model.R;
import com.naz1k1.model.enums.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> handleBusinessException(BusinessException e) {
        log.error("Business Exception: ", e);
        return R.failure(RCode.BAD_REQUEST);
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<?> handleUnauthorizedException(UnauthorizedException e) {
        log.error("Unauthorized Exception: ", e);
        return R.failure(RCode.UNAUTHORIZED);
    }

    /**
     * 处理权限异常
     */
    @ExceptionHandler(ForBiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<?> handleForbiddenException(ForBiddenException e) {
        log.error("Forbidden Exception: ", e);
        return R.failure(RCode.FORBIDDEN);
    }

    /**
     *  处理参数校验异常(@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return R.failure(RCode.BAD_REQUEST);
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> handleException(Exception e) {
        log.error("Unexpected Exception: ", e);
        return R.failure(RCode.INTERNAL_SERVER_ERROR);
    }

}

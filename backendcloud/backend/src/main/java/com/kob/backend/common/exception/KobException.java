package com.kob.backend.common.exception;

import com.kob.backend.common.api.R;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author August
 * @create 2022-08-31 15:06
 */
@RestControllerAdvice
public class KobException {

    /**
     * 捕获运行时异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public R handler(RuntimeException e) {
        return R.fail(e.getMessage());
    }

    /**
     * 实体校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        ObjectError error = bindingResult.getAllErrors().stream().findFirst().get();
        return R.fail(error.getDefaultMessage());
    }

    /**
     * Assert异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public R handler(IllegalArgumentException e) {
        return R.fail(e.getMessage());
    }

}

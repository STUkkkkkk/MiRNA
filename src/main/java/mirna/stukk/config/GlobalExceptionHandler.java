package mirna.stukk.config;

import lombok.extern.slf4j.Slf4j;
import mirna.stukk.utils.BaseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: stukk
 * @Description: TODO 全局异常处理类
 * @DateTime: 2023-05-27 23:04
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public Result baseExceptionHandler(BaseException baseException){
        log.error("出现错误:" + baseException);
        return Result.error(baseException.getCode(), baseException.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException runtimeException){
        log.error("出现错误:" + runtimeException);
        runtimeException.printStackTrace();
        return Result.error("500","系统错误");
    }



}

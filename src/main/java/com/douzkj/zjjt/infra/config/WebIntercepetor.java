package com.douzkj.zjjt.infra.config;

import com.douzkj.zjjt.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author ranger dong
 * @date 01:04 2025/3/24
 * @descrption
 * @copyright Spotter.ink
 */
@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
@Slf4j
public class WebIntercepetor  {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<Void> handleException(Exception e) {
        // 自定义异常处理逻辑，例如记录日志、返回自定义错误信息等
        log.error("请求异常.... An error occurred: ", e);
        return Response.fail("An error occurred: " + e.getMessage());
    }

    @ExceptionHandler(value = HttpClientErrorException.NotFound.class)
    @ResponseBody
    public Response<Void> handlNotFound(Exception e) {
        // 自定义异常处理逻辑，例如记录日志、返回自定义错误信息等
        return Response.fail404("Path not Found: " + e.getMessage());
    }
}

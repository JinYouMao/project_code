package com.online.messageboard.exception;

import com.online.messageboard.util.WebUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        WebUtil.writeJson(response, WebUtil.error("系统错误：" + e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        WebUtil.writeJson(response, WebUtil.error("运行时错误：" + e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public void handleNullPointerException(NullPointerException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        WebUtil.writeJson(response, WebUtil.error("空指针异常，请联系管理员"));
    }
}

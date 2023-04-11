package com.since.ocr.exception;

import com.since.ocr.builder.ErrorInfoBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * global exception handelr
 *
 * @author since
 * @date 2023-04-11 10:12
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static String DEFAULT_ERROR_VIEW = "error";//错误信息页

    @Autowired
    private ErrorInfoBuilder errorInfoBuilder;//错误信息的构建工具

    /**
     * 根据业务规则,统一处理异常。
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exceptionHandler(HttpServletRequest request, Throwable error) {
        log.error("global exception", error);
        //1.若为AJAX请求,则返回异常信息(JSON)
        if (isAjaxRequest(request)) {
            return errorInfoBuilder.getErrorInfo(request, error);
        }
        //2.其余请求,则返回指定的异常信息页(View).
        return new ModelAndView(DEFAULT_ERROR_VIEW, "errorInfo", errorInfoBuilder.getErrorInfo(request, error));
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}

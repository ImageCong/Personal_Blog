package com.example.demo.handler;

import com.example.demo.exception.BindingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常信息拦截器
 * 404、500等错误页面，Spring Boot会进行拦截并找到error/xxx.html的页面
 * <p>
 * ControllerAdvice会拦截所有的具有Controller注解的控制器
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    // 拦截所有Exception的子类
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        // 打印异常日志
        log.error("Request URL : {} ,Exception : ", request.getRequestURL(), e);

        // 如果有ResponseStatus注解的异常就不被此handler处理
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        if (e instanceof BindingException) {
            throw e;
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("exception", e);
        mav.setViewName("error/error");
        return mav;
    }

}

package com.huotu.scrm.web.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/***
 * 异常页面统一处理
 * Created by Jinxiangdong on 2016/7/19.
 */
@ControllerAdvice
public class ExceptionHandlerController {
    /***
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView error404(Exception exception) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("exception", exception);
        return mv;
    }

    /***
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView error500(Exception exception, WebRequest request) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("exception", exception);
        return mv;
    }
}

package com.huotu.scrm.web.controller.common;

import com.alibaba.fastjson.JSON;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 异常页面统一处理
 * Created by Jinxiangdong on 2016/7/19.
 */
@ControllerAdvice
public class ExceptionHandlerController {
    /***
     * 处理404 异常
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
     * 处理 500 异常
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView error500(Exception exception, HttpServletRequest request , HttpServletResponse response) {
        exception.printStackTrace();

        boolean isAjaxOrJson = isAjaxRequestOrBackJson( request);
        if(isAjaxOrJson){
            try {
                response.getWriter().write( exception.getMessage() );
            }catch (Exception ex){ex.printStackTrace();}
            return null;
        }

        ModelAndView mv = new ModelAndView("error");
        mv.addObject("exception", exception);
        return mv;
    }

    /***
     * 判断当前请求是否是ajax请求或者是返回json格式字符串
     * @param request
     * @return
     */
    private boolean isAjaxRequestOrBackJson( HttpServletRequest request){
        String accept = request.getHeader("accept");
        String x_request_with = request.getHeader("X-Requested-With" );
        if(!StringUtils.isEmpty( accept ) && accept.toLowerCase().contains("application/json")) return true;
        if( !StringUtils.isEmpty( x_request_with) && x_request_with.toLowerCase().contains("xmlhttprequest") ) return true;
        return false;
    }
}

package com.huotu.scrm.web.controller.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 异常页面统一处理
 * Created by Jinxiangdong on 2016/7/19.
 */
@ControllerAdvice
public class ExceptionHandlerController extends DefaultHandlerExceptionResolver{
    private Log logger = LogFactory.getLog(ExceptionHandlerController.class);


    @ExceptionHandler(Exception.class)
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("data error",ex);
        // TODO: 2017-08-11
        ModelAndView modelAndView =  super.doResolveException(request, response, handler, ex);
        if(modelAndView == null){
            modelAndView = new ModelAndView();
            modelAndView.addObject("errorState",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        boolean isAjaxOrJson = isAjaxRequestOrBackJson( request);
        if(isAjaxOrJson){
            try {
                response.getWriter().write( ex.getMessage() );
            }catch (Exception exception){exception.printStackTrace();}
            return null;
        }
        modelAndView.setViewName("error");
        modelAndView.addObject("exception", ex);
        return modelAndView;
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

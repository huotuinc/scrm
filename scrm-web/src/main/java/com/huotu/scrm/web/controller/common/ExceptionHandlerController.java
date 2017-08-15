package com.huotu.scrm.web.controller.common;

import com.huotu.scrm.common.utils.ApiResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 异常页面统一处理
 * Created by Jinxiangdong on 2016/7/19.
 */
@ControllerAdvice
public class ExceptionHandlerController extends DefaultHandlerExceptionResolver {
    private Log logger = LogFactory.getLog(ExceptionHandlerController.class);


    /**
     * 统一异常处理，期望实现如果是ajax请求，则返回 ApiResult 格式的string,否则返回error页面
     * 如果返回ModelAndView 就不能设置response.sendError,这两个是冲突的，所以在单元测试中异常的判断 status().XXX 是无效的,而是应该判断 attribute.status
     *
     * @param request  请求数据
     * @param response 返回数据
     * @param handler  the executed handler, or {@code null} if none chosen at the time
     *                 of the exception (for example, if multipart resolution failed)
     * @param ex       异常数据
     * @return
     */
    @ExceptionHandler(Exception.class)
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        boolean isAjaxOrJson = isAjaxRequestOrBackJson(request);
        int status = getErrorStatus(ex);
        if (isAjaxOrJson) {
            try {
                ApiResult result = new ApiResult(ex.getMessage(),status);
                response.getWriter().write(result.toString());
                logger.error(ex);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return null;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("status", status);
        modelAndView.addObject("exception", ex);
        return modelAndView;
    }

    private int getErrorStatus(Exception ex) {
        int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        if (ex instanceof BindException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof ConversionNotSupportedException) {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            status = HttpServletResponse.SC_NOT_ACCEPTABLE;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            status = HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMessageNotReadableException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof HttpMessageNotWritableException) {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            status = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
        } else if (ex instanceof MethodArgumentNotValidException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof MissingPathVariableException) {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (ex instanceof MissingServletRequestParameterException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof MissingServletRequestPartException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        } else if (ex instanceof NoHandlerFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND;
        } else if (ex instanceof TypeMismatchException) {
            status = HttpServletResponse.SC_NOT_FOUND;
        } else {
            logger.error(ex);
        }
        return status;
    }

    /***
     * 判断当前请求是否是ajax请求或者是返回json格式字符串
     * @param request
     * @return
     */
    private boolean isAjaxRequestOrBackJson(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String x_request_with = request.getHeader("X-Requested-With");
        if (!StringUtils.isEmpty(accept) && accept.toLowerCase().contains("application/json")) return true;
        if (!StringUtils.isEmpty(x_request_with) && x_request_with.toLowerCase().contains("xmlhttprequest"))
            return true;
        return false;
    }
}

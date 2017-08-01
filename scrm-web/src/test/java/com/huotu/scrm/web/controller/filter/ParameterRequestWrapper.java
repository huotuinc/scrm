/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.scrm.web.controller.filter;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * 可根据传入content让获取{@link HttpServletRequest#getParameter(String)}变得正确
 * Created by helloztt on 2016/4/5.
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String[]> params;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> newParams) throws IOException {
        super(request);
        this.params = newParams;
        // RequestDispatcher.forward parameter
        renewParameterMap(request);
    }

    @Override
    public String getParameter(String name) {
        String result;

        Object v = params.get(name);
        if (v == null) {
            result = null;
        } else {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                result = strArr[0];
            } else {
                result = null;
            }
        }

        return result;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return params;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<>(params.keySet()).elements();
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] result;

        Object v = params.get(name);
        if (v == null) {
            result = null;
        } else {
            result = (String[]) v;
        }

        return result;
    }

    /**
     * 处理post请求controller获取不到参数问题
     *
     * @param req 原生请求
     */
    private void renewParameterMap(HttpServletRequest req) throws IOException {
        // 只处理正确的content
        final MediaType mediaType;
        try {
            mediaType = MediaType.valueOf(req.getContentType());
        } catch (InvalidMediaTypeException ignored) {
            return;
        }

        if (!MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            return;
        }

        final String encoding = mediaType.getCharSet() != null ? mediaType.getCharSet().name() : "UTF-8";
        String queryString = StreamUtils.copyToString(req.getInputStream(), Charset.forName("UTF-8"));

        if (queryString.trim().length() > 0) {
            String[] params = queryString.split("&");

            for (String param : params) {
                int splitIndex = param.indexOf("=");
                if (splitIndex == -1) {
                    continue;
                }

                String key = param.substring(0, splitIndex);

                if (!this.params.containsKey(key)) {
                    if (splitIndex < param.length()) {
                        String value = param.substring(splitIndex + 1);

                        this.params.put(key, new String[]{URLDecoder.decode(value, encoding)});
                    }
                }
            }
        }
    }
}

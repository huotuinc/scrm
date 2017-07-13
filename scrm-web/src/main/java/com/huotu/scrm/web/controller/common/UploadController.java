/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.scrm.web.controller.common;

import com.huotu.scrm.web.service.StaticResourceService;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/19.
 */
@Controller
@RequestMapping("/common")
public class UploadController {
    @Autowired
    private StaticResourceService resourceServer;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> upLoad(
            @RequestParam(value = "customerId")Long customerId,
            @RequestParam(value = "userId",required = false)Long userId,
            @RequestParam(value = "infoId",required = false)Long infoId,
            @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        int result = 0;
        Map<Object, Object> responseData = new HashMap<Object, Object>();
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String path;
            if(userId != null){
                path = StaticResourceService.IMG + customerId + "/user/" + userId + "." + prefix;
            }else if(infoId != null){
                path = StaticResourceService.IMG + customerId + "/info/" + infoId + "." + prefix;
            }else {
                path = StaticResourceService.IMG + customerId + "/" + DateUtils.formatDate(now,"yyyyMMdd") + "/"
                        + DateUtils.formatDate(now, "yyyyMMddHHmmSS") + "." + prefix;
            }
            URI uri = resourceServer.uploadResource(null,path, files.getInputStream());
            responseData.put("fileUrl", uri);
            responseData.put("fileUri", path);
            result = 1;
        } catch (Exception e) {
            responseData.put("msg", e.getMessage());
        }
        responseData.put("result", result);
        return responseData;
    }

}

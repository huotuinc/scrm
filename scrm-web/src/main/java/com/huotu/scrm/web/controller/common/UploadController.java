/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.scrm.web.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.httputil.HttpClientUtil;
import com.huotu.scrm.common.httputil.HttpResult;
import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.common.ienum.UploadResourceEnum;
import com.huotu.scrm.web.service.StaticResourceService;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2015/5/19.
 */
@Controller
@RequestMapping("/common")
public class UploadController {
    @Autowired
    private StaticResourceService resourceServer;

    /**
     * 上传图片/文件
     *
     * @param customerId 商户ID
     * @param id         各种信息的主键，如果上传资讯相关文件则为资讯主键；如果上传名片相关图片则为名片主键
     * @param uploadType 参考{@link UploadResourceEnum}
     * @param files      文件信息
     * @return 文件信息
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> upLoad(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "uploadType", required = false) Integer uploadType,
            @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        int result = 0;
        Map<Object, Object> responseData = new HashMap<>();
        try {
            Date now = new Date();
            String fileName = files.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String path;
            UploadResourceEnum uploadResourceEnum = null;
            if (uploadType != null) {
                uploadResourceEnum = EnumHelper.getEnumType(UploadResourceEnum.class, uploadType);
            }
            if (id != null && uploadResourceEnum != null) {
                //如果有某一类的主键
                path = StaticResourceService.IMG + customerId + "/" + uploadResourceEnum.getValue() + "/" + id + "/";
            } else {
                path = StaticResourceService.IMG + customerId + "/";
            }
            path += (DateUtils.formatDate(now, "yyyyMMdd") + "/"
                    + DateUtils.formatDate(now, "yyyyMMddHHmmSS") + "." + prefix);
            URI uri = resourceServer.uploadResource(null, path, files.getInputStream());
            responseData.put("fileUrl", uri);
            responseData.put("fileUri", path);
            result = 1;
        } catch (Exception e) {
            responseData.put("msg", e.getMessage());
        }
        responseData.put("result", result);
        return responseData;
    }

    @RequestMapping(value = "/mall/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> upLoadToMall(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "btnFile", required = false) MultipartFile files) {
        int result = 0;
        Map<Object, Object> responseData = new HashMap<>();
        try {
            String fileName = files.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            BufferedImage image = ImageIO.read(files.getInputStream());
            BASE64Encoder encoder = new BASE64Encoder();
            String imgStr = encoder.encode(files.getBytes());
            boolean isSave = false;
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                Map<String, Object> map = new TreeMap<>();
                map.put("customid", customerId);
                map.put("base64Image", imgStr);
                map.put("size", width + "x" + height);
                map.put("extenName", prefix);

                HttpResult httpResult = HttpClientUtil.getInstance().post(SysConstant.HUOBANMALL_PUSH_URL + "/gallery/uploadPhoto", map);
                if (httpResult.getHttpStatus() == HttpStatus.SC_OK) {
                    JSONObject obj = JSONObject.parseObject(httpResult.getHttpContent());
                    if (obj.getIntValue("code") == 200) {
                        String fileUri = obj.getString("data");
                        URI uri = resourceServer.getResource(StaticResourceService.huobanmallMode, fileUri);
                        responseData.put("fileUrl", uri);
                        responseData.put("fileUri", fileUri);
                        responseData.put("msg", "上传成功！");
                        responseData.put("code", 200);
                        //ueidtor上传图片所需参数
                        responseData.put("url", uri);
                        responseData.put("title", fileUri);
                        responseData.put("original", fileUri);
                        responseData.put("state", "SUCCESS");

                        result = 1;
                        isSave = true;
                    }
                } else {
                    result = 0;
                    isSave = false;
                }
            }
            if (!isSave) {
                responseData.put("code", 500);
                responseData.put("msg", "请上传正方形文件");
            }
        } catch (Exception e) {
            responseData.put("msg", e.getMessage());
        }
        responseData.put("result", result);

        return responseData;
    }

}

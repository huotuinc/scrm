package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.SalesmanBusinessCard;
import com.huotu.scrm.service.service.BusinessCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

/**
 * 名片控制器
 * Created by jinxiangdong on 2017/7/11.
 */
//@RequestMapping("/businessCard")
@Controller
public class BusinessCardController extends MallBaseController{

    @Autowired
    private BusinessCardService businessCardService;

    /***
     * 编辑销售员名片信息
     * @param request
     * @param salesmanId
     * @param model
     * @return
     */
    @RequestMapping("/editBusinessCard")
    public String editBusinessCard(HttpServletRequest request , long salesmanId , Model model ){
        BusinessCard businessCard = businessCardService.getBusinessCard(salesmanId , getCustomerId(request));
        model.addAttribute("businessCard", businessCard);
        return "views/editbusinesscard";
    }


    @RequestMapping(value = "/uploadAvator" , method = RequestMethod.POST )
    @ResponseBody
    public ApiResult uploadAvator(HttpServletRequest request , MultipartFile uploadImage ){
//
//        try {
//            if( uploadImage == null || uploadImage.getSize() ==0 || uploadImage.isEmpty()){
//                return new ApiResult("请上传图片", ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultCode());
//            }
//            InputStream inputStream = uploadImage.getInputStream();
//            String dir = "avatar";
//            String path = request.getSession().getServletContext().getRealPath(dir);
//            String originalFileName = uploadImage.getOriginalFilename();
//            String type = uploadImage.getContentType();
//            if(type==null || !type.toLowerCase().startsWith("image/")) {
//                //return JSON.toJSONString(new Result(false, "不支持的文件类型，仅支持图片！"));
//                return new ApiResult("不支持的文件类型，仅支持图片！", ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultCode() );
//            }
//
//            System.out.println("file type:"+type);
//            String fileName = new Date().getTime()+""+new Random().nextInt(10000)+"_"+originalFileName.substring(originalFileName.lastIndexOf('.'));
//            System.out.println("文件路径："+path+":"+fileName);
//            String fullPath = path+  File.separator + "\\"+fileName;
//
//            File newFile=new File(path);
//            if(!newFile.exists()) newFile.mkdirs();
//
//            FileOutputStream fileOutputStream = new FileOutputStream(fullPath);
//            int byteCount=0;
//            int byteWritten=0;
//            byte[] buffer=new byte[1024];
//            while ( (byteCount = inputStream.read( buffer )) != -1 ){
//                //fileOutputStream.write( buffer , byteWritten , byteCount );
//                fileOutputStream.write(buffer);
//                byteWritten +=byteCount;
//            }
//            inputStream.close();
//            fileOutputStream.close();
//
            return  new ApiResult( ResultCodeEnum.SUCCESS.getResultMsg() , ResultCodeEnum.SUCCESS.getResultCode() );
//        }catch (IOException ioEx){
//            return new ApiResult(ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultMsg() , ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultCode());
//        }
    }


    @RequestMapping(value = "/updateBusinessCardInfo" , method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateBusinessCardInfo( int type , String value ){

        return new ApiResult(ResultCodeEnum.SUCCESS.getResultMsg(), ResultCodeEnum.SUCCESS.getResultCode());

    }
}

package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.common.ienum.EnumHelper;
import com.huotu.scrm.common.ienum.UploadResourceEnum;
import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.businesscard.BusinessCard;
import com.huotu.scrm.service.model.BusinessCardUpdateTypeEnum;
import com.huotu.scrm.service.service.BusinessCardService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * 名片控制器
 * Created by jinxiangdong on 2017/7/11.
 */
//@RequestMapping("/businessCard")
@Controller
public class BusinessCardController extends SiteBaseController {

    @Autowired
    private BusinessCardService businessCardService;

    @Autowired
    private StaticResourceService staticResourceService;


    /***
     * 编辑销售员名片信息
     * @param request
     * @param salesmanId
     * @param model
     * @return
     */
    @RequestMapping("/editBusinessCard")
    public String editBusinessCard(HttpServletRequest request , long customerId ,  long salesmanId , Model model ){
        BusinessCard businessCard = businessCardService.getBusinessCard(salesmanId , customerId );

        if(businessCard==null){
            businessCard=new BusinessCard();
            businessCard.setCustomerId(customerId);
            businessCard.setUserId(salesmanId);
        }

        model.addAttribute("businessCard", businessCard);

        return "businesscard/edit_businesscard";
    }


    /***
     * 上传头像接口
     * @param request
     * @param customerId
     * @param userId
     * @param btnFile
     * @return
     */
    @RequestMapping(value = "/uploadAvator" , method = RequestMethod.POST )
    @ResponseBody
    public ApiResult uploadAvator(HttpServletRequest request , Long customerId , Long userId , MultipartFile btnFile ){

        try {
            UploadResourceEnum uploadResourceType = UploadResourceEnum.USER;
            String fileName = btnFile.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String path = StaticResourceService.IMG + customerId + "/" + uploadResourceType.getValue() + "/" + userId + "." + prefix;
            String mode = null;
            //先删除原来的图片
            staticResourceService.deleteResource(path);
            //再上传最新的图片
            URI uri = staticResourceService.uploadResource(mode , path , btnFile.getInputStream());
            //然后保存图片的uri地址
            BusinessCard businessCard = businessCardService.updateBusinessCard( customerId , userId , BusinessCardUpdateTypeEnum.BUSINESS_CARD_UPDATE_TYPE_AVATAR , uri.toString() );

            ApiResult<BusinessCard> result = new ApiResult<>(ResultCodeEnum.SUCCESS.getResultMsg() , ResultCodeEnum.SUCCESS.getResultCode());
            result.setData( businessCard );
            return result;
        }catch (IOException ioEx){
            return new ApiResult( ioEx.getMessage() , ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultCode());
        }catch (URISyntaxException uriEx){
            return new ApiResult( uriEx.getMessage() , ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultCode());
        }
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
//            return  new ApiResult( ResultCodeEnum.SUCCESS.getResultMsg() , ResultCodeEnum.SUCCESS.getResultCode() );
//        }catch (IOException ioEx){
//            return new ApiResult(ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultMsg() , ResultCodeEnum.SYSTEM_BAD_REQUEST.getResultCode());
//        }
    }


    /***
     *
     * @param request
     * @param type
     * @param value
     * @return
     */
    @RequestMapping(value = "/updateBusinessCardInfo" , method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateBusinessCardInfo( HttpServletRequest request , Long customerId , Long userId, Integer type , String value ){

        //Long  customerId = this.getCustomerId( request );
        //Long userId = this.getUserId(request);
        BusinessCardUpdateTypeEnum typeEnum = EnumHelper.getEnumType( BusinessCardUpdateTypeEnum.class , type );
        BusinessCard businessCard = businessCardService.updateBusinessCard( customerId , userId , typeEnum , value);

        ApiResult<BusinessCard> result = new ApiResult<BusinessCard>(ResultCodeEnum.SUCCESS.getResultMsg(), ResultCodeEnum.SUCCESS.getResultCode() );
        result.setData(businessCard);
        return  result;
    }
}

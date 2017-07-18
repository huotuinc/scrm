package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.service.info.impl.InfoBrowseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 浏览量转发控制器
 * Created by luohaibo on 2017/7/12.
 */
@Controller
public class InfoBrowseController {

    @Autowired
    InfoBrowseServer infoBrowseServer;



    /**
     * 通过资讯id查找对应的资讯浏览记录
     *
     * @return
     */
    @RequestMapping("/browses")
    @ResponseBody
    public ApiResult infoBrowses(Long infoId){

        return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"成功",
                infoBrowseServer.InfoBrowseByInfoId(infoId));
    }


    @RequestMapping("/addBrowse")
    @ResponseBody
    public String insertBrowse(Long infoId){
        InfoBrowse infoBrowse = new InfoBrowse();
        infoBrowseServer.infoBroseSave(infoBrowse);
        return "";
    }


}

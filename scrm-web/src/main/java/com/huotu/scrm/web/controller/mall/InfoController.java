package com.huotu.scrm.web.controller.mall;


import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.service.InfoServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 资讯管理控制器
 * Created by luohaibo on 2017/7/10.
 */
@Controller
public class InfoController extends MallBaseController {

    private Log logger = LogFactory.getLog(InfoController.class);


    @Autowired
    InfoServer infoServer;
    private static final  int pageSize = 5;

    /***
     * 展示资讯首页内容
     * @param model
     * @return
     */
    @RequestMapping(value = "/infoHome")
    public String infoHomeLists(Model model){
        Page<Info> page = infoServer.infoSList(false,
                0,pageSize);
        model.addAttribute("infoLists",page.getContent());

        page.getContent().stream()
                .forEach(System.out::println);
        long account = infoServer.infoListsCount(false);
        model.addAttribute("totalAccount",account);

        return "info_list";
    }




    /**
     * 通过资讯标题模糊搜索相应的资讯列表
     * @param condition
     * @return
     */
    @RequestMapping("/search")
    @ResponseBody
    public ApiResult searchInfoListTitleLike(String condition){
        //todo
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"成功",
                infoServer.findListsByWord(condition));

    }


    /**
     * 获取行数记录总数
     * @param disable
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ApiResult getInfoListsAccount(boolean disable,int page){

        logger.info(infoServer.infoListsCount(disable)+"+++"+page);
        Map<String,Long> map = new HashMap<>();
        map.put("amount",infoServer.infoListsCount(disable));
        ApiResult apiResult = ApiResult.resultWith(ResultCodeEnum.SUCCESS,"成功",map);
        logger.info(apiResult);
        return  apiResult;

    }

    /**
     * 获取当前页的记录数
     * @param page
     * @return
     */
    @RequestMapping("/page")
    @ResponseBody
    public ApiResult getInfoListPageable(int page){

        return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"成功",infoServer.infoSList(false,
                page,pageSize)
        );

    }

    /**
     * 保存修改资讯
     * @param info
     * @return
     */
    @RequestMapping("saveInfo")
    public String saveInfo(Info info){
        infoServer.infoSave(info);
        return "index";
    }


}

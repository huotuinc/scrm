package com.huotu.scrm.web.controller.mall;


import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.service.InfoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 资讯管理控制器
 * Created by luohaibo on 2017/7/10.
 */
@Controller
public class InfoController extends MallBaseController {

    @Autowired
    InfoServer infoServer;


    private static final  int pageSize = 5;

    /**
     * 通过资讯标题模糊搜索相应的资讯列表
     * @param condition
     * @return
     */
    @RequestMapping("/search")
    @ResponseBody
    public ApiResult searchInfoListTitleLike(String condition){
        //todo
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"成功",infoServer.findListsByWord(condition));

    }


    /**
     * 获取行数记录总数
     * @param disable
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ApiResult getInfoListsAccount(boolean disable){
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"成功",infoServer.infoListsCount(disable));

    }


    /**
     * 获取当前页的记录数
     * @param page
     * @return
     */
    @RequestMapping("/page")
    @ResponseBody
    public ApiResult getInfoListPageable(int page){
        Pageable pageable = new PageRequest(page,pageSize);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS,"成功",infoServer.infoSList(false,pageable));

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

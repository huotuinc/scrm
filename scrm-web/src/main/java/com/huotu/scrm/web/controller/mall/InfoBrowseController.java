package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ExcelUtil;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.model.mall.UserModel;
import com.huotu.scrm.service.service.info.InfoBrowseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 浏览量转发控制器
 * Created by luohaibo on 2017/7/12.
 */
@Controller
public class InfoBrowseController extends MallBaseController{

    @Autowired
    InfoBrowseService infoBrowseService;

    /**
     * 查询转发记录
     * @param infoBrowseAndTurnSearch
     * @param customerId
     * @param model
     * @return
     */
    @RequestMapping("/info/turnRecord")
    public String infoTurnRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch, @ModelAttribute("customerId") Long customerId, Model model){
      infoBrowseAndTurnSearch.setCustomerId(customerId);
      Page<InfoBrowse> page =  infoBrowseService.infoTurnRecord(infoBrowseAndTurnSearch);
      model.addAttribute("infoTurnListPage",page);
      return "info/info_turn";
    }

    /***
     * 删除转发记录
     * @param infoBrowseAndTurnSearch
     * @return
     */
    @RequestMapping("/info/deleteTurn")
    @ResponseBody
    public ApiResult deleteTurn(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch){
        int count = infoBrowseService.updateInfoTurnRecord(infoBrowseAndTurnSearch);
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
    }

    /**
     * 获取某条资讯的浏览记录
     * @param infoBrowseAndTurnSearch
     * @param customerId
     * @param model
     * @return
     */
    @RequestMapping("/info/browseRecord")
    public String infoBrowseRecord(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch, @ModelAttribute("customerId") Long customerId, Model model){
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        Page<InfoBrowse> page =  infoBrowseService.infoBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("infoBrowseListPage",page);
        model.addAttribute("customerId",customerId);
        model.addAttribute("infoId",infoBrowseAndTurnSearch.getInfoId());
        return "info/info_browse";
    }

    /**
     * 下载浏览记录到Excel
     * @param infoBrowseAndTurnSearch
     * @param response
     * @throws IOException
     */
    @RequestMapping("/info/downloadBrowse")
    public void downloadBrowseToExcel(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch, HttpServletResponse response) throws IOException {
        List<UserModel> userModelList = infoBrowseService.infoBrowseRecordList(infoBrowseAndTurnSearch);
        String fileName = "资讯浏览记录列表";
        List<Map<String, Object>> mapList = createExcelRecord(userModelList);
        //列名
        String [] columnNames={"微信昵称","浏览时间","所属上线昵称","所属上线等级"};
        //map中的key
        String [] keys ={"wxNickName","browseTime","belongOneNickName","belongOneLevel"};
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ExcelUtil.createWorkBook(mapList,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

    private List<Map<String, Object>> createExcelRecord(List<UserModel> userModelList) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (int j = 0; j < userModelList.size(); j++) {
            UserModel userModel = userModelList.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("wxNickName", userModel.getWxNickName());
            LocalDateTime browseTime = userModel.getBrowseTime();
            mapValue.put("browseTime",browseTime == null? "": browseTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            mapValue.put("belongOneNickName",userModel.getBelongOneNickName());
            mapValue.put("belongOneLevel", userModel.getBelongOneLevel());
            listmap.add(mapValue);
        }
        return listmap;
    }

    /***
     * 删除转发记录
     * @param infoBrowseAndTurnSearch
     * @return
     */
    @RequestMapping("/info/deleteBrowse")
    @ResponseBody
    public ApiResult deleteBrowse(InfoBrowseAndTurnSearch infoBrowseAndTurnSearch){
        int count = infoBrowseService.updateInfoBrowse(infoBrowseAndTurnSearch);
        if(count > 0){
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS);
        }
        return ApiResult.resultWith(ResultCodeEnum.SAVE_DATA_ERROR);

    }



}

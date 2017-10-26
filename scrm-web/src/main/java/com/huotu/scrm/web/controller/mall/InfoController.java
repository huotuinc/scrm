package com.huotu.scrm.web.controller.mall;


import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ExcelUtil;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import com.huotu.scrm.service.entity.info.Info;
import com.huotu.scrm.service.entity.info.InfoBrowse;
import com.huotu.scrm.service.model.info.InfoBrowseAndTurnSearch;
import com.huotu.scrm.service.model.info.InfoExcelModel;
import com.huotu.scrm.service.model.info.InformationSearch;
import com.huotu.scrm.service.service.info.InfoBrowseService;
import com.huotu.scrm.service.service.info.InfoService;
import com.huotu.scrm.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 资讯管理控制器
 * Created by luohaibo on 2017/7/10.
 */
@Controller
public class InfoController extends MallBaseController {

    @Autowired
    private InfoService infoService;
    @Autowired
    private StaticResourceService staticResourceService;
    @Autowired
    private InfoBrowseService infoBrowseService;

    /***
     * 展示资讯首页内容
     * @param model
     * @return
     */
    @RequestMapping(value = "/info/infoList")
    public String infoHomeLists(InformationSearch informationSearch, @ModelAttribute("customerId") Long customerId, Model model) {
        informationSearch.setCustomerId(customerId);
        Page<Info> page = infoService.infoList(informationSearch);
        model.addAttribute("infoListsPage", page);
        model.addAttribute("customerId", customerId);
        model.addAttribute("informationSearch", informationSearch);
        return "info/info_list";
    }

    /**
     * 按时间等条件导出总表至Excel
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/info/download")
    public void downloadToExcel(HttpServletResponse response, String searchCondition, Long customerId, Date startDate, Date endDate) throws IOException {
        //条件查询出所有的资讯信息
        String fileName = "资讯推广列表" + LocalDateTime.now();
        InformationSearch informationSearch = new InformationSearch();
        informationSearch.setCustomerId(customerId);
        informationSearch.setSearchCondition(searchCondition);
        informationSearch.setStartDate(date2LocalDate(startDate));
        informationSearch.setEndDate(date2LocalDate(endDate));
        List<Object[]> infoList = infoService.queryInfoWithBrowse(informationSearch);
        List<Map<String, Object>> mapList = createExcelRecord(infoList);
        String columnNames[]={"资讯标题","资讯简介","创建时间","发布状态","推广状态","浏览人数"};//列名
        String keys[]    =     {"title","introduce","createTime","status","extend","infoBrowseNum"};//map中的key
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
            // Simple read/write loop.
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

    private LocalDateTime date2LocalDate(Date date) {
        if (date == null)
            return null;
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 商场点击进去详情页
     *
     * @param infoId
     * @param customerId
     * @param model
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/info/infoDetail")
    public String infoDetail(@ModelAttribute("customerId") Long customerId,
                             @RequestParam Long infoId,
                             Model model) throws URISyntaxException {
        Info info = infoService.findOneByIdAndCustomerId(infoId,customerId);
        if (!StringUtils.isEmpty(info.getImageUrl())) {
            info.setImageUrl(staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl()).toString());
        }
        int turnNum = infoBrowseService.countByTurn(infoId);
        int browse = infoBrowseService.countByBrowse(infoId);
        model.addAttribute("infoTurnNum", turnNum);
        model.addAttribute("browseNum", browse);
        model.addAttribute("customerId", customerId);
//        model.addAttribute("sourceUserId",sourceUserId);
        model.addAttribute("info", info);
        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(0);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        Page<InfoBrowse> page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("headImages", page.getContent());
        return "info/information_detail";
    }

    @RequestMapping(value = "/info/infoDetailBrowse")
    @SuppressWarnings("Duplicates")
    public String infoBrowse(@ModelAttribute("customerId") Long customerId,
                             @RequestParam Long infoId,
                             Model model) throws URISyntaxException {

        //浏览记录
        int browse = infoBrowseService.countByBrowse(infoId);
        model.addAttribute("browseNum", browse);

        InfoBrowseAndTurnSearch infoBrowseAndTurnSearch = new InfoBrowseAndTurnSearch();
        infoBrowseAndTurnSearch.setCustomerId(customerId);
        infoBrowseAndTurnSearch.setSourceType(1);
        infoBrowseAndTurnSearch.setInfoId(infoId);
        Page<InfoBrowse> page = infoBrowseService.infoSiteBrowseRecord(infoBrowseAndTurnSearch);
        model.addAttribute("headImages", page.getContent());
        return "info/browse_log";
    }

    /**
     * @param model
     * @return
     */
    @RequestMapping(value = "/info/edit")
    public String infoEditPage(@RequestParam(required = false, defaultValue = "0") Long id, Model model, @ModelAttribute("customerId") Long customerId) {
        Info info = infoService.findOneByIdAndCustomerId(id,customerId);
        if (info.getId() != null && info.getId() != 0) {
            if (info.getImageUrl() != null && !StringUtils.isEmpty(info.getImageUrl())) {
                URI imgUri = null;
                try {
                    imgUri = staticResourceService.getResource(StaticResourceService.huobanmallMode, info.getImageUrl());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                info.setMallImageUrl(imgUri.toString());
            }
        }
        try {
            model.addAttribute("mallSite",staticResourceService.getResource(StaticResourceService.huobanmallMode,""));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        model.addAttribute("customerId",customerId);
        model.addAttribute("info", info);
        return "info/info_edit";
    }

    /**
     * 保存修改资讯
     *
     * @param info
     * @return
     */
    @RequestMapping("/info/saveInfo")
    public String saveInfo(@ModelAttribute("customerId") Long customerId, Info info) {
        if (info.getCustomerId() == null || info.getCustomerId() == 0) {
            info.setCustomerId(customerId);
        }
        infoService.infoSave(info);
        return "redirect:/mall/info/infoList";
    }

    /**
     * 删除资讯
     *
     * @param customerId
     * @param id
     * @return
     */
    @RequestMapping("/info/deleteInfo")
    @ResponseBody
    public ApiResult deleteInfo(@ModelAttribute("customerId") Long customerId, Long id) {
        if (infoService.deleteInfo(customerId, id)) {
            return ApiResult.resultWith(ResultCodeEnum.SUCCESS, "删除成功");
        }
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS, "删除失败");
    }

    private List<Map<String, Object>> createExcelRecord(List<Object[]> infoList) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (int j = 0; j < infoList.size(); j++) {
            Object[] info = infoList.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("title", (String)info[0]);
            mapValue.put("introduce",(String)info[1]);
            LocalDateTime createTime = (LocalDateTime) info[2];
            mapValue.put("createTime",createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            if ((Boolean) info[3])
                mapValue.put("status","已发布");
            else
                mapValue.put("status","未发布");
            if ((Boolean) info[4])
                mapValue.put("extend","已推广");
            else
                mapValue.put("extend","未推广");
            mapValue.put("infoBrowseNum", (Long) info[5]);
            listmap.add(mapValue);
        }
        return listmap;
    }
}

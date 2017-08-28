package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.Constant;
import com.huotu.scrm.service.entity.report.DayReport;
import com.huotu.scrm.service.model.statisticinfo.SearchCondition;
import com.huotu.scrm.service.service.statisticsinfo.StatisticsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by hxh on 2017-08-28.
 */
@Controller
public class StatisticsInfoController extends MallBaseController {
    @Autowired
    private StatisticsInfoService statisticsInfoService;
    @RequestMapping("/Statistics/getDayReport")
    public String getDayReport(@ModelAttribute("searchCondition") SearchCondition searchCondition,
                               @RequestParam(required = false, defaultValue = "1") int pageNo, Model model) {
        Page<DayReport> reportListPage = statisticsInfoService.getDayReportList(searchCondition, pageNo);
        model.addAttribute("dayReportList",reportListPage.getContent());
        model.addAttribute("totalPages", reportListPage.getTotalPages());
        model.addAttribute("totalRecords", reportListPage.getTotalElements());
        model.addAttribute("pageSize", Constant.PAGE_SIZE);
        model.addAttribute("pageNo", pageNo);
        return "statisticsinfo/statistics_info";
    }
}

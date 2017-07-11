package com.huotu.scrm.web.controller.mall;

import com.huotu.scrm.common.utils.ApiResult;
import com.huotu.scrm.common.utils.ResultCodeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by helloztt on 2017-06-27.
 */
@Controller
public class TestControllerMall extends MallBaseController {

    @RequestMapping(value = "/test/index", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult index(@ModelAttribute("customerId") long customerId) {
        return ApiResult.resultWith(ResultCodeEnum.SUCCESS, "hello scrm,customerId:" + customerId + " !");
    }

    @RequestMapping(value = "/test/index/html", method = RequestMethod.GET)
    public ModelAndView htmlIndex(@ModelAttribute("customerId") long customerId) {
        ModelAndView model = new ModelAndView();
        model.setViewName("test");
        model.addObject("title", "hello scrm,customerId:" + customerId + " !");
        return model;
    }
}

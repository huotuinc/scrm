package com.huotu.scrm.web.controller.mall;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by helloztt on 2017-06-27.
 */
@Controller
public class TestController extends BaseController{

    @RequestMapping("/test/index")
    @ResponseBody
    public String index(@ModelAttribute("merchantId") long merchantId){
        return "hello scrm,merchantId:"+ merchantId+" !";
    }

    @RequestMapping("/test/index/html")
    public ModelAndView htmlIndex(@ModelAttribute("merchantId") long merchantId){
        ModelAndView model = new ModelAndView();
        model.setViewName("test");
        model.addObject("title","hello scrm,merchantId:"+ merchantId+" !");
        return model;
    }
}

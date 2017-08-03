package com.huotu.scrm.web.controller.site;

import com.huotu.scrm.service.entity.activity.ActPrize;
import com.huotu.scrm.service.entity.activity.Activity;
import com.huotu.scrm.service.service.activity.ActivityService;
import com.huotu.scrm.web.CommonTestBase;
import com.huotu.scrm.web.controller.mall.InfoController;
import com.huotu.scrm.web.service.StaticResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by luohaibo on 2017/8/3.
 */
public class ActWinControllerTest extends CommonTestBase{

    private Log logger = LogFactory.getLog(ActWinControllerTest.class);
    @Autowired
    StaticResourceService staticResourceService;
    @Autowired
    ActivityService activityService;

    @Test
    public void marketingActivity() throws Exception {

    }

    @Test
    public void joinAct() throws Exception {

    }

}
package com.huotu.scrm.service.service.info;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.info.InfoConfigure;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.junit.Assert.*;

/**
 * Created by luohaibo on 2017/7/19.
 */
public class InfoRewardConfigureServiceTest extends CommonTestBase {

    @Autowired
    InfoRewardConfigureService infoRewardConfigureService;

    @Test
    public void saveRewardConfigure() throws Exception {

        InfoConfigure infoConfigure = new InfoConfigure();
        infoConfigure.setCustomerId(0L);
        infoConfigure.setRewardSwitch(false);
        infoConfigure.setRewardScore(10);
        infoConfigure.setRewardLimitSwitch(false);
        infoConfigure.setRewardLimitNum(5);
        infoConfigure.setRewardUserType(0);
        infoConfigure.setExchangeSwitch(false);
        infoConfigure.setExchangeRate(10);
        infoConfigure.setExchangeUserType(0);
        infoConfigure.setDayExchangeLimit(500);
        InfoConfigure infoConfigure1 = infoRewardConfigureService.saveRewardConfigure(infoConfigure);

    }

    @Test
    public void readRewardConfigure() throws Exception {

        InfoConfigure infoConfigure = infoRewardConfigureService.readRewardConfigure(4423L);

    }

}
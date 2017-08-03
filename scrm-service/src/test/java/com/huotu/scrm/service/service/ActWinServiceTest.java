package com.huotu.scrm.service.service;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.repository.activity.ActWinDetailRepository;
import com.huotu.scrm.service.service.activity.ActPrizeService;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by hxh on 2017-08-02.
 */
public class ActWinServiceTest extends CommonTestBase {

    @Autowired
    private ActWinDetailService actWinDetailService;
    @Autowired
    private ActPrizeService actPrizeService;
    @Autowired
    private ActWinDetailRepository actWinDetailRepository;

    @Test
    public void testActWinDetailService() {
        Page<ActWinDetail> winDetailList = actWinDetailService.getPageActWinDetail(1, 20);
        List<ActWinDetail> content = winDetailList.getContent();
        Assert.assertNotNull(content);
    }
}

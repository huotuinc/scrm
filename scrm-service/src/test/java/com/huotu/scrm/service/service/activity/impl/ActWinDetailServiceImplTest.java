package com.huotu.scrm.service.service.activity.impl;

import com.huotu.scrm.service.CommonTestBase;
import com.huotu.scrm.service.entity.activity.ActWinDetail;
import com.huotu.scrm.service.service.activity.ActWinDetailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by luohaibo on 2017/8/11.
 */
public class ActWinDetailServiceImplTest extends CommonTestBase{

    @Autowired
    private ActWinDetailService actWinDetailService;

    @Test
    public void getPageActWinDetailByUserId() throws Exception {

        List<ActWinDetail> page = actWinDetailService.getActWinDetailRecordByActIdAndUserId(2L,1058823L);

    }

}